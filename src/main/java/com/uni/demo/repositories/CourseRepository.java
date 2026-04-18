package com.uni.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uni.demo.entites.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    long countByMajor_Id(Integer majorId);
    boolean existsByName(String name);

}

