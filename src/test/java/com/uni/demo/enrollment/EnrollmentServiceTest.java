package com.uni.demo.enrollment;

import com.uni.demo.entites.Course;
import com.uni.demo.entites.Enrollment;
import com.uni.demo.entites.Section;
import com.uni.demo.entites.Student;
import com.uni.demo.repositories.EnrollmentRepository;
import com.uni.demo.repositories.SectionRepository;
import com.uni.demo.repositories.StudentRepository;
import com.uni.demo.services.EnrollmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Student student;
    private Section section;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        student = new Student(
                "Ahmed",
                "ahmed@mail.com",
                "0790000000",
                LocalDate.of(2000, 1, 1)
        );
        student.setId(1);

        section = new Section();
        section.setId(1);

        enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSection(section);
    }

    @Test
    void enrollStudent_success() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(sectionRepository.findById(1)).thenReturn(Optional.of(section));
        when(enrollmentRepository.existsByStudentIdAndSectionId(1, 1)).thenReturn(false);

        enrollmentService.enrollStudent(enrollment);

        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void enrollStudent_studentNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_sectionNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(sectionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_alreadyEnrolled() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(sectionRepository.findById(1)).thenReturn(Optional.of(section));
        when(enrollmentRepository.existsByStudentIdAndSectionId(1, 1)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void getStudentEnrollments_success() {
        List<Enrollment> enrollments = new ArrayList<>();
        enrollments.add(enrollment);

        when(enrollmentRepository.findByStudentId(1)).thenReturn(enrollments);

        List<Enrollment> result = enrollmentService.getStudentEnrollments(1);

        assertEquals(1, result.size());
        assertEquals(enrollment, result.get(0));
    }

    @Test
    void getStudentEnrollments_empty() {
        when(enrollmentRepository.findByStudentId(1)).thenReturn(new ArrayList<>());

        List<Enrollment> result = enrollmentService.getStudentEnrollments(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateEnrollment_success() {
        Enrollment existing = new Enrollment();
        existing.setStudent(student);
        existing.setSection(section);

        Student newStudent = new Student(
                "Sara",
                "sara@mail.com",
                "0791111111",
                LocalDate.of(2000, 2, 2)
        );
        newStudent.setId(2);

        Section newSection = new Section();
        newSection.setId(2);

        Enrollment updated = new Enrollment();
        updated.setStudent(newStudent);
        updated.setSection(newSection);

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(2)).thenReturn(Optional.of(newStudent));
        when(sectionRepository.findById(2)).thenReturn(Optional.of(newSection));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.updateEnrollment(1, updated);

        assertEquals(newStudent, existing.getStudent());
        assertEquals(newSection, existing.getSection());
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void updateEnrollment_notFound() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.updateEnrollment(1, new Enrollment()));
    }

    @Test
    void partialUpdateEnrollment_updateSectionOnly() {
        Enrollment existing = new Enrollment();
        existing.setStudent(student);
        existing.setSection(section);

        Section newSection = new Section();
        newSection.setId(2);

        Enrollment incoming = new Enrollment();
        incoming.setSection(newSection);

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(sectionRepository.findById(2)).thenReturn(Optional.of(newSection));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.partialUpdateEnrollment(1, incoming);

        assertEquals(newSection, existing.getSection());
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void partialUpdateEnrollment_notFound() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.partialUpdateEnrollment(1, new Enrollment()));
    }

    @Test
    void deleteEnrollment_success() {
        when(enrollmentRepository.existsById(1)).thenReturn(true);

        enrollmentService.deleteEnrollment(1);

        verify(enrollmentRepository).deleteById(1);
    }

    @Test
    void deleteEnrollment_notFound() {
        when(enrollmentRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.deleteEnrollment(1));
    }
}
