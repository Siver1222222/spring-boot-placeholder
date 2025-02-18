-- First, clean up existing data (if any) in reverse order of dependencies
DELETE FROM professor_advisee;
DELETE FROM course_enrollment;
DELETE FROM course;
DELETE FROM student;
DELETE FROM student_profile;
DELETE FROM professor;

-- Insert Professors (without explicitly specifying professor_id as it's auto-generated)
INSERT INTO professor (name, department) VALUES
                                             ('Dr. Jane Smith', 'Computer Science'),
                                             ('Dr. John Davis', 'Mathematics'),
                                             ('Dr. Sarah Wilson', 'Physics'),
                                             ('Dr. Michael Brown', 'Computer Science'),
                                             ('Dr. Emily Johnson', 'Mathematics');

-- Insert Student Profiles (without explicitly specifying id)
INSERT INTO student_profile (email, phone_number, date_of_birth) VALUES
                                                                     ('alice@university.edu', '123-456-7890', '2000-01-15'),
                                                                     ('bob@university.edu', '123-456-7891', '2001-03-20'),
                                                                     ('charlie@university.edu', '123-456-7892', '2000-07-10'),
                                                                     ('diana@university.edu', '123-456-7893', '2001-11-30'),
                                                                     ('edward@university.edu', '123-456-7894', '2000-05-25');

-- For the following inserts, we'll need to use the generated IDs
-- You might want to consider using a database-specific way to get the generated IDs
-- For PostgreSQL, you could use currval('professor_seq') or similar

-- Below is a placeholder showing the structure. You'll need to adjust the IDs based on your sequence:
INSERT INTO student (name, major, gpa, profile_id) VALUES
                                                       ('Alice Johnson', 'Computer Science', 3.8, 1),
                                                       ('Bob Smith', 'Mathematics', 3.9, 2),
                                                       ('Charlie Brown', 'Physics', 3.7, 3),
                                                       ('Diana Wilson', 'Computer Science', 4.0, 4),
                                                       ('Edward Davis', 'Mathematics', 3.6, 5);

INSERT INTO course (course_code, title, department, credits, professor_id) VALUES
                                                                               ('CS101', 'Introduction to Programming', 'Computer Science', 3, 1),
                                                                               ('MATH201', 'Advanced Calculus', 'Mathematics', 4, 2),
                                                                               ('PHYS301', 'Quantum Mechanics', 'Physics', 4, 3),
                                                                               ('CS202', 'Data Structures', 'Computer Science', 3, 4),
                                                                               ('MATH301', 'Linear Algebra', 'Mathematics', 3, 5),
                                                                               ('CS301', 'Database Systems', 'Computer Science', 3, 1),
                                                                               ('MATH401', 'Number Theory', 'Mathematics', 3, 2),
                                                                               ('PHYS401', 'Theoretical Physics', 'Physics', 4, 3);

INSERT INTO course_enrollment (course_id, student_id) VALUES
                                                          (1, 1), -- Alice takes CS101
                                                          (1, 2), -- Bob takes CS101
                                                          (2, 2), -- Bob takes MATH201
                                                          (2, 5), -- Edward takes MATH201
                                                          (3, 3), -- Charlie takes PHYS301
                                                          (4, 1), -- Alice takes CS202
                                                          (4, 4), -- Diana takes CS202
                                                          (5, 2), -- Bob takes MATH301
                                                          (5, 5), -- Edward takes MATH301
                                                          (6, 1), -- Alice takes CS301
                                                          (6, 4), -- Diana takes CS301
                                                          (7, 2), -- Bob takes MATH401
                                                          (8, 3); -- Charlie takes PHYS401

INSERT INTO professor_advisee (professor_id, student_id) VALUES
                                                             (1, 1), -- Dr. Smith advises Alice
                                                             (2, 2), -- Dr. Davis advises Bob
                                                             (3, 3), -- Dr. Wilson advises Charlie
                                                             (4, 4), -- Dr. Brown advises Diana
                                                             (5, 5); -- Dr. Johnson advises Edward