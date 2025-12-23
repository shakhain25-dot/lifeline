package com.lifeline.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ambulances")
@Data
@NoArgsConstructor
public class Ambulance {
    @Id
    private String id;
    private String driverName;
    private String currentLocation;

    @Column(name = "is_available")
    private Boolean available;
    private String phoneNumber;
}