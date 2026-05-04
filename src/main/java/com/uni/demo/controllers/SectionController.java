package com.uni.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.uni.demo.entites.Section;
import com.uni.demo.services.SectionService;

import java.util.List;

@RestController
@RequestMapping("api/v1/section")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // ===== GET ALL (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sectionAll")
    public List<Section> getSections() {
        return sectionService.getSections();
    }

    // ===== GET BY ID (STUDENT / TEACHER with logic later) =====
    @PreAuthorize("hasAnyRole('STUDENT','TEACHER')")
    @GetMapping("/{id}")
    public Section getSection(@PathVariable Integer id) {
        return sectionService.getSectionById(id);
    }

    // ===== CREATE (ADMIN / TEACHER) =====
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PostMapping
    public Section addSection(@RequestBody Section section) {
        return sectionService.addSection(section);
    }

    // ===== FULL UPDATE (ADMIN ONLY) =====
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Section updateFull(@PathVariable Integer id,
                              @RequestBody Section section) {
        return sectionService.updateSectionFull(id, section);
    }

    // ===== PARTIAL UPDATE (ADMIN / TEACHER) =====
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}/partial")
    public Section updatePartial(@PathVariable Integer id,
                                 @RequestBody Section section) {
        return sectionService.updateSectionPartial(id, section);
    }

    // ===== DELETE (ADMIN / TEACHER) =====
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Integer id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok("Section deleted successfully");
    }
}