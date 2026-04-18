package com.uni.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uni.demo.entites.Section;

import java.time.LocalDate;
import java.time.LocalTime;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    boolean existsByTeacherIdAndDateAndTime(Integer teacherId, LocalDate date, LocalTime time);
}
