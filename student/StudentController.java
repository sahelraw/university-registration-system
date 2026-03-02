package com.springtest1.springtest1.student;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ================= GET =================

    // Get All Students
    @GetMapping("/studentAll")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    // Get Student By ID
    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable int studentId) {
        return studentService.getStudentById(studentId);
    }

    // Get Students By Name
    @GetMapping("/studentName") //here we use /studentName endpoint to avoid conflict with getStudentById which also uses @GetMapping("/{studentId}")
    public List<Student> getStudentsByName(@RequestParam String name) {
        return studentService.getStudentsByName(name);
    }

    // ================= POST =================

    // Add New Student
    @PostMapping("/studentAdd")
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    // ================= DELETE =================

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
    }

    // ================= PUT FULL UPDATE =================
    // Update ALL fields except ID

    @PutMapping("/{studentId}")
    public void updateStudentFull(
            @PathVariable int studentId,
            @RequestBody Student student) {

        studentService.updateStudentFull(studentId, student);
    }

    // ================= PUT PARTIAL UPDATE =================
    // Update specific fields only

    @PutMapping("/{studentId}/partial")
public void updateStudentPartial(
        @PathVariable int studentId,
        @RequestBody Student incomingStudent) { // Captures all fields automatically

    studentService.updateStudentPartial(studentId, incomingStudent);
}
    }
