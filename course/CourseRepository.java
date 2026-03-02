package com.springtest1.springtest1.course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    long countByMajor_Id(Integer majorId);
    boolean existsByName(String name);

}

