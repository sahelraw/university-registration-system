package com.uni.demo.section;

import com.uni.demo.entites.Course;
import com.uni.demo.entites.Section;
import com.uni.demo.entites.Teacher;
import com.uni.demo.repositories.CourseRepository;
import com.uni.demo.repositories.SectionRepository;
import com.uni.demo.repositories.TeacherRepository;
import com.uni.demo.services.SectionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private SectionService sectionService;

    private Section section;
    private Course course;
    private Teacher teacher;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        course = new Course();
        course.setId(1);
        course.setName("Java");
        course.setHours(3);

        teacher = new Teacher();
        teacher.setId(1);
        teacher.setName("John");

        section = new Section();
        section.setId(1);
        section.setCourse(course);
        section.setTeacher(teacher);
        section.setDate(LocalDate.now());
        section.setTime(LocalTime.NOON);
    }

    // ================= CREATE =================

    @Test
    void addSection_success(){

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(sectionRepository.existsByTeacherIdAndDateAndTime(any(),any(),any()))
                .thenReturn(false);

        when(sectionRepository.save(any())).thenReturn(section);

        Section result = sectionService.addSection(section);

        assertNotNull(result);
        verify(sectionRepository).save(section);
    }

    @Test
    void addSection_courseNotFound(){
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sectionService.addSection(section));
    }

    @Test
    void addSection_teacherNotFound(){
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sectionService.addSection(section));
    }

    @Test
    void addSection_teacherAlreadyHasSection(){
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(sectionRepository.existsByTeacherIdAndDateAndTime(any(),any(),any()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> sectionService.addSection(section));
    }

    @Test
    void addSection_missingCourseId(){
        Section invalidSection = new Section();
        invalidSection.setTeacher(teacher);

        assertThrows(IllegalStateException.class,
                () -> sectionService.addSection(invalidSection));
    }

    @Test
    void addSection_missingTeacherId(){
        Section invalidSection = new Section();
        invalidSection.setCourse(course);

        assertThrows(IllegalStateException.class,
                () -> sectionService.addSection(invalidSection));
    }

    // ================= GET =================

    @Test
    void getSections_success(){
        List<Section> sections = new ArrayList<>();
        sections.add(section);

        when(sectionRepository.findAll()).thenReturn(sections);

        List<Section> result = sectionService.getSections();

        assertEquals(1, result.size());
        assertEquals(section, result.get(0));
    }

    @Test
    void getSections_empty(){
        when(sectionRepository.findAll()).thenReturn(new ArrayList<>());

        List<Section> result = sectionService.getSections();

        assertEquals(0, result.size());
    }

    @Test
    void getSectionById_success(){
        when(sectionRepository.findById(1)).thenReturn(Optional.of(section));

        Section result = sectionService.getSectionById(1);

        assertEquals(section, result);
    }

    @Test
    void getSectionById_notFound(){
        when(sectionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sectionService.getSectionById(1));
    }

    // ================= FULL UPDATE =================

    @Test
    void updateSectionFull_success(){
        Section existing = new Section();
        existing.setId(1);
        existing.setCourse(course);
        existing.setTeacher(teacher);
        existing.setDate(LocalDate.now());
        existing.setTime(LocalTime.NOON);

        Section incoming = new Section();
        incoming.setCourse(course);
        incoming.setTeacher(teacher);
        incoming.setDate(LocalDate.now().plusDays(1));
        incoming.setTime(LocalTime.of(10, 0));

        when(sectionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(sectionRepository.existsByTeacherIdAndDateAndTime(any(),any(),any()))
                .thenReturn(false);
        when(sectionRepository.save(any())).thenReturn(existing);

        Section result = sectionService.updateSectionFull(1, incoming);

        assertEquals(LocalDate.now().plusDays(1), result.getDate());
        verify(sectionRepository).save(existing);
    }

    @Test
    void updateSectionFull_missingDate(){
        Section incoming = new Section();
        incoming.setTime(LocalTime.NOON);
        incoming.setCourse(course);
        incoming.setTeacher(teacher);

        assertThrows(IllegalStateException.class,
                () -> sectionService.updateSectionFull(1, incoming));
    }

    @Test
    void updateSectionFull_missingTime(){
        Section incoming = new Section();
        incoming.setDate(LocalDate.now());
        incoming.setCourse(course);
        incoming.setTeacher(teacher);

        assertThrows(IllegalStateException.class,
                () -> sectionService.updateSectionFull(1, incoming));
    }

    @Test
    void updateSectionFull_notFound(){
        when(sectionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sectionService.updateSectionFull(1, new Section()));
    }

    // ================= PARTIAL UPDATE =================

    @Test
    void updateSectionPartial_updateDateOnly(){
        Section existing = new Section();
        existing.setId(1);
        existing.setCourse(course);
        existing.setTeacher(teacher);
        existing.setDate(LocalDate.now());
        existing.setTime(LocalTime.NOON);

        Section incoming = new Section();
        incoming.setDate(LocalDate.now().plusDays(1));

        when(sectionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(sectionRepository.save(any())).thenReturn(existing);

        Section result = sectionService.updateSectionPartial(1, incoming);

        assertEquals(LocalDate.now().plusDays(1), result.getDate());
        assertEquals(LocalTime.NOON, result.getTime());
        verify(sectionRepository).save(existing);
    }

    @Test
    void updateSectionPartial_updateTimeOnly(){
        Section existing = new Section();
        existing.setId(1);
        existing.setCourse(course);
        existing.setTeacher(teacher);
        existing.setDate(LocalDate.now());
        existing.setTime(LocalTime.NOON);

        Section incoming = new Section();
        incoming.setTime(LocalTime.of(10, 0));

        when(sectionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(sectionRepository.save(any())).thenReturn(existing);

        Section result = sectionService.updateSectionPartial(1, incoming);

        assertEquals(LocalDate.now(), result.getDate());
        assertEquals(LocalTime.of(10, 0), result.getTime());
        verify(sectionRepository).save(existing);
    }

    @Test
    void updateSectionPartial_updateCourseOnly(){
        Section existing = new Section();
        existing.setId(1);
        existing.setCourse(course);
        existing.setTeacher(teacher);
        existing.setDate(LocalDate.now());
        existing.setTime(LocalTime.NOON);

        Course newCourse = new Course();
        newCourse.setId(2);
        newCourse.setName("Spring");

        Section incoming = new Section();
        incoming.setCourse(newCourse);

        when(sectionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(2)).thenReturn(Optional.of(newCourse));
        when(sectionRepository.save(any())).thenReturn(existing);

        Section result = sectionService.updateSectionPartial(1, incoming);

        assertEquals(newCourse, result.getCourse());
        verify(sectionRepository).save(existing);
    }

    @Test
    void updateSectionPartial_updateTeacherOnly(){
        Section existing = new Section();
        existing.setId(1);
        existing.setCourse(course);
        existing.setTeacher(teacher);
        existing.setDate(LocalDate.now());
        existing.setTime(LocalTime.NOON);

        Teacher newTeacher = new Teacher();
        newTeacher.setId(2);
        newTeacher.setName("Jane");

        Section incoming = new Section();
        incoming.setTeacher(newTeacher);

        when(sectionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.findById(2)).thenReturn(Optional.of(newTeacher));
        when(sectionRepository.existsByTeacherIdAndDateAndTime(any(),any(),any()))
                .thenReturn(false);
        when(sectionRepository.save(any())).thenReturn(existing);

        Section result = sectionService.updateSectionPartial(1, incoming);

        assertEquals(newTeacher, result.getTeacher());
        verify(sectionRepository).save(existing);
    }

    @Test
    void updateSectionPartial_notFound(){
        when(sectionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> sectionService.updateSectionPartial(1, new Section()));
    }

    // ================= DELETE =================

    @Test
    void deleteSection_success(){

        when(sectionRepository.existsById(1)).thenReturn(true);

        sectionService.deleteSection(1);

        verify(sectionRepository).deleteById(1);
    }

    @Test
    void deleteSection_notFound(){
        when(sectionRepository.existsById(1)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> sectionService.deleteSection(1));
    }
}