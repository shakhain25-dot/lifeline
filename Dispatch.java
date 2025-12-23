package com.lifeline.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dispatches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Column(name = "emergency_id")
    private Long emergencyId;

    @Column(name = "ambulance_id")
    private String ambulanceId;

    @Column(name = "dispatch_time")
    private LocalDateTime dispatchTime;

    @Column(name = "estimated_arrival")
    private LocalDateTime estimatedArrival;

    private String status;

    @Column(name = "from_location")
    private String fromLocation;

    @Column(name = "to_location")
    private String toLocation;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "route_info", length = 1000)
    private String routeInfo;
}