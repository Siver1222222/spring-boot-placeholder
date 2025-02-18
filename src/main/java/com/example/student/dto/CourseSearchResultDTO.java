package com.example.student.dto;

import lombok.Data;

@Data
public class CourseSearchResultDTO {
    private long id;
    private String code;
    private String title;
    private String professorName;
    private int enrollmentCount;
    private double averageGrade;
}