package com.test.shiv.jta.repository;

import com.test.shiv.jta.entity.Enrollment;
import com.test.shiv.jta.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(Student student);

}
