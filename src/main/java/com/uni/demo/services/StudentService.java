package com.uni.demo.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uni.demo.entites.Student;
import com.uni.demo.repositories.StudentRepository;

import com.uni.demo.entites.Major;
import com.uni.demo.repositories.MajorRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;

    public StudentService(StudentRepository studentRepository, MajorRepository majorRepository) {
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
    }

    // ================= GET =================

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalStateException("Student with ID " + studentId + " does not exist"));
    }

    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByNameIgnoreCase(name);
    }

    // ================= ADD =================

    public void addNewStudent(Student student) {

        // Email duplication check
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        // Age validation (must be 18+)
        if (student.getAge() == null || student.getAge() < 18) {
            throw new IllegalStateException("Student must be at least 18 years old");
        }
         if (student.getMajor() != null && student.getMajor().getId() != null) {
        Major major = majorRepository.findById(student.getMajor().getId())
                .orElseThrow(() -> new IllegalStateException("major not found"));
        student.setMajor(major);
    }

        studentRepository.save(student);
    }

    // ================= DELETE =================

    public void deleteStudent(int studentId) {

        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw new IllegalStateException(
                    "Student with ID " + studentId + " does not exist");
        }

        studentRepository.deleteById(studentId);
    }

    // ================= UPDATE FULL =================

    @Transactional
    public void updateStudentFull(int studentId, Student updatedStudent) {

    // 1. Validate that all required fields are present
    if (updatedStudent.getName() == null || updatedStudent.getName().isEmpty() ||
        updatedStudent.getEmail() == null || updatedStudent.getEmail().isEmpty() ||
        updatedStudent.getPhoneNumber() == null || updatedStudent.getPhoneNumber().isEmpty() ||
        updatedStudent.getDob() == null) {
        throw new IllegalStateException("All fields (Name, Email, Phone, DOB) are required for a full update");
    }

    Student student = studentRepository.findById(studentId)
            .orElseThrow(() ->
                    new IllegalStateException("Student with ID " + studentId + " does not exist"));

    // 2. Validate Age (Assuming getAge() calculates based on DOB)
    // Note: If age is derived, just checking DOB above is usually enough, but we keep your check here.
    if (updatedStudent.getAge() != null && updatedStudent.getAge() < 18) {
         throw new IllegalStateException("Student must be at least 18 years old");
    }

    // 3. Handle Email Update
    if (!Objects.equals(student.getEmail(), updatedStudent.getEmail())) {
        if (studentRepository.existsByEmail(updatedStudent.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }
        student.setEmail(updatedStudent.getEmail());
    }

    // 4. Update the rest
    student.setName(updatedStudent.getName());
    student.setPhoneNumber(updatedStudent.getPhoneNumber());
    student.setDob(updatedStudent.getDob());
    studentRepository.save(student);
    }

    // ================= UPDATE PARTIAL =================

@Transactional
public void updateStudentPartial(int studentId, Student incomingStudent) {

    // 1. Find existing student
    Student existingStudent = studentRepository.findById(studentId)
            .orElseThrow(() ->
                    new IllegalStateException("Student with ID " + studentId + " does not exist"));

    // ===== NAME =====
    if (incomingStudent.getName() != null &&
            !incomingStudent.getName().isEmpty()) {

        existingStudent.setName(incomingStudent.getName());
    }

    // ===== EMAIL =====
    if (incomingStudent.getEmail() != null &&
            !incomingStudent.getEmail().isEmpty() &&
            !Objects.equals(existingStudent.getEmail(), incomingStudent.getEmail())) {

        if (studentRepository.existsByEmail(incomingStudent.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        existingStudent.setEmail(incomingStudent.getEmail());
    }

    // ===== PHONE NUMBER =====
    if (incomingStudent.getPhoneNumber() != null &&
            !incomingStudent.getPhoneNumber().isEmpty()) {

        existingStudent.setPhoneNumber(incomingStudent.getPhoneNumber());
    }

    // ===== DOB =====
    if (incomingStudent.getDob() != null) {
        existingStudent.setDob(incomingStudent.getDob());
    }

    studentRepository.save(existingStudent);
    }
    
}
