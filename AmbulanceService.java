package com.lifeline.service;

import com.lifeline.model.EmergencyRequest;
import com.lifeline.repository.EmergencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyService {

    @Autowired
    private EmergencyRepository repository;

    public EmergencyRequest createRequest(EmergencyRequest request) {
        return repository.save(request);
    }

    public List<EmergencyRequest> getAllRequests() {
        return repository.findAll();
    }

    public Optional<EmergencyRequest> getRequestById(Long id) {
        return repository.findById(id);
    }

    public void updateRequestStatus(Long id, String status) {
        repository.findById(id).ifPresent(request -> {
            request.setStatus(status);
            repository.save(request);
        });
    }

    public void deleteRequest(Long id) {
        repository.deleteById(id);
    }
}