package com.uni.demo.services;

import com.uni.demo.entites.Enrollment;
import com.uni.demo.entites.Section;
import com.uni.demo.entites.Student;
import com.uni.demo.repositories.EnrollmentRepository;
import com.uni.demo.repositories.SectionRepository;
import com.uni.demo.repositories.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            SectionRepository sectionRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
    }

    // ================= CREATE =================
    @Transactional
    public void enrollStudent(Enrollment enrollment) {
        if (enrollment.getStudent() == null || enrollment.getStudent().getId() == null) {
            throw new IllegalStateException("Student and Student ID are required");
        }
        if (enrollment.getSection() == null || enrollment.getSection().getId() == null) {
            throw new IllegalStateException("Section and Section ID are required");
        }

        Integer studentId = enrollment.getStudent().getId();
        Integer sectionId = enrollment.getSection().getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalStateException("section not found"));

        if (enrollmentRepository.existsByStudentIdAndSectionId(studentId, sectionId)) {
            throw new IllegalStateException("student already enrolled in this section");
        }
        // Check for time conflicts with existing enrollments
List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentId);
for (Enrollment existing : existingEnrollments) {
    Section existingSection = existing.getSection();
    if (existingSection.getDate().equals(section.getDate()) && existingSection.getTime().equals(section.getTime())) {
        throw new IllegalStateException("Student already has a section at this time");
    }
}
 // Check if student is already enrolled in this course (different sections)
        for (Enrollment existing : existingEnrollments) {
            Section existingSection = existing.getSection();
            if (existingSection.getCourse() != null && section.getCourse() != null &&
                existingSection.getCourse().getId().equals(section.getCourse().getId())) {
                throw new IllegalStateException("Student is already enrolled in this course");
            }
        }

        enrollment.setStudent(student);
        enrollment.setSection(section);

        enrollmentRepository.save(enrollment);
    }

    // ================= GET =================
    public List<Enrollment> getStudentEnrollments(Integer studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    // ================= DELETE =================
    public void deleteEnrollment(Integer enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new IllegalStateException("enrollment not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
    }

    // ================= FULL UPDATE =================
    @Transactional
    public void updateEnrollment(Integer enrollmentId, Enrollment updatedEnrollment) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("enrollment not found"));

        // Full update requires both fields to be present and valid
        if (updatedEnrollment.getStudent() != null && updatedEnrollment.getStudent().getId() != null) {
            Student student = studentRepository.findById(updatedEnrollment.getStudent().getId())
                    .orElseThrow(() -> new IllegalStateException("student not found"));
            enrollment.setStudent(student);
        } else {
            throw new IllegalStateException("Student ID is mandatory for update");
        }

        if (updatedEnrollment.getSection() != null && updatedEnrollment.getSection().getId() != null) {
            Section section = sectionRepository.findById(updatedEnrollment.getSection().getId())
                    .orElseThrow(() -> new IllegalStateException("section not found"));
            enrollment.setSection(section);
        } else {
            throw new IllegalStateException("Section ID is mandatory for update");
        }

        enrollmentRepository.save(enrollment);
    }

    // ================= PARTIAL UPDATE =================
    @Transactional
    public void partialUpdateEnrollment(Integer enrollmentId, Enrollment incoming) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("enrollment not found"));

        // Only update if the field is actually provided in the request
        if (incoming.getStudent() != null && incoming.getStudent().getId() != null) {
            Student student = studentRepository.findById(incoming.getStudent().getId())
                    .orElseThrow(() -> new IllegalStateException("student not found"));
            enrollment.setStudent(student);
        }

        if (incoming.getSection() != null && incoming.getSection().getId() != null) {
            Section section = sectionRepository.findById(incoming.getSection().getId())
                    .orElseThrow(() -> new IllegalStateException("section not found"));
            enrollment.setSection(section);
        }

        enrollmentRepository.save(enrollment);
    }
}