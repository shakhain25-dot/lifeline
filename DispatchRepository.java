package com.lifeline.repository;
import com.lifeline.model.Dispatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, Long> {
    // Spring Boot automatically implements save(), findAll(), delete(), etc.
    // We only need to define custom search methods:
    List<Dispatch> findByAmbulanceId(String ambulanceId);
}