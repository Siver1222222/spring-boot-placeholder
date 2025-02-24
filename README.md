# Student Management System

A comprehensive Spring Boot application for managing university students, courses, and professors, featuring a REST API with OpenAPI documentation.

## Features

- Student enrollment and management
- Course creation and management
- Professor assignments and advising
- RESTful API with comprehensive documentation
- PostgreSQL database integration
- DTO pattern implementation

## Tech Stack

- Java 21
- Spring Boot 3.2.2
- PostgreSQL
- OpenAPI (Swagger) for documentation
- MapStruct for object mapping
- Lombok for reducing boilerplate code

## Prerequisites

Before you begin, ensure you have:
- JDK 21
- Maven
- PostgreSQL
- Your favorite IDE (preferably IntelliJ IDEA)

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/student-management.git
cd student-management
```

2. Create PostgreSQL database:
```sql
CREATE DATABASE uni;
```

3. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/uni
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Documentation

Access the Swagger UI to explore and test the API:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Project Structure

```
src/main/java/com/example/student/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── model/          # Entity classes
├── repository/     # Data access layer
├── service/        # Business logic
├── dto/            # Data Transfer Objects
├── mapper/         # Object mappers
└── exception/      # Exception handling
```

## Key Features Explained

### Student Management
- Create and update student profiles
- Track academic progress
- Manage course enrollments

### Course Management
- Create and modify courses
- Track enrollments
- Assign professors

### Professor Management
- Manage professor information
- Assign courses
- Track student advisees

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Running Tests

```bash
mvn test
```

## Database Schema

The application uses the following core entities:
- Student
- Course
- Professor
- StudentProfile

Relationships include:
- One-to-One between Student and StudentProfile
- Many-to-Many between Student and Course
- Many-to-One between Course and Professor
- Many-to-Many between Student and Professor (advisors)

## API Examples

### Create a New Student
```bash
POST /api/v1/academic/students
{
    "name": "John Doe",
    "major": "Computer Science",
    "profile": {
        "email": "john@university.edu",
        "phoneNumber": "123-456-7890"
    }
}
```

### Enroll in a Course
```bash
POST /api/v1/academic/courses/{courseId}/enroll/{studentId}
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Contact

Project Link: [https://github.com/peyman-t/student-management](https://github.com/peyman-t/student-management)

## Acknowledgments

- Spring Boot Team
- PostgreSQL Team
- OpenAPI Initiative
