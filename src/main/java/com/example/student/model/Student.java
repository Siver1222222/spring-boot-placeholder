package com.example.student.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String major;
    private Double gpa;

    // Many-to-Many relationship with Course
    @ManyToMany(mappedBy = "enrolledStudents")
    private Set<Course> enrolledCourses = new HashSet<>();

    // Many-to-Many relationship with Professor (advisors)
    @ManyToMany(mappedBy = "advisees")
    private Set<Professor> advisors = new HashSet<>();

    // One-to-One relationship with StudentProfile
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private StudentProfile profile;

}