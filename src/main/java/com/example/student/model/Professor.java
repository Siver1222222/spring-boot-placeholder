package com.example.student.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "professor_id")
    private long id;

    private String name;
    private String department;

    // One-to-Many relationship with Course
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    // Many-to-Many relationship with Student (for advisees)
    @ManyToMany
    @JoinTable(
            name = "professor_advisee",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> advisees = new HashSet<>();

    // Getters, setters, and helper methods
    public void addCourse(Course course) {
        courses.add(course);
        course.setProfessor(this);
    }

    public void addAdvisee(Student student) {
        advisees.add(student);
        student.getAdvisors().add(this);
    }
}