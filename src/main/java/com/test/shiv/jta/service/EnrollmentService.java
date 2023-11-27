package com.test.shiv.jta.service;

import com.test.shiv.jta.entity.Course;
import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.entity.Student;
import com.test.shiv.jta.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment allocateStudentToCourse(Student student, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }
}
