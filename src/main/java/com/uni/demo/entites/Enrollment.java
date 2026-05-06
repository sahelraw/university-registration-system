package com.uni.demo.entites;

import jakarta.persistence.*;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id","section_id"})
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
    @JoinColumn(name="section_id")
    private Section section;

    public Enrollment(){}

    public Enrollment(Student student, Section section) {
        this.student = student;
        this.section = section;
    }

    public Integer getId() { return id; }

    public Student getStudent() { return student; }

    public Section getSection() { return section; }

    public void setStudent(Student student) { this.student = student; }

    public void setSection(Section section) { this.section = section; }
}