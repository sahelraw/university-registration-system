package com.uni.demo.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {

    List<Enrollment> findByStudentId(Integer studentId);

    boolean existsByStudentIdAndCourseId(Integer studentId,Integer courseId);

}