package com.uni.demo.course;
import com.uni.demo.major.Major;
import com.uni.demo.major.MajorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final MajorRepository majorRepository;

    public CourseService(CourseRepository courseRepository,
                         MajorRepository majorRepository) {
        this.courseRepository = courseRepository;
        this.majorRepository = majorRepository;
    }

    // ===== GET ALL =====
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    // ===== GET BY ID =====
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // ===== ADD COURSE =====
     public Course addCourse(Course course, Integer majorId) {
        if (majorId == null) {
            throw new IllegalStateException("Major ID must be provided");
        }

        if (courseRepository.existsByName(course.getName())) {
            throw new IllegalStateException("Course name already exists");
        }

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new RuntimeException("Major not found"));

        course.setMajor(major);
        return courseRepository.save(course);
    }
// ===== FULL UPDATE =====
@Transactional
   public Course updateCourseFull(Integer courseId, Course newCourse, Integer majorId) {
    if (newCourse.getName() == null || newCourse.getName().isEmpty() || majorId == null) {
        throw new IllegalStateException("All fields including major ID required for full update");
    }

    Course existing = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    // Check if the new name already exists for a different course
    // Note: You need to add 'existsByName' to your CourseRepository interface
    if (!Objects.equals(existing.getName(), newCourse.getName())
            && courseRepository.existsByName(newCourse.getName())) {
        throw new IllegalStateException("Course name already exists");
    }

    Major major = majorRepository.findById(majorId)
            .orElseThrow(() -> new RuntimeException("Major not found"));

    // Update the fields
    existing.setName(newCourse.getName());
    existing.setHours(newCourse.getHours());
    existing.setMajor(major);

    // CRITICAL: Save the changes back to the database
    return courseRepository.save(existing);
}

    // ===== PARTIAL UPDATE =====
    @Transactional
    public Course updateCoursePartial(Integer courseId, Course incoming, Integer majorId) {
        Course existing = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Update name if provided
        if (incoming.getName() != null && !incoming.getName().isEmpty()) {
            if (!Objects.equals(existing.getName(), incoming.getName())
                    && courseRepository.existsByName(incoming.getName())) {
                throw new IllegalStateException("Course name already exists");
            }
            existing.setName(incoming.getName());
        }

        // Update hours if positive
        if (incoming.getHours() > 0) {
            existing.setHours(incoming.getHours());
        }

        // Update major if majorId provided
        if (majorId != null) {
            Major major = majorRepository.findById(majorId)
                    .orElseThrow(() -> new RuntimeException("Major not found"));
            existing.setMajor(major);
        }

        // CRITICAL: Save the updated entity back to the database
        return courseRepository.save(existing);
    }
    // ===== DELETE =====
    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
}
