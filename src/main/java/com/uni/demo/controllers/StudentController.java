package com.uni.demo.controllers;

import org.springframework.http.ResponseEntity;
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

    // ================= GET =================

    @GetMapping("/studentAll")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable int studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping("/studentName") //here we use /studentName endpoint to avoid conflict with getStudentById which also uses @GetMapping("/{studentId}")
    public List<Student> getStudentsByName(@RequestParam String name) {
        return studentService.getStudentsByName(name);
    }

    @PostMapping("/studentAdd")
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }


    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student " + studentId + " deleted successfully.");
    }


    @PutMapping("/{studentId}")
    public void updateStudentFull(
            @PathVariable int studentId,
            @RequestBody Student student) {

        studentService.updateStudentFull(studentId, student);
    }


    @PutMapping("/{studentId}/partial")
public void updateStudentPartial(
        @PathVariable int studentId,
        @RequestBody Student incomingStudent) { // Captures all fields automatically

    studentService.updateStudentPartial(studentId, incomingStudent);
}
    }
