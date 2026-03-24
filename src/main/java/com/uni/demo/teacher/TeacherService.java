package com.uni.demo.teacher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // ===== GET =====
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(int teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalStateException("Teacher not found"));
    }

    // ===== ADD =====
    public void addNewTeacher(Teacher teacher) {

        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        teacherRepository.save(teacher);
    }

    // ===== DELETE =====
    public void deleteTeacher(int teacherId) {

        if (!teacherRepository.existsById(teacherId)) {
            throw new IllegalStateException("Teacher not found");
        }

        teacherRepository.deleteById(teacherId);
    }

    // ================= FULL UPDATE =================
@Transactional
public void updateTeacherFull(int teacherId, Teacher incomingTeacher) {

    if (incomingTeacher.getName() == null || incomingTeacher.getName().isEmpty() ||
        incomingTeacher.getEmail() == null || incomingTeacher.getEmail().isEmpty() ||
        incomingTeacher.getPhoneNumber() == null || incomingTeacher.getPhoneNumber().isEmpty() ||
        incomingTeacher.getMajorName() == null || incomingTeacher.getMajorName().isEmpty()) {

        throw new IllegalStateException("All teacher fields are required for full update");
    }

    Teacher existing = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    if (!Objects.equals(existing.getEmail(), incomingTeacher.getEmail()) &&
            teacherRepository.existsByEmail(incomingTeacher.getEmail())) {

        throw new IllegalStateException("Email already taken");
    }

    existing.setName(incomingTeacher.getName());
    existing.setEmail(incomingTeacher.getEmail());
    existing.setPhoneNumber(incomingTeacher.getPhoneNumber());
    existing.setMajorName(incomingTeacher.getMajorName());
    teacherRepository.save(existing);
    }

// ================= PARTIAL UPDATE =================
@Transactional
public void updateTeacherPartial(int teacherId, Teacher incomingTeacher) {

    Teacher existing = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    if (incomingTeacher.getName() != null && !incomingTeacher.getName().isEmpty()) {
        existing.setName(incomingTeacher.getName());
    }

    if (incomingTeacher.getEmail() != null &&
        !incomingTeacher.getEmail().isEmpty() &&
        !Objects.equals(existing.getEmail(), incomingTeacher.getEmail())) {

        if (teacherRepository.existsByEmail(incomingTeacher.getEmail())) {
            throw new IllegalStateException("Email already taken");
        }

        existing.setEmail(incomingTeacher.getEmail());
    }

    if (incomingTeacher.getPhoneNumber() != null && !incomingTeacher.getPhoneNumber().isEmpty()) {
        existing.setPhoneNumber(incomingTeacher.getPhoneNumber());
    }

    if (incomingTeacher.getMajorName() != null && !incomingTeacher.getMajorName().isEmpty()) {
        existing.setMajorName(incomingTeacher.getMajorName());
    }

    teacherRepository.save(existing);
    }
}
