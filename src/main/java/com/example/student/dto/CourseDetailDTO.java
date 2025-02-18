package com.example.student.dto;

import com.example.student.model.Professor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseDetailDTO {
    private long id;
    private String courseCode;
    private String title;
    private ProfessorDTO professor;
    private int enrollmentCount;
    private String department;
    private int credits;

    @Data
    public static class ProfessorDTO {
        private Long id;
        private String name;
        private String department;

        public ProfessorDTO(Professor professor) {
            this.id = professor.getId();
            this.name = professor.getName();
            this.department = professor.getDepartment();
        }

        public ProfessorDTO(Long id, String name, String department) {
            this.id = id;
            this.name = name;
            this.department = department;
        }
    }
}