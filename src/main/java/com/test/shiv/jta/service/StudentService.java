package com.test.shiv.jta.service;

import com.test.shiv.jta.entity.Course;
import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.entity.Student;
import com.test.shiv.jta.entity.StudentWithCoursesDTO;
import com.test.shiv.jta.repository.CourseRepository;
import com.test.shiv.jta.repository.EnrollmentRepository;
import com.test.shiv.jta.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    EnrollmentService enrollmentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<StudentWithCoursesDTO> getAllStudentsWithCourses() {
        return studentRepository.findAll().stream()
                .map(student -> {
                    StudentWithCoursesDTO dto = new StudentWithCoursesDTO();
                    dto.setStudentId(student.getStudentId());
                    dto.setFullName(student.getFullName());

                    List<String> courseNames = enrollmentRepository.findByStudent(student)
                            .stream()
                            .map(enrollment -> enrollment.getCourse().getCourseName())
                            .collect(Collectors.toList());

                    dto.setCourseNames(courseNames);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<Student> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student updatedStudent) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setFullName(updatedStudent.getFullName());
            student.setEmailAddress(updatedStudent.getEmailAddress());
            student.setTelephoneNumber(updatedStudent.getTelephoneNumber());
            student.setAddress(updatedStudent.getAddress());
            return studentRepository.save(student);
        } else {
            // Handle student not found
            return null;
        }
    }

    public void deleteStudent(Long studentId) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);
        if(existingStudent.isPresent()) {

            List<Enrollment> existingEnrollments = enrollmentRepository.findByStudent(existingStudent.get());
            enrollmentRepository.deleteAll(existingEnrollments);
            studentRepository.deleteById(studentId);
        } else {
            System.out.println("Student Not Present");
        }
    }

    public void assignStudentWithCourses(Long studentId, List<Long> courseIds) {
        Optional<Student> student = studentRepository.findById(studentId);
        List<Course> courses = courseRepository.findAllById(courseIds);

        if (student.isPresent() && !courses.isEmpty()) {
            for (Course course : courses)
                enrollmentService.allocateStudentToCourse(student.get(), course);
        }
    }

    public void updatedCoursesByStudentId(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Fetch the course entities from the database
        List<Course> courses = courseRepository.findAllById(courseIds);

        // Update the courses for the student
        updateStudentCourses(student, courses);
    }

    private void updateStudentCourses(Student student, List<Course> courses) {
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudent(student);

        // Remove existing enrollments not present in the new courses
        List<Long> removedCourseIds = new ArrayList<>();
        existingEnrollments.removeIf(enrollment -> {
            if(courses.contains(enrollment.getCourse())) {
                removedCourseIds.add(enrollment.getCourse().getCourseId());
                return true;
            }
            return false;
        });

        enrollmentRepository.deleteAll(existingEnrollments);

        courses.removeIf(course -> removedCourseIds.contains(course.getCourseId()));
        courses.forEach(course -> {
            Enrollment enrollment = new Enrollment(student, course);
            enrollmentRepository.save(enrollment);
        });
    }
}
