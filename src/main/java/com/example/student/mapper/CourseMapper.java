package com.example.student.mapper;

import org.mapstruct.*;
import com.example.student.dto.*;
import com.example.student.model.Course;
import com.example.student.model.Professor;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CourseMapper {

    @Mapping(target = "courseCode", source = "code")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    Course toCourse(CourseRequestDTO courseDTO);

    CourseBasicDTO toCourseBasicDTO(Course course);

    @Mapping(target = "professor", source = "professor", qualifiedByName = "toProfessorDTO")
    @Mapping(target = "enrollmentCount", expression = "java(course.getEnrolledStudents() != null ? course.getEnrolledStudents().size() : 0)")
    CourseDetailDTO toCourseDetailDTO(Course course);

    @Mapping(target = "code", source = "courseCode")
    @Mapping(target = "professorName", source = "professor.name")
    @Mapping(target = "enrollmentCount", expression = "java(course.getEnrolledStudents() != null ? course.getEnrolledStudents().size() : 0)")
    CourseSearchResultDTO toCourseSearchResultDTO(Course course);

    List<CourseBasicDTO> toCourseBasicDTOs(List<Course> courses);
    List<CourseDetailDTO> toCourseDetailDTOs(List<Course> courses);
    List<CourseSearchResultDTO> toCourseSearchResultDTOs(List<Course> courses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    void updateCourseFromDTO(CourseRequestDTO dto, @MappingTarget Course course);

    @Named("toProfessorDTO")
    default CourseDetailDTO.ProfessorDTO toProfessorDTO(Professor professor) {
        if (professor == null) {
            return null;
        }
        return new CourseDetailDTO.ProfessorDTO(professor.getId(), professor.getName(), professor.getDepartment());
    }
}
