package com.example.student.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRequestDTO {
    @NotNull
    @Size(min = 2, max = 10)
    private String code;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private String department;

    @Min(1) @Max(6)
    private int credits;

    private long professorId;
}