package com.uni.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.uni.demo.entites.Student;
import com.uni.demo.repositories.StudentRepository;
import com.uni.demo.services.StudentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        student = new Student(
                "Sahel",
                "sahel@mail.com",
                "0790000000",
                LocalDate.of(2000,1,1)
        );
    }

    // ================= CREATE =================

    @Test
    void addStudent_success(){

        when(studentRepository.existsByEmail(student.getEmail()))
                .thenReturn(false);

        studentService.addNewStudent(student);

        verify(studentRepository).save(student);
    }

    @Test
    void addStudent_duplicateEmail(){

        when(studentRepository.existsByEmail(student.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> studentService.addNewStudent(student));
    }

    @Test
    void addStudent_ageBelow18(){
        Student youngStudent = new Student(
                "Young",
                "young@mail.com",
                "0790000001",
                LocalDate.of(2010,1,1)
        );

        when(studentRepository.existsByEmail(youngStudent.getEmail()))
                .thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> studentService.addNewStudent(youngStudent));
    }

    // ================= GET =================

    @Test
    void getStudents_success(){
        List<Student> students = new ArrayList<>();
        students.add(student);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getStudents();

        assertEquals(1, result.size());
        assertEquals(student, result.get(0));
    }

    @Test
    void getStudents_empty(){
        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Student> result = studentService.getStudents();

        assertEquals(0, result.size());
    }

    @Test
    void getStudentById_success(){
        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1);

        assertEquals("Sahel", result.getName());
    }

    @Test
    void getStudentById_notFound(){
        when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> studentService.getStudentById(1));
    }

    @Test
    void getStudentsByName_success(){
        List<Student> students = new ArrayList<>();
        students.add(student);

        when(studentRepository.findByNameIgnoreCase("Sahel"))
                .thenReturn(students);

        List<Student> result = studentService.getStudentsByName("Sahel");

        assertEquals(1, result.size());
        assertEquals("Sahel", result.get(0).getName());
    }

    @Test
    void getStudentsByName_empty(){
        when(studentRepository.findByNameIgnoreCase("NonExistent"))
                .thenReturn(new ArrayList<>());

        List<Student> result = studentService.getStudentsByName("NonExistent");

        assertEquals(0, result.size());
    }

    // ================= FULL UPDATE =================

    @Test
    void updateStudentFull_success(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student updated = new Student(
                "New",
                "new@mail.com",
                "079000000",
                LocalDate.of(1999,1,1)
        );

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("new@mail.com"))
                .thenReturn(false);

        studentService.updateStudentFull(1, updated);

        assertEquals("New", existing.getName());
        assertEquals("new@mail.com", existing.getEmail());
        assertEquals("079000000", existing.getPhoneNumber());
    }

    @Test
    void updateStudentFull_missingRequiredField(){
        Student updated = new Student();
        updated.setName("New");
        updated.setEmail("new@mail.com");
        updated.setPhoneNumber("079000000");
        updated.setDob(null);

        assertThrows(IllegalStateException.class,
                () -> studentService.updateStudentFull(1, updated));
    }

    @Test
    void updateStudentFull_duplicateEmail(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student updated = new Student(
                "New",
                "duplicate@mail.com",
                "079000000",
                LocalDate.of(1999,1,1)
        );

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("duplicate@mail.com"))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> studentService.updateStudentFull(1, updated));
    }

    @Test
    void updateStudentFull_studentNotFound(){
        when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> studentService.updateStudentFull(1, student));
    }

    // ================= PARTIAL UPDATE =================

    @Test
    void updateStudentPartial_updateNameOnly(){

        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student incoming = new Student();
        incoming.setName("NewName");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));

        studentService.updateStudentPartial(1,incoming);

        assertEquals("NewName", existing.getName());
        assertEquals("old@mail.com", existing.getEmail());
    }

    @Test
    void updateStudentPartial_updateEmailOnly(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student incoming = new Student();
        incoming.setEmail("newemail@mail.com");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("newemail@mail.com"))
                .thenReturn(false);

        studentService.updateStudentPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("newemail@mail.com", existing.getEmail());
    }

    @Test
    void updateStudentPartial_updatePhoneOnly(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student incoming = new Student();
        incoming.setPhoneNumber("0799999999");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));

        studentService.updateStudentPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("0799999999", existing.getPhoneNumber());
    }

    @Test
    void updateStudentPartial_updateDobOnly(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student incoming = new Student();
        incoming.setDob(LocalDate.of(1998, 5, 15));

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));

        studentService.updateStudentPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals(LocalDate.of(1998, 5, 15), existing.getDob());
    }

    @Test
    void updateStudentPartial_duplicateEmail(){
        Student existing = new Student(
                "Old",
                "old@mail.com",
                "078000000",
                LocalDate.of(2000,1,1)
        );

        Student incoming = new Student();
        incoming.setEmail("taken@mail.com");

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("taken@mail.com"))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> studentService.updateStudentPartial(1, incoming));
    }

    @Test
    void updateStudentPartial_studentNotFound(){
        when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> studentService.updateStudentPartial(1, student));
    }

    // ================= DELETE =================

    @Test
    void deleteStudent_success(){

        when(studentRepository.existsById(1)).thenReturn(true);

        studentService.deleteStudent(1);

        verify(studentRepository).deleteById(1);
    }

    @Test
    void deleteStudent_notFound(){
        when(studentRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> studentService.deleteStudent(1));
    }
}