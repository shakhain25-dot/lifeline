package com.lifeline.controller;
import com.lifeline.model.Ambulance;
import com.lifeline.service.AmbulanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance")
@CrossOrigin(origins = "http://localhost:3000")
public class AmbulanceController {

    private AmbulanceService service = new AmbulanceService();

    @GetMapping
    public List<Ambulance> getAllAmbulances() {
        return service.getAllAmbulances();
    }

    @GetMapping("/available")
    public List<Ambulance> getAvailableAmbulances() {
        return service.getAvailableAmbulances();
    }
}