package com.uni.demo.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/section")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<Section> getSections() {
        return sectionService.getSections();
    }

    @GetMapping("/{id}")
    public Section getSection(@PathVariable Integer id) {
        return sectionService.getSectionById(id);
    }

    @PostMapping
    public Section addSection(@RequestBody Section section) {
        return sectionService.addSection(section);
    }

    @PutMapping("/{id}")
    public Section updateFull(@PathVariable Integer id,
                              @RequestBody Section section) {
        return sectionService.updateSectionFull(id, section);
    }

    @PutMapping("/{id}/partial")
    public Section updatePartial(@PathVariable Integer id,
                                 @RequestBody Section section) {
        return sectionService.updateSectionPartial(id, section);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable Integer id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok("Section deleted successfully");
    }
}
