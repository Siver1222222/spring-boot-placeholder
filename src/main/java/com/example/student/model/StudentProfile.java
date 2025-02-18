package com.example.student.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    private String phoneNumber;


    private LocalDate dateOfBirth;

    // One-to-One relationship with Student
    @OneToOne(mappedBy = "profile")
    private Student student;
}