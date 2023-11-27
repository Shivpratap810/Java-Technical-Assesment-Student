package com.test.shiv.jta.service;

import com.test.shiv.jta.entity.Course;
import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.entity.Student;
import com.test.shiv.jta.entity.StudentWithCoursesDTO;
import com.test.shiv.jta.exception.BadRequestException;
import com.test.shiv.jta.exception.ResourceNotFoundException;
import com.test.shiv.jta.repository.CourseRepository;
import com.test.shiv.jta.repository.EnrollmentRepository;
import com.test.shiv.jta.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + studentId));
    }

    public Student addStudent(Student student) {
        if (student == null || student.getFullName().isEmpty() || student.getTelephoneNumber().isEmpty() || student.getEmailAddress().isEmpty()) {
            throw new BadRequestException("Invalid request");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + studentId));
        if (updatedStudent == null || updatedStudent.getFullName().isEmpty() || updatedStudent.getTelephoneNumber().isEmpty() || updatedStudent.getEmailAddress().isEmpty()) {
            throw new BadRequestException("Invalid request");
        } else {
            existingStudent.setFullName(updatedStudent.getFullName());
            existingStudent.setEmailAddress(updatedStudent.getEmailAddress());
            existingStudent.setTelephoneNumber(updatedStudent.getTelephoneNumber());
            existingStudent.setAddress(updatedStudent.getAddress());
            return studentRepository.save(existingStudent);
        }
    }

    public void deleteStudent(Long studentId) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + studentId));
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudent(existingStudent);
        enrollmentRepository.deleteAll(existingEnrollments);
        studentRepository.deleteById(studentId);
    }

    public void assignStudentWithCourses(Long studentId, List<Long> courseIds) {
        Student existingStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found with id: " + studentId));

        List<Course> courses = courseRepository.findAllById(courseIds);

        if (!courses.isEmpty()) {
            for (Course course : courses)
                enrollmentService.allocateStudentToCourse(existingStudent, course);
        }
    }

    public void updatedCoursesByStudentId(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        List<Course> courses = courseRepository.findAllById(courseIds);
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
