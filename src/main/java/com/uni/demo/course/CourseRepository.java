package com.uni.demo.course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    long countByMajor_Id(Integer majorId);
    boolean existsByName(String name);

}

