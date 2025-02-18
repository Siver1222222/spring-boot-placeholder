package com.example.student.dto;

import lombok.Data;

@Data
public class StudentSearchResultDTO {
    private Long id;
    private String name;
    private String major;
    private Double gpa;
    private String email;
    private int enrolledCoursesCount;
}