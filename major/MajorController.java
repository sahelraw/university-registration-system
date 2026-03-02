package com.springtest1.springtest1.major;

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
    public void addMajor(@RequestBody Major major) {
        majorService.addMajor(major);
    }

    @DeleteMapping("/{majorId}")
    public ResponseEntity<String> deleteMajor(@PathVariable int majorId) {
        majorService.deleteMajor(majorId);
        return ResponseEntity.ok("Major " + majorId + " and all its courses were successfully deleted.");
    }

   @PutMapping("/{majorId}")
public void updateMajorFull(@PathVariable int majorId,
                           @RequestBody Major major) {

    majorService.updateMajorFull(majorId, major);
}
@PutMapping("/{majorId}/partial")
public void updateMajorPartial(@PathVariable int majorId,
                              @RequestBody Major major) {

    majorService.updateMajorPartial(majorId, major);
}
}
