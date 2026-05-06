package com.uni.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.uni.demo.entites.Major;
import com.uni.demo.services.MajorService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/major")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    // ===== GET ALL (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/majorAll")
    public List<Major> getMajors() {
        return majorService.getMajors();
    }

    // ===== GET BY ID (ALL ROLES) =====
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER','ADMIN')")
    @GetMapping("/{majorId}")
    public Major getMajorById(@PathVariable int majorId) {
        return majorService.getMajorById(majorId);
    }

    // ===== CREATE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/majorAdd")
    public ResponseEntity<String> addMajor(@RequestBody Major major) {
        majorService.addMajor(major);
        return ResponseEntity.ok("Major added successfully.");
    }

    // ===== DELETE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{majorId}")
    public ResponseEntity<String> deleteMajor(@PathVariable int majorId) {
        majorService.deleteMajor(majorId);
        return ResponseEntity.ok("Major " + majorId + " and all associated students and courses deleted successfully.");
    }

    // ===== FULL UPDATE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{majorId}")
    public ResponseEntity<String> updateMajorFull(@PathVariable int majorId,
                           @RequestBody Major major) {
        majorService.updateMajorFull(majorId, major);
        return ResponseEntity.ok("Major updated successfully.");
    }

    // ===== PARTIAL UPDATE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{majorId}/partial")
    public ResponseEntity<String> updateMajorPartial(@PathVariable int majorId,
                              @RequestBody Major major) {
        majorService.updateMajorPartial(majorId, major);
        return ResponseEntity.ok("Major partially updated successfully.");
    }
}