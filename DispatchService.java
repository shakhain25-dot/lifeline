package com.lifeline.service;

import com.lifeline.algorithm.DijkstraAlgorithm;
import com.lifeline.datastructures.Graph;
import com.lifeline.model.Ambulance;
import com.lifeline.model.Dispatch;
import com.lifeline.model.EmergencyRequest;
import com.lifeline.repository.DispatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DispatchService {

    @Autowired
    private DispatchRepository repository;

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private EmergencyService emergencyService;

    private Graph cityGraph;

    public DispatchService() {
        initializeCityGraph();
    }

    private void initializeCityGraph() {
        cityGraph = new Graph();

        // ==================== MAJOR HOSPITALS ====================
        cityGraph.addNode("Aga Khan Hospital", 150, 120, 24.8902, 67.0739);
        cityGraph.addNode("Liaquat National Hospital", 180, 140, 24.8820, 67.0680);
        cityGraph.addNode("Jinnah Hospital", 95, 190, 24.8738, 67.0264);
        cityGraph.addNode("Civil Hospital", 85, 200, 24.8607, 67.0011);
        cityGraph.addNode("Ziauddin Hospital", 130, 290, 24.8200, 67.0320);
        cityGraph.addNode("South City Hospital", 140, 310, 24.8100, 67.0380);

        // ==================== CENTRAL AREAS ====================
        cityGraph.addNode("Saddar", 90, 200, 24.8607, 67.0011);
        cityGraph.addNode("Gulshan-e-Iqbal", 250, 150, 24.9207, 67.0821);
        cityGraph.addNode("North Nazimabad", 200, 80, 24.9373, 67.0377);
        cityGraph.addNode("Federal B Area", 220, 100, 24.9100, 67.0600);
        cityGraph.addNode("Nazimabad", 180, 90, 24.9200, 67.0400);

        // ==================== SOUTH KARACHI ====================
        cityGraph.addNode("Clifton", 120, 280, 24.8138, 67.0294);
        cityGraph.addNode("Defence", 150, 320, 24.8075, 67.0336);
        cityGraph.addNode("Boat Basin", 125, 295, 24.8150, 67.0300);
        cityGraph.addNode("Zamzama", 135, 305, 24.8120, 67.0310);
        cityGraph.addNode("Darakhshan", 145, 315, 24.8090, 67.0330);
        cityGraph.addNode("Phase 4", 160, 330, 24.8060, 67.0350);
        cityGraph.addNode("Phase 5", 170, 340, 24.8040, 67.0370);
        cityGraph.addNode("Phase 6", 180, 350, 24.8020, 67.0390);
        cityGraph.addNode("Phase 7", 190, 360, 24.8000, 67.0410);
        cityGraph.addNode("Phase 8", 200, 370, 24.7980, 67.0430);

        // ==================== EAST KARACHI ====================
        cityGraph.addNode("Malir", 380, 180, 24.8905, 67.2074);
        cityGraph.addNode("Malir Cantt", 390, 190, 24.8850, 67.2150);
        cityGraph.addNode("Korangi", 320, 260, 24.8354, 67.1101);
        cityGraph.addNode("Korangi Industrial", 330, 270, 24.8320, 67.1150);
        cityGraph.addNode("Landhi", 340, 280, 24.8200, 67.1300);
        cityGraph.addNode("Shah Faisal Colony", 300, 220, 24.8600, 67.1000);
        cityGraph.addNode("Model Colony", 280, 200, 24.8700, 67.0900);

        // ==================== NORTH KARACHI ====================
        cityGraph.addNode("North Karachi", 210, 60, 24.9800, 67.0500);
        cityGraph.addNode("New Karachi", 230, 50, 25.0000, 67.0600);
        cityGraph.addNode("Surjani Town", 240, 40, 25.0200, 67.0650);
        cityGraph.addNode("Orangi Town", 160, 70, 24.9500, 67.0200);
        cityGraph.addNode("SITE Area", 140, 110, 24.9000, 67.0300);

        // ==================== WEST KARACHI ====================
        cityGraph.addNode("Lyari", 70, 180, 24.8700, 66.9900);
        cityGraph.addNode("Kemari", 50, 160, 24.8000, 66.9800);
        cityGraph.addNode("Keamari Port", 40, 150, 24.8050, 66.9750);
        cityGraph.addNode("Maripur", 30, 140, 24.8100, 66.9700);

        // ==================== COMMERCIAL AREAS ====================
        cityGraph.addNode("Tariq Road", 200, 170, 24.8700, 67.0600);
        cityGraph.addNode("Bahadurabad", 240, 130, 24.8900, 67.0750);
        cityGraph.addNode("Shahrah-e-Faisal", 160, 200, 24.8500, 67.0500);
        cityGraph.addNode("Rashid Minhas Road", 270, 140, 24.9000, 67.0850);
        cityGraph.addNode("University Road", 220, 120, 24.9100, 67.0700);

        // ==================== MAIN ROADS & CONNECTIONS ====================

        // Hospital connections
        cityGraph.addEdge("Aga Khan Hospital", "Gulshan-e-Iqbal", 4.5);
        cityGraph.addEdge("Aga Khan Hospital", "Saddar", 6.2);
        cityGraph.addEdge("Aga Khan Hospital", "Liaquat National Hospital", 2.8);
        cityGraph.addEdge("Liaquat National Hospital", "Gulshan-e-Iqbal", 3.5);
        cityGraph.addEdge("Jinnah Hospital", "Saddar", 2.1);
        cityGraph.addEdge("Civil Hospital", "Saddar", 1.5);
        cityGraph.addEdge("Ziauddin Hospital", "Clifton", 2.3);
        cityGraph.addEdge("South City Hospital", "Defence", 1.8);

        // Clifton & Defence Area
        cityGraph.addEdge("Clifton", "Defence", 3.8);
        cityGraph.addEdge("Clifton", "Saddar", 5.5);
        cityGraph.addEdge("Clifton", "Boat Basin", 1.5);
        cityGraph.addEdge("Boat Basin", "Zamzama", 1.2);
        cityGraph.addEdge("Zamzama", "Darakhshan", 1.1);
        cityGraph.addEdge("Darakhshan", "Phase 4", 1.3);
        cityGraph.addEdge("Phase 4", "Phase 5", 1.5);
        cityGraph.addEdge("Phase 5", "Phase 6", 1.4);
        cityGraph.addEdge("Phase 6", "Phase 7", 1.6);
        cityGraph.addEdge("Phase 7", "Phase 8", 1.5);
        cityGraph.addEdge("Defence", "Phase 4", 2.0);
        cityGraph.addEdge("Defence", "Saddar", 7.1);
        cityGraph.addEdge("Defence", "Korangi", 11.4);

        // Gulshan & East connections
        cityGraph.addEdge("Gulshan-e-Iqbal", "North Nazimabad", 8.3);
        cityGraph.addEdge("Gulshan-e-Iqbal", "Malir", 12.5);
        cityGraph.addEdge("Gulshan-e-Iqbal", "Korangi", 9.6);
        cityGraph.addEdge("Gulshan-e-Iqbal", "Bahadurabad", 3.2);
        cityGraph.addEdge("Gulshan-e-Iqbal", "Tariq Road", 4.8);
        cityGraph.addEdge("Gulshan-e-Iqbal", "University Road", 3.5);
        cityGraph.addEdge("Bahadurabad", "Federal B Area", 4.0);
        cityGraph.addEdge("Bahadurabad", "Rashid Minhas Road", 2.5);

        // North Karachi connections
        cityGraph.addEdge("North Nazimabad", "Saddar", 9.2);
        cityGraph.addEdge("North Nazimabad", "Nazimabad", 2.5);
        cityGraph.addEdge("North Nazimabad", "Federal B Area", 3.8);
        cityGraph.addEdge("Nazimabad", "Federal B Area", 2.0);
        cityGraph.addEdge("Federal B Area", "University Road", 2.8);
        cityGraph.addEdge("North Karachi", "North Nazimabad", 4.5);
        cityGraph.addEdge("North Karachi", "New Karachi", 3.2);
        cityGraph.addEdge("New Karachi", "Surjani Town", 4.0);
        cityGraph.addEdge("Orangi Town", "North Nazimabad", 5.5);
        cityGraph.addEdge("SITE Area", "Orangi Town", 3.8);
        cityGraph.addEdge("SITE Area", "North Nazimabad", 4.2);

        // Malir & East connections
        cityGraph.addEdge("Malir", "Korangi", 8.7);
        cityGraph.addEdge("Malir", "Malir Cantt", 2.5);
        cityGraph.addEdge("Malir Cantt", "Landhi", 6.5);
        cityGraph.addEdge("Korangi", "Korangi Industrial", 2.0);
        cityGraph.addEdge("Korangi Industrial", "Landhi", 3.5);
        cityGraph.addEdge("Korangi", "Shah Faisal Colony", 5.2);
        cityGraph.addEdge("Shah Faisal Colony", "Model Colony", 3.8);
        cityGraph.addEdge("Model Colony", "Gulshan-e-Iqbal", 4.5);

        // Saddar central hub
        cityGraph.addEdge("Saddar", "Shahrah-e-Faisal", 3.5);
        cityGraph.addEdge("Saddar", "Lyari", 4.2);
        cityGraph.addEdge("Saddar", "Tariq Road", 3.8);
        cityGraph.addEdge("Shahrah-e-Faisal", "Tariq Road", 2.5);
        cityGraph.addEdge("Tariq Road", "University Road", 3.2);

        // West Karachi connections
        cityGraph.addEdge("Lyari", "Kemari", 3.5);
        cityGraph.addEdge("Kemari", "Keamari Port", 2.0);
        cityGraph.addEdge("Keamari Port", "Maripur", 2.5);
        cityGraph.addEdge("Lyari", "Orangi Town", 6.5);

        // Commercial area connections
        cityGraph.addEdge("Tariq Road", "Bahadurabad", 4.0);
        cityGraph.addEdge("University Road", "Rashid Minhas Road", 3.5);
        cityGraph.addEdge("Rashid Minhas Road", "Malir", 8.5);

        // Cross-city expressways
        cityGraph.addEdge("Phase 8", "Korangi", 15.5);
        cityGraph.addEdge("North Karachi", "Malir", 18.0);
        cityGraph.addEdge("Surjani Town", "Landhi", 22.0);
        cityGraph.addEdge("Defence", "Model Colony", 13.5);
        cityGraph.addEdge("Clifton", "Shah Faisal Colony", 12.0);

        System.out.println("✓ City Graph initialized with " + cityGraph.getNodes().size() + " locations");
    }

    public Dispatch dispatchNearestAmbulance(Long requestId) {
        EmergencyRequest request = emergencyService.getRequestById(requestId).orElse(null);
        if (request == null) {
            System.out.println("✗ Emergency request not found: " + requestId);
            return null;
        }

        List<Ambulance> availableAmbulances = ambulanceService.getAvailableAmbulances();
        if (availableAmbulances.isEmpty()) {
            System.out.println("✗ No available ambulances");
            return null;
        }

        Ambulance nearest = null;
        double minDistance = Double.INFINITY;
        List<String> bestPath = null;

        System.out.println("\n🚑 Finding nearest ambulance for: " + request.getLocation());
        System.out.println("Available ambulances: " + availableAmbulances.size());

        for (Ambulance amb : availableAmbulances) {
            try {
                DijkstraAlgorithm.DijkstraResult result =
                        DijkstraAlgorithm.findShortestPath(cityGraph, amb.getCurrentLocation(), request.getLocation());

                System.out.println("  " + amb.getId() + " at " + amb.getCurrentLocation() +
                        " → Distance: " + String.format("%.2f", result.distance) + " km");

                if (result.distance < minDistance) {
                    minDistance = result.distance;
                    nearest = amb;
                    bestPath = result.path;
                }
            } catch (Exception e) {
                System.err.println("✗ Error calculating route for " + amb.getId() + ": " + e.getMessage());
            }
        }

        if (nearest != null) {
            System.out.println("\n✓ Selected: " + nearest.getId() + " (Distance: " +
                    String.format("%.2f", minDistance) + " km)");
            System.out.println("Route: " + String.join(" → ", bestPath));

            Dispatch dispatch = new Dispatch();
            dispatch.setRequestId(requestId);
            dispatch.setAmbulanceId(nearest.getId());
            dispatch.setFromLocation(nearest.getCurrentLocation());
            dispatch.setToLocation(request.getLocation());
            dispatch.setDistance(minDistance);
            dispatch.setPath(String.join(",", bestPath));

            // Update ambulance status
            ambulanceService.updateAmbulanceStatus(nearest.getId(), false);
            ambulanceService.updateAmbulanceLocation(nearest.getId(), request.getLocation());

            // Update request status
            emergencyService.updateRequestStatus(requestId, "DISPATCHED");

            return repository.save(dispatch);
        }

        System.out.println("✗ Could not dispatch ambulance");
        return null;
    }

    public List<Dispatch> getAllDispatches() {
        return repository.findAll();
    }

    public List<Dispatch> getDispatchesByAmbulance(String ambulanceId) {
        return repository.findByAmbulanceId(ambulanceId);
    }

    /**
     * Get all available locations in the city
     */
    public List<String> getAllLocations() {
        return new java.util.ArrayList<>(cityGraph.getNodes().keySet());
    }

    /**
     * Calculate route between two locations
     */
    public DijkstraAlgorithm.DijkstraResult calculateRoute(String from, String to) {
        return DijkstraAlgorithm.findShortestPath(cityGraph, from, to);
    }

    /**
     * Get city graph for visualization
     */
    public Graph getCityGraph() {
        return cityGraph;
    }
}