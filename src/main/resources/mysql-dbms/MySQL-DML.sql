-- Inserting dummy data into students table
INSERT INTO students (student_id, full_name, email_address, telephone_number, student_address)
VALUES
    (1, 'John Doe', 'john.doe@example.com', '+1234567890', 'Cityville'),
    (2, 'Jane Smith', 'jane.smith@example.com', '+9876543210', 'Townsville'),
    (3, 'Alice Johnson', 'alice.johnson@example.com', '+1122334455', 'Villagetown'),
    (4, 'Shivpratap Singh', 'shiv.chouhan@outlook.com', '+919413036321', 'Jaipur, Rajasthan');

-- Inserting dummy data into courses table
INSERT INTO courses (course_id, course_name, course_instructor, course_description)
VALUES
    (101, 'Introduction to Programming', 'Prof. Smith', 'Learn the basics of programming'),
    (102, 'Database Design', 'Prof. Johnson', 'Understanding database design principles'),
    (103, 'Web Development', 'Prof. Brown', 'Building dynamic websites');

-- Inserting dummy data into enrollments table
INSERT INTO enrollments (enrollment_id, student_id, course_id)
VALUES
    (1001, 1, 101),
    (1002, 1, 102),
    (1003, 2, 101),
    (1004, 3, 103),
    (1005, 4, 101),
    (1006, 4, 102),
    (1007, 4, 103);