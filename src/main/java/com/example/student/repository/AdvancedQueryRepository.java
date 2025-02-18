package com.example.student.repository;

import com.example.student.model.Course;
import com.example.student.dto.CourseSearchResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvancedQueryRepository extends JpaRepository<Course, Long> {
    @Query(
            value = "SELECT c.*, COUNT(e.student_id) as enrollment_count " +
                    "FROM course c " +
                    "LEFT JOIN course_enrollment e ON c.id = e.course_id " +
                    "GROUP BY c.id " +
                    "HAVING enrollment_count >= :minEnrollment",
            nativeQuery = true
    )
    List<Course> findCoursesWithMinEnrollment(@Param("minEnrollment") int minEnrollment);

    @Query(
            value = "SELECT c.* " +
                    "FROM course c " +
                    "LEFT JOIN course_enrollment e ON c.id = e.course_id " +
                    "GROUP BY c.id " +
                    "ORDER BY COUNT(e.student_id) DESC " +
                    "LIMIT :limit",
            nativeQuery = true
    )
    List<Course> findTopPopularCourses(@Param("limit") int limit);

    @Query(
            value = "WITH avg_gpa AS (" +
                    "  SELECT c.id, AVG(s.gpa) as avg_student_gpa " +
                    "  FROM course c " +
                    "  JOIN course_enrollment ce ON c.id = ce.course_id " +
                    "  JOIN students s ON ce.student_id = s.id " +
                    "  GROUP BY c.id" +
                    ") " +
                    "SELECT c.* FROM course c " +
                    "JOIN avg_gpa ag ON c.id = ag.id " +
                    "WHERE ag.avg_student_gpa >= :minGpa",
            nativeQuery = true
    )
    List<Course> findCoursesWithHighAchievingStudents(@Param("minGpa") double minGpa);
}