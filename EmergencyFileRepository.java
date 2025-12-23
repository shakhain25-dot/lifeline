package com.lifeline.repository;

import com.lifeline.model.EmergencyRequest;
import com.lifeline.storage.FileStorageManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmergencyFileRepository {

    private static final String FILENAME = "emergency_requests.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Long currentId = 1L;

    public EmergencyFileRepository() {
        List<EmergencyRequest> requests = findAll();
        if (!requests.isEmpty()) {
            currentId = requests.stream()
                    .mapToLong(EmergencyRequest::getId)
                    .max()
                    .orElse(0L) + 1;
        }
    }

    public List<EmergencyRequest> findAll() {
        List<EmergencyRequest> requests = new ArrayList<>();
        List<String> lines = FileStorageManager.readFile(FILENAME);

        for (String line : lines) {
            if (line.trim().isEmpty())
                continue;
            requests.add(parseRequest(line));
        }
        return requests;
    }

    private EmergencyRequest parseRequest(String line) {
        String[] parts = line.split("\\|");
        EmergencyRequest req = new EmergencyRequest();
        req.setId(Long.parseLong(parts[0]));
        req.setCallerName(parts[1]);
        req.setPhoneNumber(parts[2]);
        req.setLocation(parts[3]);
        req.setPriority(parts[4]);
        req.setDescription(parts[5]);
        req.setStatus(parts[6]);
        req.setTimestamp(LocalDateTime.parse(parts[7], FORMATTER));
        return req;
    }

    private String toLine(EmergencyRequest req) {
        return String.join("|",
                String.valueOf(req.getId()),
                req.getCallerName(),
                req.getPhoneNumber() != null ? req.getPhoneNumber() : "",
                req.getLocation(),
                req.getPriority(),
                req.getDescription(),
                req.getStatus(),
                req.getTimestamp().format(FORMATTER));
    }

    public EmergencyRequest save(EmergencyRequest request) {
        List<EmergencyRequest> requests = findAll();

        if (request.getId() == null) {
            request.setId(currentId++);
            request.setTimestamp(LocalDateTime.now());
            request.setStatus("PENDING");
        }

        requests.removeIf(req -> req.getId().equals(request.getId()));
        requests.add(request);

        List<String> lines = new ArrayList<>();
        for (EmergencyRequest req : requests) {
            lines.add(toLine(req));
        }
        FileStorageManager.writeFile(FILENAME, lines);

        return request;
    }

    public List<EmergencyRequest> findByStatus(String status) {
        List<EmergencyRequest> all = findAll();
        List<EmergencyRequest> filtered = new ArrayList<>();
        for (EmergencyRequest req : all) {
            if (req.getStatus().equals(status)) {
                filtered.add(req);
            }
        }
        return filtered;
    }
}
