package com.uni.demo.teacher;

import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    private String majorName;

    public Teacher() {}

    public Teacher(String name, String email, String phoneNumber, String majorName) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.majorName = majorName;
    }

    // GETTERS

    public int getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getMajorName() { return majorName; }

    // SETTERS

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setMajorName(String majorName) { this.majorName = majorName; }
    public void setId(Integer id) {
    this.id = id;
}

   
}
