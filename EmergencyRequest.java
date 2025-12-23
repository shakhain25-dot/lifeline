package com.lifeline.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "caller_name", nullable = false)
    private String callerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String priority; // HIGH, MEDIUM, LOW

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String status; // PENDING, DISPATCHED, COMPLETED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }
}