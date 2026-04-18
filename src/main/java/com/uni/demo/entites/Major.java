package com.uni.demo.entites;

import jakarta.persistence.*;
//The cascading has been done in PostgreSQL database, so it is not needed here.
@Entity
@Table(name = "majors")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private String description;

    public Major() {}

    public Major(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setId(int id) {
        this.id = id;
    }
}