package com.example.student.dto;

import lombok.Data;

@Data
public class StudentBasicDTO {
    private Long id;
    private String name;
    private String major;
    private Double gpa;
}