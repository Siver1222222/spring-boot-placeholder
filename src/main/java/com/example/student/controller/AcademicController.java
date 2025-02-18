package com.example.student.controller;

import com.example.student.dto.*;
import com.example.student.service.AcademicService;
import com.example.student.service.AcademicQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic")
@Tag(name = "Academic Controller", description = "APIs for managing academic operations")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;
    private final AcademicQueryService queryService;

    // Non-paged endpoints
    /**
     * Get all basic course information (non-paged)
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/courses/basic
     *
     * Response: List<CourseBasicDTO> containing:
     * {
     *   "id": 1,
     *   "courseCode": "CS101",
     *   "title": "Introduction to Programming"
     * }
     */
    @GetMapping("/courses/basic")
    @Operation(summary = "Get all courses (non-paged)", description = "Retrieves a simple list of all courses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses",
                    content = @Content(schema = @Schema(implementation = CourseBasicDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<CourseBasicDTO>> getAllCoursesBasic() {
        return ResponseEntity.ok(academicService.getAllCoursesBasic());
    }

    /**
     * Get department courses as basic list
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/courses/department/CS/list
     * Path variable:
     * - department: department code (e.g., "CS", "MATH")
     *
     * Response: List<CourseBasicDTO>
     */
    @GetMapping("/courses/department/{department}/list")
    @Operation(summary = "Get courses by department (non-paged)",
            description = "Retrieves all courses for a specific department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CourseBasicDTO>> getCoursesByDepartmentList(
            @Parameter(description = "Department name", required = true)
            @PathVariable String department) {
        return ResponseEntity.ok(academicService.getCoursesByDepartment(department));
    }

    /**
     * Get top 10 popular courses with detailed search results
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/courses/popular/top
     *
     * Response: List<CourseSearchResultDTO> containing:
     * {
     *   "id": 1,
     *   "code": "CS101",
     *   "title": "Introduction to Programming",
     *   "professorName": "John Doe",
     *   "enrollmentCount": 150,
     *   "averageGrade": 3.8
     * }
     */
    @GetMapping("/courses/popular/top")
    @Operation(summary = "Get top popular courses",
            description = "Retrieves top 10 courses by enrollment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CourseSearchResultDTO>> getTopPopularCourses() {
        return ResponseEntity.ok(queryService.findTopPopularCourses(10));
    }

    // Paged endpoints

    /**
     * GET /courses
     * Supports pagination with following query parameters:
     * - page: Zero-based page number (e.g. page=1 for second page)
     * - size: Number of elements per page (default: 20)
     * - sort: Sort field and direction (default: courseCode,asc)
     *
     * Example calls:
     * /courses?page=0               // First page
     * /courses?page=1&size=50      // Second page, 50 items
     * /courses?sort=courseName,desc // Sort by name descending
     */
    @GetMapping("/courses")
    @Operation(summary = "Get all courses (paged)",
            description = "Retrieves a paginated list of all available courses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<CourseBasicDTO>> getAllCourses(
            @PageableDefault(size = 20, sort = "courseCode") Pageable pageable) {
        return ResponseEntity.ok(academicService.getAllCoursesPageable(pageable));
    }

    /**
     * Search courses with advanced criteria and pagination
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/courses/search
     * Query params (CourseSearchCriteriaDTO):
     * - courseCode
     * - title
     * - department
     * - professorName
     * - minCredits (min: 0)
     * - minEnrollment (min: 0)
     * - hasAvailableSeats
     * - minAverageGrade
     * - isActive
     * - sortBy (default: "courseCode")
     * - sortDirection (default: "ASC")
     *
     * Pagination:
     * - page (default: 0)
     * - size (default: 20)
     *
     * Example: /courses/search?courseCode=CS101&minCredits=3&department=CS
     *
     * Response: PageResponseDTO<CourseSearchResultDTO> containing:
     * {
     *   "content": [{course data}],
     *   "pageNumber": 0,
     *   "pageSize": 20,
     *   "totalElements": 100,
     *   "totalPages": 5,
     *   "last": false
     * }
     */
    @GetMapping("/courses/search")
    @Operation(summary = "Search courses with criteria (paged)",
            description = "Search courses using multiple criteria with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponseDTO<CourseSearchResultDTO>> searchCourses(
            @Valid CourseSearchCriteriaDTO criteria,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<CourseSearchResultDTO> page = queryService.searchCourses(criteria, pageable);
        return ResponseEntity.ok(PageResponseDTO.of(page));
    }

    // Operation endpoints

    /**
     * Assign professor to course
     *
     * Postman:
     * POST http://localhost:8080/api/v1/academic/courses/{courseId}/professor/{professorId}
     * Path variables:
     * - courseId: ID of the course
     * - professorId: ID of the professor to assign
     *
     * Response: No content
     * Success: 200 OK
     * Error: 404 if course or professor not found
     */
    @PostMapping("/courses/{courseId}/professor/{professorId}")
    @Operation(summary = "Assign professor to course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully assigned professor"),
            @ApiResponse(responseCode = "404", description = "Professor or course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> assignProfessorToCourse(
            @PathVariable Long professorId,
            @PathVariable Long courseId) {
        academicService.assignProfessorToCourse(professorId, courseId);
        return ResponseEntity.ok().build();
    }

    /**
     * Enroll student in course
     *
     * Postman:
     * POST http://localhost:8080/api/v1/academic/courses/{courseId}/enroll/{studentId}
     * Path variables:
     * - courseId: ID of the course
     * - studentId: ID of the student to enroll
     *
     * Response: No content
     * Success: 201 Created
     * Error: 404 if student or course not found
     *        409 if student already enrolled
     */
    @PostMapping("/courses/{courseId}/enroll/{studentId}")
    @Operation(summary = "Enroll student in course")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully enrolled student"),
            @ApiResponse(responseCode = "404", description = "Student or course not found"),
            @ApiResponse(responseCode = "409", description = "Student already enrolled"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> enrollStudentInCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        academicService.enrollStudentInCourse(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Student endpoints (mixed paging)
    /**
     * Get top performing students by GPA
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/students/top
     * Query params:
     * - limit: number of top students to return (default: 10)
     *
     * Response: List<StudentSearchResultDTO> containing:
     * {
     *   "id": 1,
     *   "name": "John Smith",
     *   "major": "Computer Science",
     *   "gpa": 3.8,
     *   "email": "john@university.edu",
     *   "enrolledCoursesCount": 4
     * }
     */
    @GetMapping("/students/top")
    @Operation(summary = "Get top performing students",
            description = "Retrieves top students by GPA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved students",
                    content = @Content(schema = @Schema(implementation = StudentSearchResultDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StudentSearchResultDTO>> getTopStudents(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.findTopStudentsByGpa(limit));
    }

    /**
     * Search students with filters
     *
     * Postman:
     * GET http://localhost:8080/api/v1/academic/students/search
     * Query params:
     * - major (optional)
     * - minGpa (optional)
     * - courseEnrolled (optional)
     * - page (default: 0)
     * - size (default: 20)
     *
     * Response: Page<StudentSearchResultDTO> containing:
     * {
     *   "id": 1,
     *   "name": "John Smith",
     *   "major": "Computer Science",
     *   "gpa": 3.8,
     *   "email": "john@university.edu",
     *   "enrolledCoursesCount": 4
     * }
     */
    @GetMapping("/students/search")
    @Operation(summary = "Search students with criteria (paged)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved students",
                    content = @Content(schema = @Schema(implementation = StudentSearchResultDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<StudentSearchResultDTO>> searchStudents(
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Double minGpa,
            @RequestParam(required = false) String courseEnrolled,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(queryService.searchStudents(
                major, minGpa, courseEnrolled, null, pageable));
    }

    // Course management endpoints
    /**
     * Create new course
     *
     * Postman:
     * POST http://localhost:8080/api/v1/academic/courses
     * Headers: Content-Type: application/json
     *
     * Request Body (CourseRequestDTO):
     * {
     *   "code": "CS101",        // required, 2-10 chars
     *   "title": "Intro to Programming", // required, 3-100 chars
     *   "description": "Learn basics...", // max 500 chars
     *   "department": "CS",     // required
     *   "credits": 3,          // required, 1-6
     *   "professorId": 123     // optional
     * }
     *
     * Response: CourseDetailDTO containing:
     * {
     *   "id": 1,
     *   "courseCode": "CS101",
     *   "title": "Intro to Programming",
     *   "professor": {
     *     "id": 123,
     *     "name": "John Doe",
     *     "department": "CS"
     *   },
     *   "enrollmentCount": 0,
     *   "department": "CS",
     *   "credits": 3
     * }
     */
    @PostMapping("/courses")
    @Operation(summary = "Create new course")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CourseDetailDTO> createCourse(
            @Valid @RequestBody CourseRequestDTO courseRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(academicService.createCourse(courseRequest));
    }

    /**
     * Update existing course
     *
     * Postman:
     * PUT http://localhost:8080/api/v1/academic/courses/{courseId}
     * Path variable:
     * - courseId: ID of the course to update
     *
     * Headers: Content-Type: application/json
     *
     * Request Body (CourseRequestDTO):
     * {
     *   "code": "CS101",        // required, 2-10 chars
     *   "title": "Intro to Programming", // required, 3-100 chars
     *   "description": "Learn basics...", // max 500 chars
     *   "department": "CS",     // required
     *   "credits": 3,          // required, 1-6
     *   "professorId": 123     // optional
     * }
     *
     * Response: CourseDetailDTO containing updated course information
     */
    @PutMapping("/courses/{courseId}")
    @Operation(summary = "Update course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CourseDetailDTO> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequestDTO courseRequest) {
        return ResponseEntity.ok(academicService.updateCourse(courseId, courseRequest));
    }

    /**
     * Delete course by ID
     *
     * Postman:
     * DELETE http://localhost:8080/api/v1/academic/courses/{courseId}
     * Path variable:
     * - courseId: ID of the course to delete
     *
     * Response: No content
     * Success: 204 No Content
     * Error: 404 if course not found
     */
    @DeleteMapping("/courses/{courseId}")
    @Operation(summary = "Delete course")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        academicService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}