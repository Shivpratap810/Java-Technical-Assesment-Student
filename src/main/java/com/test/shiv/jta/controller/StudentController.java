package com.test.shiv.jta.controller;

import com.test.shiv.jta.entity.Student;
import com.test.shiv.jta.entity.StudentWithCoursesDTO;
import com.test.shiv.jta.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/studentsWithCourses")
    public List<StudentWithCoursesDTO> getAllStudentsWithCourses() {
        return studentService.getAllStudentsWithCourses();
    }

    @GetMapping("/students/{studentId}")
    public Student getStudentById(@PathVariable Long studentId) {
        return studentService.getStudentById(studentId);
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/students/{studentId}")
    public Student updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        return studentService.updateStudent(studentId, updatedStudent);
    }

    @DeleteMapping("/students/{studentId}")
    public void deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PostMapping("/studentsWithCourses/{studentId}")
    public void assignStudentWithCourses(@PathVariable Long studentId,
                                         @RequestBody List<Long> courseIds) {
        studentService.assignStudentWithCourses(studentId, courseIds);
    }

    @PutMapping("/students/updateCourses/{studentId}")
    public void updateCoursesForStudent(@PathVariable Long studentId,
                                           @RequestBody List<Long> courseIds) {
        studentService.updatedCoursesByStudentId(studentId, courseIds);
    }
}
