package com.lifeline.repository;

import com.lifeline.model.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends JpaRepository<EmergencyRequest, Long> {
    // Spring Data JPA automatically provides:
    // - save()
    // - findAll()
    // - findById()
    // - deleteById()
    // - count()
}