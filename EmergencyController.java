package com.lifeline.controller;

import com.lifeline.model.EmergencyRequest;
import com.lifeline.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = "http://localhost:3000")
public class EmergencyController {

    @Autowired
    private EmergencyService service;

    @PostMapping
    public ResponseEntity<EmergencyRequest> createRequest(@RequestBody EmergencyRequest request) {
        EmergencyRequest created = service.createRequest(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyRequest>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyRequest> getRequestById(@PathVariable Long id) {
        return service.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<EmergencyRequest>> getPendingRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyRequest> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        EmergencyRequest updated = service.updateRequestStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.ok().build();
    }
}