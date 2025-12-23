package com.lifeline.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer x;
    private Integer y;
    private String type;

    public Location(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isHospital() {
        return "HOSPITAL".equalsIgnoreCase(type) ||
                (name != null && name.toLowerCase().contains("hospital"));
    }

    public double distanceTo(Location other) {
        if (this.latitude == null || this.longitude == null ||
                other.latitude == null || other.longitude == null) {
            return 0.0;
        }

        final int EARTH_RADIUS = 6371;
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lon2 = Math.toRadians(other.longitude);

        double a = Math.sin((lat2 - lat1) / 2) * Math.sin((lat2 - lat1) / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin((lon2 - lon1) / 2) * Math.sin((lon2 - lon1) / 2);

        return EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    // Get formatted coordinates
    public String getFormattedCoordinates() {
        if (latitude != null && longitude != null) {
            return String.format("%.4f, %.4f", latitude, longitude);
        }
        return "N/A";
    }

    // Validation method
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                latitude != null && longitude != null;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", x=" + x +
                ", y=" + y +
                ", type='" + type + '\'' +
                '}';
    }

    // equals and hashCode based on name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return name != null && name.equals(location.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
