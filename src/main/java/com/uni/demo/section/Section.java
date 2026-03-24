package com.uni.demo.section;

import com.uni.demo.course.Course;
import com.uni.demo.teacher.Teacher;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Section() {}

    public Section(LocalDate date, LocalTime time, Course course, Teacher teacher) {
        this.date = date;
        this.time = time;
        this.course = course;
        this.teacher = teacher;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
}
