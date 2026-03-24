package com.uni.demo.major;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/major")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping("/majorAll")
    public List<Major> getMajors() {
        return majorService.getMajors();
    }
@GetMapping("/{majorId}")
public Major getMajorById(@PathVariable int majorId) {
    return majorService.getMajorById(majorId);
}

    @PostMapping("/majorAdd")
    public ResponseEntity<String> addMajor(@RequestBody Major major) {
        majorService.addMajor(major);
        return ResponseEntity.ok("Major added successfully.");
    }

    @DeleteMapping("/{majorId}")
    public ResponseEntity<String> deleteMajor(@PathVariable int majorId) {
        majorService.deleteMajor(majorId);
        return ResponseEntity.ok("Major " + majorId + " and all its courses were successfully deleted.");
    }

   @PutMapping("/{majorId}")
    public ResponseEntity<String> updateMajorFull(@PathVariable int majorId,
                           @RequestBody Major major) {
        majorService.updateMajorFull(majorId, major);
        return ResponseEntity.ok("Major " + majorId + " updated successfully.");
    }

    @PutMapping("/{majorId}/partial")
    public ResponseEntity<String> updateMajorPartial(@PathVariable int majorId,
                              @RequestBody Major major) {
        majorService.updateMajorPartial(majorId, major);
        return ResponseEntity.ok("Major " + majorId + " partially updated successfully.");
    }
}
