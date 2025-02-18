package com.example.student.repository;

import com.example.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {
    // Simple conditions
    List<Student> findByMajor(String major);
    List<Student> findByGpaGreaterThan(double gpa);

    // You can create a query by using the keyword And without writing the query
    List<Student> findStudentsByNameAndAdvisorsEmpty(String name);

    // Multiple conditions with And/Or
    List<Student> findByMajorAndGpaGreaterThan(String major, double gpa);

    // Ordering results
    List<Student> findByMajorOrderByGpaDesc(String major);

    // Using Like for pattern matching
    List<Student> findByNameContainingIgnoreCase(String namePattern);

    // Limiting results
    List<Student> findTop3ByOrderByGpaDesc();

    // Using In clause
    List<Student> findByMajorIn(List<String> majors);}