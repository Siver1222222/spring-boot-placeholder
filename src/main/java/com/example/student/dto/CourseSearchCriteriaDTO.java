package com.example.student.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CourseSearchCriteriaDTO {
    private String courseCode;
    private String title;
    private String department;
    private String professorName;

    @Min(0)
    private Integer minCredits;

    @Min(0)
    private Integer minEnrollment;

    private Boolean hasAvailableSeats;

    // For sorting
    private String sortBy = "courseCode";
    private String sortDirection = "ASC";

    // For advanced filtering
    private Double minAverageGrade;
    private Boolean isActive;
}