package com.example.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseBasicDTO {
    private long id;
    private String courseCode;
    private String title;
}