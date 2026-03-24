package com.uni.demo.course;

import com.uni.demo.major.Major;
import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer hours;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    public Course() {}

    public Course(String name, Integer hours, Major major) {
        this.name = name;
        this.hours = hours;
        this.major = major;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getHours() {
        return hours;
    }

    public Major getMajor() {
        return major;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void setMajor(Major major) {
        this.major = major;
    }
}