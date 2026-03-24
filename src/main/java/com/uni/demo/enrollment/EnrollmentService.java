package com.uni.demo.enrollment;

import com.uni.demo.student.Student;
import com.uni.demo.student.StudentRepository;
import com.uni.demo.course.Course;
import com.uni.demo.course.CourseRepository;
import com.uni.demo.major.Major;
import com.uni.demo.major.MajorRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final MajorRepository majorRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            MajorRepository majorRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.majorRepository = majorRepository;
    }

    // ================= CREATE =================
    public void enrollStudent(Enrollment enrollment) {

    Integer studentId = enrollment.getStudent().getId();
    Integer courseId = enrollment.getCourse().getId();

    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalStateException("student not found"));

    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalStateException("course not found"));

    if (course.getMajor() == null) {
        throw new IllegalStateException("course has no major assigned");
    }

    Major major = majorRepository.findById(course.getMajor().getId())
            .orElseThrow(() -> new IllegalStateException("major not found"));

    if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
        throw new IllegalStateException("student already enrolled in this course");
    }

    List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

    int totalHours = enrollments.stream()
            .mapToInt(e -> e.getCourse().getHours())
            .sum();

    if (totalHours + course.getHours() > 18) {
        throw new IllegalStateException("cannot exceed 18 credit hours");
    }

    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setMajor(major);

    enrollmentRepository.save(enrollment);
}
    // ================= GET =================
    public List<Enrollment> getStudentEnrollments(Integer studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // ================= DELETE =================
    public void deleteEnrollment(Integer enrollmentId) {

        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new IllegalStateException("enrollment not found");
        }

        enrollmentRepository.deleteById(enrollmentId);
    }

    // ================= FULL UPDATE =================
    public void updateEnrollment(Integer enrollmentId, Enrollment updatedEnrollment) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("enrollment not found"));

        enrollment.setGrade(updatedEnrollment.getGrade());
        enrollment.setSemester(updatedEnrollment.getSemester());
        enrollment.setYear(updatedEnrollment.getYear());

        enrollmentRepository.save(enrollment);
    }

    // ================= PARTIAL UPDATE =================
    public void partialUpdateEnrollment(Integer enrollmentId, Enrollment incoming) {

    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new IllegalStateException("enrollment not found"));

    if (incoming.getGrade() != null) {
        enrollment.setGrade(incoming.getGrade());
    }

    if (incoming.getSemester() != null) {
        enrollment.setSemester(incoming.getSemester());
    }

    if (incoming.getYear() != null) {
        enrollment.setYear(incoming.getYear());
    }

    enrollmentRepository.save(enrollment);
}
}