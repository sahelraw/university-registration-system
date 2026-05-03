package com.uni.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.uni.demo.entites.Enrollment;
import com.uni.demo.services.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // ================= CREATE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<String> enrollStudent(@RequestBody Enrollment enrollment) {
        enrollmentService.enrollStudent(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student enrolled successfully");
    }

    // ================= GET (STUDENT / TEACHER / ADMIN) =================
    @PreAuthorize("hasAnyAuthority('STUDENT','TEACHER','ADMIN')")
    @GetMapping("/{studentId}")
    public List<Enrollment> getEnrollments(@PathVariable Integer studentId) {
        return enrollmentService.getStudentEnrollments(studentId);
    }

    // ================= DELETE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<String> deleteEnrollment(@PathVariable Integer enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Enrollment deleted successfully");
    }

    // ================= FULL UPDATE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{enrollmentId}")
    public ResponseEntity<String> updateEnrollment(
            @PathVariable Integer enrollmentId,
            @RequestBody Enrollment enrollment) {
        enrollmentService.updateEnrollment(enrollmentId, enrollment);
        return ResponseEntity.ok("Enrollment updated successfully");
    }

    // ================= PARTIAL UPDATE (ADMIN ONLY) =================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{enrollmentId}/partial")
    public ResponseEntity<String> partialUpdateEnrollment(
            @PathVariable Integer enrollmentId,
            @RequestBody Enrollment enrollment) {
        enrollmentService.partialUpdateEnrollment(enrollmentId, enrollment);
        return ResponseEntity.ok("Enrollment updated partially successfully");
    }
}