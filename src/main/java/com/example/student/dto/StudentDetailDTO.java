package com.example.student.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class StudentDetailDTO {
    private Long id;
    private String name;
    private String major;
    private Double gpa;
    private StudentProfileDTO profile;
    private Set<CourseBasicDTO> enrolledCourses;
    private Set<ProfessorBasicDTO> advisors;

    @Data
    public static class StudentProfileDTO {
        private Long id;
        private String email;
        private String phoneNumber;
        private LocalDate dateOfBirth;
    }

    @Data
    public static class ProfessorBasicDTO {
        private Long id;
        private String name;
        private String department;
    }
}