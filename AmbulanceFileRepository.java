package com.lifeline.repository;

import com.lifeline.model.Ambulance;
import com.lifeline.storage.FileStorageManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AmbulanceFileRepository {

    private static final String FILENAME = "ambulances.txt";

    public List<Ambulance> findAll() {
        List<Ambulance> ambulances = new ArrayList<>();
        List<String> lines = FileStorageManager.readFile(FILENAME);

        for (String line : lines) {
            if (line.trim().isEmpty())
                continue;
            ambulances.add(parseAmbulance(line));
        }
        return ambulances;
    }

    private Ambulance parseAmbulance(String line) {
        String[] parts = line.split("\\|");
        Ambulance amb = new Ambulance();
        amb.setId(parts[0]);
        amb.setDriverName(parts[1]);
        amb.setCurrentLocation(parts[2]);
        amb.setAvailable(Boolean.parseBoolean(parts[3]));
        amb.setPhoneNumber(parts[4]);
        return amb;
    }

    public List<Ambulance> findByAvailable(Boolean available) {
        List<Ambulance> all = findAll();
        List<Ambulance> filtered = new ArrayList<>();
        for (Ambulance amb : all) {
            if (amb.getAvailable().equals(available)) {
                filtered.add(amb);
            }
        }
        return filtered;
    }
}
