package com.example.student.repository;

import com.example.student.model.Course;
import com.example.student.model.Student;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class providing JPA Specifications for filtering Student entities.
 * These specifications can be used in both repository and service layers.
 *
 * 1. Repository Layer Usage:
 * @Repository
 * public interface StudentRepository extends JpaRepository<Student, Long>,
 *     JpaSpecificationExecutor<Student> {
 *     // Basic repository methods
 * }
 *
 * 2. Service Layer Usage (Simple):
 * @Service
 * public class StudentService {
 *     public Page<Student> findStudents(String major, Double minGpa, String courseCode, Pageable pageable) {
 *         Specification<Student> spec = Specification.where(null);
 *
 *         if (minGpa != null) {
 *             spec = spec.and(StudentSpecifications.hasMinGpa(minGpa));
 *         }
 *         if (major != null) {
 *             spec = spec.and(StudentSpecifications.inMajor(major));
 *         }
 *         return studentRepository.findAll(spec, pageable);
 *     }
 * }
 *
 * 3. Query Service Usage (Complex):
 * @Service
 * public class StudentQueryService {
 *     public List<Student> findAdvancedStudents(StudentSearchCriteria criteria) {
 *         return studentRepository.findAll(
 *             Specification.where(null)
 *                 .and(StudentSpecifications.hasMinGpa(3.5))
 *                 .and(StudentSpecifications.graduatesAfter(2024))
 *         );
 *     }
 * }
 *
 * Benefits:
 * - Reusable query conditions
 * - Type-safe queries
 * - Can combine multiple conditions dynamically
 * - Clean separation of query logic
 * - Flexible usage in different layers
 */
public class StudentSpecifications {
    public static Specification<Student> hasMinGpa(double minGpa) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("gpa"), minGpa);
    }

    public static Specification<Student> inMajor(String major) {
        return (root, query, cb) -> cb.equal(root.get("major"), major);
    }

    public static Specification<Student> graduatesAfter(int year) {
        return (root, query, cb) -> cb.greaterThan(root.get("graduationYear"), year);
    }

    public static Specification<Student> enrolledInCourse(String courseCode) {
        return (root, query, cb) -> {
            Join<Student, Course> courseJoin = root.join("courses");
            return cb.equal(courseJoin.get("code"), courseCode);
        };
    }
}