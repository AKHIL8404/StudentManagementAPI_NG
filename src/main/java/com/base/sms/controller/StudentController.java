package com.base.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.base.sms.entity.Student;
import com.base.sms.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular app to call API
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // Get a single student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        if (studentService.existsById(student.getId())) {
            return ResponseEntity.badRequest().body("Student ID already exists.");
        }

        if (studentService.existsByEmail(student.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        if (studentService.existsByPhone(student.getPhone())) {
            return ResponseEntity.badRequest().body("Phone number already exists.");
        }

        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }

    // Update a student
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        Student existingStudent = studentService.getStudentById(id);
        if (existingStudent == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingStudent.getEmail().equals(student.getEmail()) &&
            studentService.existsByEmail(student.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        if (!existingStudent.getPhone().equals(student.getPhone()) &&
            studentService.existsByPhone(student.getPhone())) {
            return ResponseEntity.badRequest().body("Phone number already exists.");
        }

        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());

        Student updatedStudent = studentService.updateStudent(existingStudent);
        return ResponseEntity.ok(updatedStudent);
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}