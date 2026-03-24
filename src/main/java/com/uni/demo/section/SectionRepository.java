package com.uni.demo.section;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    boolean existsByTeacherIdAndDateAndTime(Integer teacherId, LocalDate date, LocalTime time);
}
