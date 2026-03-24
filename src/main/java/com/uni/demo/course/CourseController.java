package com.uni.demo.course;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ===== GET ALL =====
    @GetMapping("/all")
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    // ===== GET BY ID =====
    @GetMapping("/{courseId}")
    public Course getCourse(@PathVariable Integer courseId) {
        return courseService.getCourseById(courseId);
    }

    // ===== POST (Add course) =====
    @PostMapping
    public Course addCourse(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        Integer hours = (Integer) payload.get("hours");
        Integer majorId = (Integer) payload.get("majorId");

        Course course = new Course();
        course.setName(name);
        course.setHours(hours != null ? hours : 0);

        return courseService.addCourse(course, majorId);
    }

    // ===== FULL PUT =====
    @PutMapping("/{courseId}")
    public Course updateCourseFull(@PathVariable Integer courseId,
                                   @RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        Integer hours = (Integer) payload.get("hours");
        Integer majorId = (Integer) payload.get("majorId");

        Course course = new Course();
        course.setName(name);
        course.setHours(hours != null ? hours : 0);

        return courseService.updateCourseFull(courseId, course, majorId);
    }

    // ===== PARTIAL PUT =====
    @PutMapping("/{courseId}/partial")
    public Course updateCoursePartial(@PathVariable Integer courseId,
                                      @RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        Integer hours = (Integer) payload.get("hours");
        Integer majorId = (Integer) payload.get("majorId");

        Course course = new Course();
        course.setName(name);
        if (hours != null) course.setHours(hours);

        return courseService.updateCoursePartial(courseId, course, majorId);
    }

    // ===== DELETE =====
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course " + courseId + " deleted successfully.");
    }
}
