package com.example.student.service;

import com.example.student.dto.*;
import com.example.student.mapper.CourseMapper;
import com.example.student.model.Course;
import com.example.student.model.Professor;
import com.example.student.model.Student;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.ProfessorRepository;
import com.example.student.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional // means all methods in this class are transactional
@RequiredArgsConstructor
public class AcademicService {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    @PersistenceContext
    private EntityManager entityManager;

    // Non-paged methods
    public List<CourseBasicDTO> getAllCoursesBasic() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toCourseBasicDTOs(courses);
    }

    public List<CourseBasicDTO> getCoursesByDepartment(String department) {
        List<Course> courses = courseRepository.findByDepartment(department);
        return courseMapper.toCourseBasicDTOs(courses);
    }

    // Paged methods
    public Page<CourseBasicDTO> getAllCoursesPageable(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseMapper::toCourseBasicDTO);
    }

    // Course management methods
    public CourseDetailDTO createCourse(CourseRequestDTO request) {
        Course course = courseMapper.toCourse(request);
        course.setEnrolledStudents(new HashSet<>());

        // First save the course without professor
        Course savedCourse = courseRepository.save(course);

        // Then set professor if provided
        if (request.getProfessorId() != -1) {
            Professor professor = professorRepository.getProfessorById(request.getProfessorId());
            savedCourse.setProfessor(professor);
            savedCourse = courseRepository.save(savedCourse);
        }

        return courseMapper.toCourseDetailDTO(savedCourse);
    }

    public CourseDetailDTO updateCourse(Long courseId, CourseRequestDTO request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Use mapper to update the course
        courseMapper.updateCourseFromDTO(request, course);

        if (request.getProfessorId() != -1) {
            Professor professor = professorRepository.findById(request.getProfessorId())
                    .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
            course.setProfessor(professor);
        }

        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseDetailDTO(savedCourse);
    }

    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }

    // Assignment methods remain the same...
    public void assignProfessorToCourse(Long professorId, Long courseId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new EntityNotFoundException("Professor not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        course.setProfessor(professor);
        courseRepository.save(course);
    }

    public void enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (course.getEnrolledStudents().contains(student)) {
            throw new IllegalStateException("Student already enrolled in this course");
        }

        course.enrollStudent(student);
        courseRepository.save(course);
    }
}