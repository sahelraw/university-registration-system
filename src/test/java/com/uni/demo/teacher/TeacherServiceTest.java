package com.uni.demo.teacher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.uni.demo.entites.Teacher;
import com.uni.demo.repositories.TeacherRepository;
import com.uni.demo.services.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        teacher = new Teacher(
                "John",
                "john@mail.com",
                "079999999",
                "IT"
        );
    }

    // ================= CREATE =================

    @Test
    void addTeacher_success(){

        when(teacherRepository.existsByEmail(teacher.getEmail()))
                .thenReturn(false);

        teacherService.addNewTeacher(teacher);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void addTeacher_duplicate(){

        when(teacherRepository.existsByEmail(teacher.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> teacherService.addNewTeacher(teacher));
    }

    // ================= GET =================

    @Test
    void getTeachers_success(){
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getTeachers();

        assertEquals(1, result.size());
        assertEquals(teacher, result.get(0));
    }

    @Test
    void getTeachers_empty(){
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

        List<Teacher> result = teacherService.getTeachers();

        assertEquals(0, result.size());
    }

    @Test
    void getTeacherById_success(){
        teacher.setId(1);

        when(teacherRepository.findById(1))
                .thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1);

        assertEquals("John", result.getName());
    }

    @Test
    void getTeacherById_notFound(){
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> teacherService.getTeacherById(1));
    }

    // ================= FULL UPDATE =================

    @Test
    void updateTeacherFull_success(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher updated = new Teacher(
                "New",
                "new@mail.com",
                "079000000",
                "IT"
        );

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(teacherRepository.save(any())).thenReturn(existing);

        teacherService.updateTeacherFull(1, updated);

        assertEquals("New", existing.getName());
        assertEquals("new@mail.com", existing.getEmail());
        assertEquals("079000000", existing.getPhoneNumber());
        assertEquals("IT", existing.getMajorName());
        verify(teacherRepository).save(existing);
    }

    @Test
    void updateTeacherFull_missingName(){
        Teacher updated = new Teacher();
        updated.setEmail("new@mail.com");
        updated.setPhoneNumber("079000000");
        updated.setMajorName("IT");

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherFull(1, updated));
    }

    @Test
    void updateTeacherFull_missingEmail(){
        Teacher updated = new Teacher();
        updated.setName("New");
        updated.setPhoneNumber("079000000");
        updated.setMajorName("IT");

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherFull(1, updated));
    }

    @Test
    void updateTeacherFull_duplicateEmail(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher updated = new Teacher(
                "New",
                "taken@mail.com",
                "079000000",
                "IT"
        );

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.existsByEmail("taken@mail.com")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherFull(1, updated));
    }

    @Test
    void updateTeacherFull_notFound(){
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherFull(1, teacher));
    }

    // ================= PARTIAL UPDATE =================

    @Test
    void updateTeacherPartial_updateNameOnly(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher incoming = new Teacher();
        incoming.setName("NewName");

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(any())).thenReturn(existing);

        teacherService.updateTeacherPartial(1, incoming);

        assertEquals("NewName", existing.getName());
        assertEquals("old@mail.com", existing.getEmail());
        verify(teacherRepository).save(existing);
    }

    @Test
    void updateTeacherPartial_updateEmailOnly(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher incoming = new Teacher();
        incoming.setEmail("newemail@mail.com");

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.existsByEmail("newemail@mail.com")).thenReturn(false);
        when(teacherRepository.save(any())).thenReturn(existing);

        teacherService.updateTeacherPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("newemail@mail.com", existing.getEmail());
        verify(teacherRepository).save(existing);
    }

    @Test
    void updateTeacherPartial_updatePhoneOnly(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher incoming = new Teacher();
        incoming.setPhoneNumber("0799999999");

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(any())).thenReturn(existing);

        teacherService.updateTeacherPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("0799999999", existing.getPhoneNumber());
        verify(teacherRepository).save(existing);
    }

    @Test
    void updateTeacherPartial_updateMajorOnly(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher incoming = new Teacher();
        incoming.setMajorName("IT");

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(any())).thenReturn(existing);

        teacherService.updateTeacherPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("IT", existing.getMajorName());
        verify(teacherRepository).save(existing);
    }

    @Test
    void updateTeacherPartial_duplicateEmail(){
        Teacher existing = new Teacher(
                "Old",
                "old@mail.com",
                "078000000",
                "CS"
        );

        Teacher incoming = new Teacher();
        incoming.setEmail("taken@mail.com");

        when(teacherRepository.findById(1)).thenReturn(Optional.of(existing));
        when(teacherRepository.existsByEmail("taken@mail.com")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherPartial(1, incoming));
    }

    @Test
    void updateTeacherPartial_notFound(){
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> teacherService.updateTeacherPartial(1, teacher));
    }

    // ================= DELETE =================

    @Test
    void deleteTeacher_success(){

        when(teacherRepository.existsById(1)).thenReturn(true);

        teacherService.deleteTeacher(1);

        verify(teacherRepository).deleteById(1);
    }

    @Test
    void deleteTeacher_notFound(){
        when(teacherRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> teacherService.deleteTeacher(1));
    }
}