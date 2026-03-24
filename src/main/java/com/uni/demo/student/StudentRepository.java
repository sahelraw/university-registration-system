package com.uni.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Find student by email (used for duplication check)
    Optional<Student> findStudentByEmail(String email);

    // Faster duplication check (returns true/false)
    boolean existsByEmail(String email);

    // Get students by name (case insensitive)
    List<Student> findByNameIgnoreCase(String name);
}
