package com.uni.demo.enrollment;

import com.uni.demo.student.Student;
import com.uni.demo.course.Course;
import com.uni.demo.major.Major;

import jakarta.persistence.*;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id","course_id","semester","\"year\""})
        }
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name="major_id")
    private Major major;

    private String semester;
@Column(name = "\"year\"")
    private Integer year;

    private Integer grade;

    public Enrollment(){}

    public Enrollment(Student student, Course course, Major major, String semester, Integer year) {
        this.student = student;
        this.course = course;
        this.major = major;
        this.semester = semester;
        this.year = year;
    }

    public Integer getId() { return id; }

    public Student getStudent() { return student; }

    public Course getCourse() { return course; }

    public Major getMajor() { return major; }

    public String getSemester() { return semester; }

    public Integer getYear() { return year; }

    public Integer getGrade() { return grade; }

    public void setGrade(Integer grade) { this.grade = grade; }

    public void setSemester(String semester) { this.semester = semester; }

    public void setYear(Integer year) { this.year = year; }

    public void setStudent(Student student) { this.student = student; }

    public void setCourse(Course course) { this.course = course; }

    public void setMajor(Major major) { this.major = major; }
}