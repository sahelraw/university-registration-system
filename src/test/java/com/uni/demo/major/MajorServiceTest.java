package com.uni.demo.major;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.uni.demo.entites.Major;
import com.uni.demo.repositories.MajorRepository;
import com.uni.demo.services.MajorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MajorServiceTest {

    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private MajorService majorService;

    private Major major;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        major = new Major("IT","Technology");
    }

    // ================= CREATE =================

    @Test
    void addMajor_success() {

        when(majorRepository.existsByName("IT")).thenReturn(false);

        majorService.addMajor(major);

        verify(majorRepository).save(major);
    }

    @Test
    void addMajor_duplicate() {

        when(majorRepository.existsByName("IT")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> majorService.addMajor(major));
    }

    // ================= GET =================

    @Test
    void getMajors_success() {
        List<Major> majors = new ArrayList<>();
        majors.add(major);

        when(majorRepository.findAll()).thenReturn(majors);

        List<Major> result = majorService.getMajors();

        assertEquals(1, result.size());
        assertEquals(major, result.get(0));
    }

    @Test
    void getMajors_empty() {
        when(majorRepository.findAll()).thenReturn(new ArrayList<>());

        List<Major> result = majorService.getMajors();

        assertEquals(0, result.size());
    }

    @Test
    void getMajorById_success() {
        when(majorRepository.findById(1)).thenReturn(Optional.of(major));

        Major result = majorService.getMajorById(1);

        assertEquals("IT", result.getName());
    }

    @Test
    void getMajorById_notFound() {
        when(majorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> majorService.getMajorById(1));
    }

    // ================= FULL UPDATE =================

    @Test
    void updateMajorFull_success() {

        Major existing = new Major("Old","OldDesc");

        Major incoming = new Major("New","NewDesc");

        when(majorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.existsByName("New")).thenReturn(false);
        when(majorRepository.save(any())).thenReturn(existing);

        majorService.updateMajorFull(1, incoming);

        assertEquals("New", existing.getName());
        assertEquals("NewDesc", existing.getDescription());
        verify(majorRepository).save(existing);
    }

    @Test
    void updateMajorFull_missingName() {
        Major incoming = new Major();
        incoming.setDescription("NewDesc");

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorFull(1, incoming));
    }

    @Test
    void updateMajorFull_missingDescription() {
        Major incoming = new Major("New", "");

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorFull(1, incoming));
    }

    @Test
    void updateMajorFull_duplicateName() {
        Major existing = new Major("Old","OldDesc");
        Major incoming = new Major("Duplicate","NewDesc");

        when(majorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.existsByName("Duplicate")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorFull(1, incoming));
    }

    @Test
    void updateMajorFull_notFound() {
        when(majorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorFull(1, major));
    }

    // ================= PARTIAL UPDATE =================

    @Test
    void updateMajorPartial_updateNameOnly() {
        Major existing = new Major("Old","OldDesc");
        Major incoming = new Major("New", null);

        when(majorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.existsByName("New")).thenReturn(false);
        when(majorRepository.save(any())).thenReturn(existing);

        majorService.updateMajorPartial(1, incoming);

        assertEquals("New", existing.getName());
        assertEquals("OldDesc", existing.getDescription());
        verify(majorRepository).save(existing);
    }

    @Test
    void updateMajorPartial_updateDescriptionOnly() {
        Major existing = new Major("Old","OldDesc");
        Major incoming = new Major();
        incoming.setDescription("NewDesc");

        when(majorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.save(any())).thenReturn(existing);

        majorService.updateMajorPartial(1, incoming);

        assertEquals("Old", existing.getName());
        assertEquals("NewDesc", existing.getDescription());
        verify(majorRepository).save(existing);
    }

    @Test
    void updateMajorPartial_duplicateName() {
        Major existing = new Major("Old","OldDesc");
        Major incoming = new Major("Taken", null);

        when(majorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(majorRepository.existsByName("Taken")).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorPartial(1, incoming));
    }

    @Test
    void updateMajorPartial_notFound() {
        when(majorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> majorService.updateMajorPartial(1, major));
    }

    // ================= DELETE =================

    @Test
    void deleteMajor_success(){

        when(majorRepository.existsById(1)).thenReturn(true);

        majorService.deleteMajor(1);

        verify(majorRepository).deleteById(1);
    }

    @Test
    void deleteMajor_notFound(){
        when(majorRepository.existsById(1)).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> majorService.deleteMajor(1));
    }
}