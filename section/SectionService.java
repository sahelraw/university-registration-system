package com.springtest1.springtest1.section;

import com.springtest1.springtest1.course.Course;
import com.springtest1.springtest1.course.CourseRepository;
import com.springtest1.springtest1.teacher.Teacher;
import com.springtest1.springtest1.teacher.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public SectionService(SectionRepository sectionRepository,
                          CourseRepository courseRepository,
                          TeacherRepository teacherRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    // ===== GET =====
    public List<Section> getSections() {
        return sectionRepository.findAll();
    }

    public Section getSectionById(Integer id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
    }

    // ===== CREATE =====
    public Section addSection(Section section) {

        if (section.getCourse() == null || section.getCourse().getId() == null)
            throw new IllegalStateException("Course ID required");

        if (section.getTeacher() == null || section.getTeacher().getId() <= 0)
            throw new IllegalStateException("Teacher ID required");

        Course course = courseRepository.findById(section.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Teacher teacher = teacherRepository.findById(section.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (sectionRepository.existsByTeacherIdAndDateAndTime(
                teacher.getId(),
                section.getDate(),
                section.getTime())) {
            throw new IllegalStateException("Teacher already has a section at this time");
        }

        section.setCourse(course);
        section.setTeacher(teacher);

        return sectionRepository.save(section);
    }

    // ===== FULL UPDATE =====
    @Transactional
    public Section updateSectionFull(Integer id, Section incoming) {

        if (incoming.getDate() == null ||
            incoming.getTime() == null ||
            incoming.getCourse() == null ||
            incoming.getCourse().getId() == null ||
            incoming.getTeacher() == null ||
            incoming.getTeacher().getId() <= 0) {

            throw new IllegalStateException("All fields required for full update");
        }

        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Course course = courseRepository.findById(incoming.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Teacher teacher = teacherRepository.findById(incoming.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (!Objects.equals(existing.getTeacher().getId(), teacher.getId())
                || !Objects.equals(existing.getDate(), incoming.getDate())
                || !Objects.equals(existing.getTime(), incoming.getTime())) {

            if (sectionRepository.existsByTeacherIdAndDateAndTime(
                    teacher.getId(),
                    incoming.getDate(),
                    incoming.getTime())) {

                throw new IllegalStateException("Teacher already has a section at this time");
            }
        }

        existing.setDate(incoming.getDate());
        existing.setTime(incoming.getTime());
        existing.setCourse(course);
        existing.setTeacher(teacher);

        return sectionRepository.save(existing);
    }

    // ===== PARTIAL UPDATE =====
    @Transactional
    public Section updateSectionPartial(Integer id, Section incoming) {

        Section existing = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        if (incoming.getDate() != null)
            existing.setDate(incoming.getDate());

        if (incoming.getTime() != null)
            existing.setTime(incoming.getTime());

        if (incoming.getCourse() != null &&
            incoming.getCourse().getId() != null) {

            Course course = courseRepository.findById(incoming.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            existing.setCourse(course);
        }

        if (incoming.getTeacher() != null &&
            incoming.getTeacher().getId() > 0) {

            Teacher teacher = teacherRepository.findById(incoming.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            if (sectionRepository.existsByTeacherIdAndDateAndTime(
                    teacher.getId(),
                    existing.getDate(),
                    existing.getTime())) {

                throw new IllegalStateException("Teacher already has a section at this time");
            }

            existing.setTeacher(teacher);
        }

        return sectionRepository.save(existing);
    }

    // ===== DELETE =====
    public void deleteSection(Integer id) {
        if (!sectionRepository.existsById(id))
            throw new RuntimeException("Section not found");

        sectionRepository.deleteById(id);
    }
}