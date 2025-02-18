package com.example.student.repository;

import com.example.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentQueryRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM student s ORDER BY s.gpa DESC LIMIT :limit",
            nativeQuery = true)
    List<Student> findTopStudentsByGpa(@Param("limit") int limit);

    @Query(value = "SELECT s.* FROM student s " +
            "JOIN course_enrollment ce ON s.id = ce.student_id " +
            "GROUP BY s.id " +
            "HAVING COUNT(ce.course_id) >= :minCourses",
            nativeQuery = true)
    List<Student> findStudentsWithMinEnrollments(@Param("minCourses") int minCourses);
}