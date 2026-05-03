package com.uni.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.uni.demo.entites.Student;
import com.uni.demo.services.StudentService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ================= GET ALL (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/studentAll")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    // ================= GET BY ID (STUDENT / ADMIN) =================
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN')")
    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable int studentId) {
        return studentService.getStudentById(studentId);
    }

    // ================= GET BY NAME (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/studentName")
    public List<Student> getStudentsByName(@RequestParam String name) {
        return studentService.getStudentsByName(name);
    }

    // ================= CREATE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/studentAdd")
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    // ================= DELETE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student " + studentId + " deleted successfully.");
    }

    // ================= FULL UPDATE (ADMIN / STUDENT) =================
    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @PutMapping("/{studentId}")
    public void updateStudentFull(
            @PathVariable int studentId,
            @RequestBody Student student) {

        studentService.updateStudentFull(studentId, student);
    }

    // ================= PARTIAL UPDATE (ADMIN / STUDENT) =================
    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT')")
    @PutMapping("/{studentId}/partial")
    public void updateStudentPartial(
            @PathVariable int studentId,
            @RequestBody Student incomingStudent) {

        studentService.updateStudentPartial(studentId, incomingStudent);
    }
}