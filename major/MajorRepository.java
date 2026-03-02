package com.springtest1.springtest1.major;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {

    boolean existsByName(String name);

    Optional<Major> findByNameIgnoreCase(String name);
}
