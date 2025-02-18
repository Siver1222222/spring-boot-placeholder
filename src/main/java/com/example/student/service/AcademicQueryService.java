package com.example.student.service;

import com.example.student.dto.CourseSearchCriteriaDTO;
import com.example.student.dto.CourseSearchResultDTO;
import com.example.student.dto.StudentSearchResultDTO;
import com.example.student.mapper.StudentMapper;
import com.example.student.model.Course;
import com.example.student.model.Student;
import com.example.student.repository.AdvancedQueryRepository;
import com.example.student.repository.StudentQueryRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // This annotation is used to indicate that the methods in this class are read-only
@RequiredArgsConstructor
public class AcademicQueryService {

    private final AdvancedQueryRepository advancedQueryRepository;
    private final StudentQueryRepository studentQueryRepository;
    private final StudentMapper studentMapper;  // Add this field


    @PersistenceContext // This annotation is used to inject the EntityManager into the class
    private EntityManager entityManager; // This field is used to interact with the database

    public List<CourseSearchResultDTO> findTopPopularCourses(int limit) {
        List<Course> courses = advancedQueryRepository.findTopPopularCourses(limit);
        return courses.stream()
                .map(this::mapToSearchResult)
                .collect(Collectors.toList());
    }

    public List<StudentSearchResultDTO> findTopStudentsByGpa(int limit) {
        List<Student> students = studentQueryRepository.findTopStudentsByGpa(limit);
        return students.stream()
                .map(studentMapper::toStudentSearchResultDTO)
                .collect(Collectors.toList());
    }

    public Page<CourseSearchResultDTO> searchCourses(CourseSearchCriteriaDTO criteria, Pageable pageable) {
        // Create the base query
        String queryStr = "SELECT c FROM Course c WHERE 1=1 ";
        String countQueryStr = "SELECT COUNT(c) FROM Course c WHERE 1=1 ";

        // Parameter map for values
        Map<String, Object> parameters = new HashMap<>();

        // Build query conditions
        StringBuilder conditions = new StringBuilder();

        if (criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
            conditions.append(" AND LOWER(c.title) LIKE :title");
            parameters.put("title", "%" + criteria.getTitle().toLowerCase() + "%");
        }

        if (criteria.getCourseCode() != null && !criteria.getCourseCode().isEmpty()) {
            conditions.append(" AND LOWER(c.courseCode) LIKE :courseCode");
            parameters.put("courseCode", "%" + criteria.getCourseCode().toLowerCase() + "%");
        }

        if (criteria.getDepartment() != null && !criteria.getDepartment().isEmpty()) {
            conditions.append(" AND c.department = :department");
            parameters.put("department", criteria.getDepartment());
        }

        if (criteria.getMinCredits() != null) {
            conditions.append(" AND c.credits >= :minCredits");
            parameters.put("minCredits", criteria.getMinCredits());
        }

        // Add conditions to queries
        queryStr += conditions.toString();
        countQueryStr += conditions.toString();

        // Add sorting
        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
            queryStr += " ORDER BY c." + criteria.getSortBy() + " " +
                    (criteria.getSortDirection() != null ? criteria.getSortDirection() : "ASC");
        }

        // Create queries
        TypedQuery<Course> query = entityManager.createQuery(queryStr, Course.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);

        // Set parameters for both queries
        parameters.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Execute queries
        List<Course> courses = query.getResultList();
        Long total = countQuery.getSingleResult();

        // Convert to DTOs
        List<CourseSearchResultDTO> dtos = courses.stream()
                .map(this::mapToSearchResult)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }

    private CourseSearchResultDTO mapToSearchResult(Course course) {
        CourseSearchResultDTO dto = new CourseSearchResultDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCourseCode());
        dto.setTitle(course.getTitle());
        if (course.getProfessor() != null) {
            dto.setProfessorName(course.getProfessor().getName());
        }
        dto.setEnrollmentCount(course.getEnrolledStudents().size());
        return dto;
    }

    public Page<StudentSearchResultDTO> searchStudents(
            String major,
            Double minGpa,
            String courseEnrolled,
            String advisorName,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Create main query
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> student = query.from(Student.class);

        // Create count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Student> countRoot = countQuery.from(Student.class);

        // Build predicates
        List<Predicate> predicates = buildPredicates(cb, student, major, minGpa, courseEnrolled);
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, major, minGpa, courseEnrolled);

        // Apply predicates to both queries
        query.where(predicates.toArray(new Predicate[0]));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        // Add select and count
        query.select(student);
        countQuery.select(cb.count(countRoot));

        // Execute queries
        TypedQuery<Student> typedQuery = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        List<Student> students = typedQuery.getResultList();
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // Convert to DTOs
        List<StudentSearchResultDTO> dtos = students.stream()
                .map(studentMapper::toStudentSearchResultDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Student> student,
            String major,
            Double minGpa,
            String courseEnrolled) {

        List<Predicate> predicates = new ArrayList<>();

        if (major != null && !major.isEmpty()) {
            predicates.add(cb.equal(student.get("major"), major));
        }

        if (minGpa != null) {
            predicates.add(cb.greaterThanOrEqualTo(student.get("gpa"), minGpa));
        }

        if (courseEnrolled != null && !courseEnrolled.isEmpty()) {
            Join<Student, Course> courseJoin = student.join("enrolledCourses");
            predicates.add(cb.equal(courseJoin.get("courseCode"), courseEnrolled));
        }

        return predicates;
    }
}