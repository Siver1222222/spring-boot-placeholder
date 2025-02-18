package com.example.student.repository;

import com.example.student.model.Course;
import com.example.student.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class CustomStudentRepositoryImpl implements CustomStudentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Find students matching multiple optional criteria using JPA Criteria API
     *
     * @param majorPattern Pattern to match student major (e.g., "Computer%" for Computer Science)
     * @param minGpa Minimum GPA threshold
     * @param graduationYear Expected graduation year
     * @param excludedCourses List of course codes to exclude from results
     * @return List of Student entities matching all provided criteria
     *
     * Builds a dynamic query that:
     * - Filters by major using LIKE if majorPattern provided
     * - Filters by minimum GPA if minGpa provided
     * - Filters by graduation year if graduationYear provided
     * - Excludes students enrolled in any course from excludedCourses list
     * - Orders results by GPA descending
     *
     * Example usage:
     * findStudentsWithComplexCriteria(
     *     "Computer%",    // Major starting with "Computer"
     *     3.5,           // Minimum GPA of 3.5
     *     2024,          // Graduating in 2024
     *     List.of("CS101", "CS102")  // Exclude these courses
     * )
     *
     * Implementation steps:
     * 1. Create criteria builder and query for Student entity
     * 2. Build list of predicates based on non-null parameters:
     *    - majorPattern: LIKE condition with wildcards
     *    - minGpa: >= comparison
     *    - graduationYear: exact match
     *    - excludedCourses: LEFT JOIN with courses and NOT IN condition
     * 3. Combine all predicates with AND
     * 4. Order by GPA descending
     * 5. Execute and return results
     */    @Override
    public List<Student> findStudentsWithComplexCriteria(
            String majorPattern,
            Double minGpa,
            Integer graduationYear,
            List<String> excludedCourses) {


        // CriteriaBuilder: a JPA interface that helps build type-safe SQL queries
        // programmatically. It's used to create dynamic queries without writing
        // raw SQL or JPQL strings.
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> student = query.from(Student.class);

        List<Predicate> predicates = new ArrayList<>();

        // Add dynamic conditions based on parameters
        if (majorPattern != null) {
            predicates.add(cb.like(student.get("major"), "%" + majorPattern + "%"));
        }

        if (minGpa != null) {
            predicates.add(cb.greaterThanOrEqualTo(student.get("gpa"), minGpa));
        }

        if (graduationYear != null) {
            predicates.add(cb.equal(student.get("graduationYear"), graduationYear));
        }

        if (excludedCourses != null && !excludedCourses.isEmpty()) {
            Join<Student, Course> courseJoin = student.join("courses", JoinType.LEFT);
            predicates.add(courseJoin.get("code").in(excludedCourses).not());
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(student.get("gpa")));

        return entityManager.createQuery(query).getResultList();
    }
}