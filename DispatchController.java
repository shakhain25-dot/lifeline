package com.lifeline.controller; // Remove "main.java."
import com.lifeline.model.Dispatch; // Update this import too
import com.lifeline.service.DispatchService; // Update this import too
import main.java.com.lifeline.model.Dispatch;
import main.java.com.lifeline.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.lifeline.algorithm.DijkstraAlgorithm; // Corrected import
import com.lifeline.datastructures.Graph;      // Corrected import
@RestController
@RequestMapping("/api/dispatch")
@CrossOrigin(origins = "http://localhost:3000")
public class DispatchController {

    @Autowired
    private DispatchService service;

    @PostMapping("/{requestId}")
    public ResponseEntity<Dispatch> dispatchAmbulance(@PathVariable Long requestId) {
        Dispatch dispatch = service.dispatchNearestAmbulance(requestId);
        return dispatch != null ? ResponseEntity.ok(dispatch) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Dispatch>> getAllDispatches() {
        return ResponseEntity.ok(service.getAllDispatches());
    }

    @GetMapping("/ambulance/{ambulanceId}")
    public ResponseEntity<List<Dispatch>> getDispatchesByAmbulance(@PathVariable String ambulanceId) {
        return ResponseEntity.ok(service.getDispatchesByAmbulance(ambulanceId));
    }
}