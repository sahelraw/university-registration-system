package com.uni.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uni.demo.entites.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {

    List<Enrollment> findByStudentId(Integer studentId);

    boolean existsByStudentIdAndCourseId(Integer studentId,Integer courseId);

}