package com.springtest1.springtest1.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findTeacherByEmail(String email);

    boolean existsByEmail(String email);

    List<Teacher> findByNameIgnoreCase(String name);
}
