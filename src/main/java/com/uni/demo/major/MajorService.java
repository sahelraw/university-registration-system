package com.uni.demo.major;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MajorService {

    private final MajorRepository majorRepository;

    // Removed CourseRepository as it's no longer needed here
    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> getMajors() {
        return majorRepository.findAll();
    }

    public Major getMajorById(int majorId) {
        return majorRepository.findById(majorId)
                .orElseThrow(() -> new IllegalStateException("Major not found"));
    }

    @Transactional
    public void addMajor(Major major) {
        if (majorRepository.existsByName(major.getName())) {
            throw new IllegalStateException("Major already exists");
        }
        majorRepository.save(major);
    }

    @Transactional
    public void deleteMajor(int majorId) {
        if (!majorRepository.existsById(majorId)) {
            throw new IllegalStateException("Major not found");
        }
        // PostgreSQL handles the CASCADE delete automatically now
        majorRepository.deleteById(majorId);
    }

    @Transactional
    public void updateMajorFull(int majorId, Major incomingMajor) {
        if (incomingMajor.getName() == null || incomingMajor.getName().isEmpty() ||
            incomingMajor.getDescription() == null || incomingMajor.getDescription().isEmpty()) {
            throw new IllegalStateException("All fields required for full update");
        }

        Major existing = majorRepository.findById(majorId)
                .orElseThrow(() -> new IllegalStateException("Major not found"));

        if (!existing.getName().equalsIgnoreCase(incomingMajor.getName()) &&
                majorRepository.existsByName(incomingMajor.getName())) {
            throw new IllegalStateException("Major name already exists");
        }

        existing.setName(incomingMajor.getName());
        existing.setDescription(incomingMajor.getDescription());
        
        // SAVE the changes
        majorRepository.save(existing);
    }

    @Transactional
    public void updateMajorPartial(int majorId, Major incomingMajor) {
        Major existing = majorRepository.findById(majorId)
                .orElseThrow(() -> new IllegalStateException("Major not found"));

        if (incomingMajor.getName() != null && !incomingMajor.getName().isEmpty()) {
            if (!existing.getName().equalsIgnoreCase(incomingMajor.getName()) &&
                    majorRepository.existsByName(incomingMajor.getName())) {
                throw new IllegalStateException("Major name already exists");
            }
            existing.setName(incomingMajor.getName());
        }

        if (incomingMajor.getDescription() != null && !incomingMajor.getDescription().isEmpty()) {
            existing.setDescription(incomingMajor.getDescription());
        }

        // SAVE the changes
        majorRepository.save(existing);
    }
}

