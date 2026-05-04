package com.uni.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.uni.demo.entites.Teacher;
import com.uni.demo.services.TeacherService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // ===== GET ALL (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/teacherAll")
    public List<Teacher> getTeachers() {
        return teacherService.getTeachers();
    }

    // ===== GET BY ID (TEACHER / ADMIN) =====
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @GetMapping("/{teacherId}")
    public Teacher getTeacherById(@PathVariable int teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    // ===== CREATE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacherAdd")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        teacherService.addNewTeacher(teacher);
        return ResponseEntity.ok("Teacher added successfully.");
    }

    // ===== DELETE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable int teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok("Teacher " + teacherId + " deleted successfully.");
    }

    // ===== FULL UPDATE (ADMIN / TEACHER) =====
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{teacherId}")
    public ResponseEntity<String> updateTeacherFull(@PathVariable int teacherId,
                             @RequestBody Teacher teacher) {
        teacherService.updateTeacherFull(teacherId, teacher);
        return ResponseEntity.ok("Teacher " + teacherId + " updated successfully.");
    }

    // ===== PARTIAL UPDATE (ADMIN / TEACHER) =====
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{teacherId}/partial")
    public ResponseEntity<String> updateTeacherPartial(@PathVariable int teacherId,
                                @RequestBody Teacher teacher) {
        teacherService.updateTeacherPartial(teacherId, teacher);
        return ResponseEntity.ok("Teacher " + teacherId + " partially updated successfully.");
    }
}