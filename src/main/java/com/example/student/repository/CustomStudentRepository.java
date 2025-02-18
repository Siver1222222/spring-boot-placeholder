package com.example.student.repository;

import com.example.student.model.Student;

import java.util.List;

public interface CustomStudentRepository {
    List<Student> findStudentsWithComplexCriteria(
            String majorPattern,
            Double minGpa,
            Integer graduationYear,
            List<String> excludedCourses
    );
}