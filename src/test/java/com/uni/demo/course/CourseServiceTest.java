package com.uni.demo.course;

import com.uni.demo.entites.Course;
import com.uni.demo.entites.Major;
import com.uni.demo.repositories.CourseRepository;
import com.uni.demo.repositories.MajorRepository;
import com.uni.demo.services.CourseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Major major;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        major = new Major("IT", "Information Technology");

        course = new Course();
        course.setName("Java");
        course.setHours(3);
    }

    // ================= CREATE =================

    @Test
    void addCourse_success() {
        when(courseRepository.existsByName("Java")).thenReturn(false);
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));
        when(courseRepository.save(any())).thenReturn(course);

        Course saved = courseService.addCourse(course, 1);

        assertEquals("Java", saved.getName());
        verify(courseRepository).save(course);
    }

    @Test
    void getCourses_success() {
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getCourses();

        assertEquals(1, result.size());
        assertEquals(course, result.get(0));
    }

    @Test
    void getCourses_empty() {
        when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        List<Course> result = courseService.getCourses();

        assertEquals(0, result.size());
    }

    @Test
    void addCourse_duplicateName() {
        when(courseRepository.existsByName("Java")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> courseService.addCourse(course, 1));
    }

    @Test
    void addCourse_missingMajorId() {
        assertThrows(IllegalStateException.class,
                () -> courseService.addCourse(course, null));
    }

    @Test
    void addCourse_majorNotFound() {
        when(courseRepository.existsByName("Java")).thenReturn(false);
        when(majorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.addCourse(course, 1));
    }

    // ================= GET =================

    @Test
    void getCourseById_success() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1);

        assertEquals("Java", result.getName());
    }

    @Test
    void getCourseById_notFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.getCourseById(1));
    }

    // ================= FULL UPDATE =================

    @Test
    void updateCourseFull_success() {

        Course existing = new Course();
        existing.setName("Old");
        existing.setHours(2);

        Course newCourse = new Course();
        newCourse.setName("New");
        newCourse.setHours(4);

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.existsByName("New")).thenReturn(false);
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));
        when(courseRepository.save(any())).thenReturn(existing);

        Course result = courseService.updateCourseFull(1, newCourse, 1);

        assertEquals("New", result.getName());
        assertEquals(4, result.getHours());
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCourseFull_missingName() {
        Course newCourse = new Course();
        newCourse.setHours(4);

        assertThrows(IllegalStateException.class,
                () -> courseService.updateCourseFull(1, newCourse, 1));
    }

    @Test
    void updateCourseFull_missingMajorId() {
        Course newCourse = new Course();
        newCourse.setName("New");
        newCourse.setHours(4);

        assertThrows(IllegalStateException.class,
                () -> courseService.updateCourseFull(1, newCourse, null));
    }

    @Test
    void updateCourseFull_duplicateName() {
        Course existing = new Course();
        existing.setName("Old");

        Course newCourse = new Course();
        newCourse.setName("Taken");
        newCourse.setHours(4);

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.existsByName("Taken")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> courseService.updateCourseFull(1, newCourse, 1));
    }

    @Test
    void updateCourseFull_courseNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.updateCourseFull(1, new Course(), 1));
    }

    // ================= PARTIAL UPDATE =================

    @Test
    void updateCoursePartial_updateNameOnly() {

        Course existing = new Course();
        existing.setName("Java");
        existing.setHours(3);

        Course incoming = new Course();
        incoming.setName("Spring");

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.existsByName("Spring")).thenReturn(false);
        when(courseRepository.save(any())).thenReturn(existing);

        Course result = courseService.updateCoursePartial(1, incoming, null);

        assertEquals("Spring", result.getName());
        assertEquals(3, result.getHours());
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCoursePartial_updateHoursOnly() {
        Course existing = new Course();
        existing.setName("Java");
        existing.setHours(3);

        Course incoming = new Course();
        incoming.setHours(4);

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any())).thenReturn(existing);

        Course result = courseService.updateCoursePartial(1, incoming, null);

        assertEquals("Java", result.getName());
        assertEquals(4, result.getHours());
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCoursePartial_updateMajorOnly() {
        Course existing = new Course();
        existing.setName("Java");
        existing.setHours(3);
        existing.setMajor(major);

        Major newMajor = new Major("CS", "Computer Science");

        Course incoming = new Course();

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.findById(1)).thenReturn(Optional.of(newMajor));
        when(courseRepository.save(any())).thenReturn(existing);

        Course result = courseService.updateCoursePartial(1, incoming, 1);

        assertEquals("Java", result.getName());
        assertEquals(newMajor, result.getMajor());
        verify(courseRepository).save(existing);
    }

    @Test
    void updateCoursePartial_duplicateName() {
        Course existing = new Course();
        existing.setName("Java");

        Course incoming = new Course();
        incoming.setName("Taken");

        when(courseRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.existsByName("Taken")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> courseService.updateCoursePartial(1, incoming, null));
    }

    @Test
    void updateCoursePartial_courseNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.updateCoursePartial(1, new Course(), null));
    }

    // ================= DELETE =================

    @Test
    void deleteCourse_success() {

        when(courseRepository.existsById(1)).thenReturn(true);

        courseService.deleteCourse(1);

        verify(courseRepository).deleteById(1);
    }

    @Test
    void deleteCourse_notFound() {
        when(courseRepository.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> courseService.deleteCourse(1));
    }
}