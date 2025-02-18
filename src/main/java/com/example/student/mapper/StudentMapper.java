package com.example.student.mapper;

import org.mapstruct.*;
import com.example.student.dto.*;
import com.example.student.model.Student;
import com.example.student.model.Professor;
import com.example.student.model.Course;
import com.example.student.model.StudentProfile;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CourseMapper.class}) // uses is used to specify other mappers that this mapper depends on
public interface StudentMapper {

    @Named("toStudentProfileDTO")
    default StudentDetailDTO.StudentProfileDTO toStudentProfileDTO(StudentProfile profile) {
        if (profile == null) {
            return null;
        }
        StudentDetailDTO.StudentProfileDTO dto = new StudentDetailDTO.StudentProfileDTO();
        dto.setId(profile.getId());
        dto.setEmail(profile.getEmail());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setDateOfBirth(profile.getDateOfBirth());
        return dto;
    }

    @Named("toProfessorBasicDTO")
    default StudentDetailDTO.ProfessorBasicDTO toProfessorBasicDTO(Professor professor) {
        if (professor == null) {
            return null;
        }
        StudentDetailDTO.ProfessorBasicDTO dto = new StudentDetailDTO.ProfessorBasicDTO();
        dto.setId(professor.getId());
        dto.setName(professor.getName());
        dto.setDepartment(professor.getDepartment());
        return dto;
    }

    @Named("mapCourseToBasicDTO")
    default CourseBasicDTO mapCourseToBasicDTO(Course course) {
        if (course == null) {
            return null;
        }
        CourseBasicDTO dto = new CourseBasicDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setTitle(course.getTitle());
        return dto;
    }

    @Named("mapCoursesToSet")
    default Set<CourseBasicDTO> mapCoursesToSet(Collection<Course> courses) {
        if (courses == null) {
            return null;
        }
        return courses.stream()
                .map(this::mapCourseToBasicDTO)
                .collect(Collectors.toSet());
    }

    @Named("mapProfessorsToSet")
    default Set<StudentDetailDTO.ProfessorBasicDTO> mapProfessorsToSet(Collection<Professor> professors) {
        if (professors == null) {
            return null;
        }
        return professors.stream()
                .map(this::toProfessorBasicDTO)
                .collect(Collectors.toSet());
    }

    // Basic mapping method
    StudentBasicDTO toStudentBasicDTO(Student student);

    // Detailed mapping method
    @Mapping(target = "profile", source = "profile", qualifiedByName = "toStudentProfileDTO")
    @Mapping(target = "enrolledCourses", source = "enrolledCourses", qualifiedByName = "mapCoursesToSet")
    @Mapping(target = "advisors", source = "advisors", qualifiedByName = "mapProfessorsToSet")
    StudentDetailDTO toStudentDetailDTO(Student student);

    // Search result mapping method
    @Mapping(target = "email", source = "profile.email")
    @Mapping(target = "enrolledCoursesCount", expression = "java(student.getEnrolledCourses().size())")
    StudentSearchResultDTO toStudentSearchResultDTO(Student student);
}