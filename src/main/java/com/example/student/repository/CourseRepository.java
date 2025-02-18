package com.example.student.repository;

import com.example.student.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>,
        JpaSpecificationExecutor<Course> {
    List<Course> findByDepartment(String department);
    List<Course> findByCourseCodeContainingIgnoreCase(String code);
    List<Course> findByTitleContainingIgnoreCase(String title);
    // Basic JPQL query
    @Query("SELECT c FROM Course c WHERE c.department = :dept AND c.credits >= :minCredits")
    List<Course> findCoursesByDepartmentAndMinCredits(
            @Param("dept") String department,
            @Param("minCredits") int minCredits
    );

    // Join with other entities
    @Query("SELECT c FROM Course c JOIN c.professor p WHERE p.department = :dept")
    List<Course> findCoursesByProfessorDepartment(@Param("dept") String department);

    // Aggregate functions
    @Query("SELECT c.department, COUNT(c) FROM Course c GROUP BY c.department")
    List<Object[]> countCoursesByDepartment();

    // Complex conditions
    @Query("SELECT c FROM Course c WHERE c.credits > :minCredits " +
            "AND SIZE(c.enrolledStudents) >= :minStudents " +
            "ORDER BY SIZE(c.enrolledStudents) DESC")
    List<Course> findPopularCourses(
            @Param("minCredits") int minCredits,
            @Param("minStudents") int minStudents
    );

    // Update queries
    @Modifying
    @Query("UPDATE Course c SET c.credits = :newCredits WHERE c.courseCode = :code")
    int updateCourseCredits(@Param("code") String code, @Param("newCredits") int newCredits);
}