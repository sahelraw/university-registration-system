package com.uni.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uni.demo.entites.Major;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {

    boolean existsByName(String name);

    Optional<Major> findByNameIgnoreCase(String name);
}
