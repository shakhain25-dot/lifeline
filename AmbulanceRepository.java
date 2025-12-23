package com.lifeline.repository;

import com.lifeline.model.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, String> {
    List<Ambulance> findByAvailable(Boolean available);

    List<Ambulance> findByCurrentLocation(String currentLocation);
}
