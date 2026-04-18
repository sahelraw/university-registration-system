package com.uni.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uni.demo.entites.Teacher;

import java.util.Optional;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findTeacherByEmail(String email);

    boolean existsByEmail(String email);

    List<Teacher> findByNameIgnoreCase(String name);
}
