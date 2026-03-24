package com.uni.demo.enrollment;

import com.uni.demo.course.Course;
import com.uni.demo.course.CourseRepository;
import com.uni.demo.major.Major;
import com.uni.demo.major.MajorRepository;
import com.uni.demo.student.Student;
import com.uni.demo.student.StudentRepository;
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
    private CourseRepository courseRepository;

    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Student student;
    private Course course;
    private Major major;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        major = new Major("IT", "Information Technology");
        major.setId(1); // Set ID

        student = new Student(
                "Ahmed",
                "ahmed@mail.com",
                "0790000000",
                LocalDate.of(2000, 1, 1)
        );
        student.setId(1); // Set ID

        course = new Course();
        course.setName("Java");
        course.setHours(3);
        course.setMajor(major);
        course.setId(1); // Set ID

        enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setMajor(major);
        enrollment.setGrade(4);
        enrollment.setSemester("1");
        enrollment.setYear(2024);
    }

    @Test
    void enrollStudent_success() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1, 1)).thenReturn(false);
        when(enrollmentRepository.findByStudentId(1)).thenReturn(new ArrayList<>());

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
    void enrollStudent_courseNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_majorNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(majorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_alreadyEnrolled() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1, 1)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_exceedsMaxHours() {
        Enrollment existingEnrollment = new Enrollment();
        Course existingCourse = new Course();
        existingCourse.setHours(16);
        existingEnrollment.setCourse(existingCourse);

        List<Enrollment> studentEnrollments = new ArrayList<>();
        studentEnrollments.add(existingEnrollment);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1, 1)).thenReturn(false);
        when(enrollmentRepository.findByStudentId(1)).thenReturn(studentEnrollments);

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.enrollStudent(enrollment));
    }

    @Test
    void enrollStudent_courseWithoutMajor() {
        course.setMajor(null);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

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

        assertEquals(0, result.size());
    }

    @Test
    void updateEnrollment_success() {
        Enrollment existing = new Enrollment();
        existing.setGrade(3);
        existing.setSemester("1");
        existing.setYear(2023);

        Enrollment updated = new Enrollment();
        updated.setGrade(4);
        updated.setSemester("2");
        updated.setYear(2024);

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.updateEnrollment(1, updated);

        assertEquals(4, existing.getGrade());
        assertEquals("2", existing.getSemester());
        assertEquals(2024, existing.getYear());
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void updateEnrollment_notFound() {
        when(enrollmentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> enrollmentService.updateEnrollment(1, new Enrollment()));
    }

    @Test
    void partialUpdateEnrollment_updateGradeOnly() {
        Enrollment existing = new Enrollment();
        existing.setGrade(3);
        existing.setSemester("1");
        existing.setYear(2023);

        Enrollment incoming = new Enrollment();
        incoming.setGrade(4);

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.partialUpdateEnrollment(1, incoming);

        assertEquals(4, existing.getGrade());
        assertEquals("1", existing.getSemester());
        assertEquals(2023, existing.getYear());
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void partialUpdateEnrollment_updateSemesterOnly() {
        Enrollment existing = new Enrollment();
        existing.setGrade(3);
        existing.setSemester("1");
        existing.setYear(2023);

        Enrollment incoming = new Enrollment();
        incoming.setSemester("2");

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.partialUpdateEnrollment(1, incoming);

        assertEquals(3, existing.getGrade());
        assertEquals("2", existing.getSemester());
        assertEquals(2023, existing.getYear());
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void partialUpdateEnrollment_updateYearOnly() {
        Enrollment existing = new Enrollment();
        existing.setGrade(3);
        existing.setSemester("1");
        existing.setYear(2023);

        Enrollment incoming = new Enrollment();
        incoming.setYear(2025);

        when(enrollmentRepository.findById(1)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        enrollmentService.partialUpdateEnrollment(1, incoming);

        assertEquals(3, existing.getGrade());
        assertEquals("1", existing.getSemester());
        assertEquals(2025, existing.getYear());
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