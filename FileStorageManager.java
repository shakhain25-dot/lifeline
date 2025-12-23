package com.lifeline.storage;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileStorageManager {

    // Data directory path
    private static final String DATA_DIR = "data/";
    private static final String CSV_DELIMITER = ",";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // File names constants
    public static final String EMERGENCY_FILE = "emergencies.txt";
    public static final String AMBULANCE_FILE = "ambulances.txt";
    public static final String DISPATCH_FILE = "dispatches.txt";

    public static void initializeStorage() {
        try {
            Path path = Paths.get(DATA_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✓ Created data directory: " + DATA_DIR);
            } else {
                System.out.println("✓ Data directory exists: " + DATA_DIR);
            }

            // Create default files if they don't exist
            createFileIfNotExists(EMERGENCY_FILE);
            createFileIfNotExists(AMBULANCE_FILE);
            createFileIfNotExists(DISPATCH_FILE);

        } catch (IOException e) {
            System.err.println("✗ Error creating data directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createFileIfNotExists(String filename) throws IOException {
        Path path = Paths.get(DATA_DIR + filename);
        if (!Files.exists(path)) {
            Files.createFile(path);
            System.out.println("✓ Created file: " + filename);
        }
    }

    // ==================== BASIC FILE OPERATIONS ====================

    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try {
            Path path = Paths.get(DATA_DIR + filename);

            if (Files.exists(path)) {
                lines = Files.readAllLines(path);
                System.out.println("✓ Read " + lines.size() + " lines from " + filename);
            } else {
                System.out.println("⚠ File does not exist: " + filename + " - Creating empty file");
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.println("✗ Error reading file " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    public static void writeFile(String filename, List<String> lines) {
        try {
            Path path = Paths.get(DATA_DIR + filename);
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("✓ Wrote " + lines.size() + " lines to " + filename);
        } catch (IOException e) {
            System.err.println("✗ Error writing file " + filename + ": " + e.getMessage());
        }
    }

    public static void appendLine(String filename, String line) {
        try {
            Path path = Paths.get(DATA_DIR + filename);

            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            System.out.println("✓ Appended line to " + filename);
        } catch (IOException e) {
            System.err.println("✗ Error appending to file " + filename + ": " + e.getMessage());
        }
    }

    // ==================== CSV OPERATIONS ====================

    public static String[] parseCSVLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        return line.split(CSV_DELIMITER, -1);
    }

    public static String createCSVLine(String... fields) {
        return String.join(CSV_DELIMITER, fields);
    }

    public static List<String[]> readCSV(String filename) {
        List<String> lines = readFile(filename);
        return lines.stream()
                .filter(line -> !line.trim().isEmpty())
                .map(FileStorageManager::parseCSVLine)
                .collect(Collectors.toList());
    }

    public static void writeCSV(String filename, List<String[]> records) {
        List<String> lines = records.stream()
                .map(FileStorageManager::createCSVLine)
                .collect(Collectors.toList());
        writeFile(filename, lines);
    }

    // ==================== SEARCH & FILTER OPERATIONS ====================

    public static List<String> searchLines(String filename, String searchText) {
        List<String> lines = readFile(filename);
        return lines.stream()
                .filter(line -> line.toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String[]> searchByField(String filename, int fieldIndex, String value) {
        List<String[]> records = readCSV(filename);
        return records.stream()
                .filter(record -> record.length > fieldIndex &&
                        record[fieldIndex].equalsIgnoreCase(value))
                .collect(Collectors.toList());
    }

    public static List<String[]> filterRecords(String filename, RecordFilter filter) {
        List<String[]> records = readCSV(filename);
        return records.stream()
                .filter(filter::matches)
                .collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface RecordFilter {
        boolean matches(String[] record);
    }

    // ==================== UPDATE & DELETE OPERATIONS ====================

    public static String[] findById(String filename, String id) {
        List<String[]> records = readCSV(filename);
        return records.stream()
                .filter(record -> record.length > 0 && record[0].equals(id))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateById(String filename, String id, String[] newRecord) {
        List<String[]> records = readCSV(filename);
        boolean updated = false;

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).length > 0 && records.get(i)[0].equals(id)) {
                records.set(i, newRecord);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeCSV(filename, records);
            System.out.println("✓ Updated record with ID: " + id);
        } else {
            System.out.println("⚠ Record not found with ID: " + id);
        }

        return updated;
    }

    public static boolean deleteById(String filename, String id) {
        List<String[]> records = readCSV(filename);
        int sizeBefore = records.size();

        records = records.stream()
                .filter(record -> record.length == 0 || !record[0].equals(id))
                .collect(Collectors.toList());

        boolean deleted = records.size() < sizeBefore;

        if (deleted) {
            writeCSV(filename, records);
            System.out.println("✓ Deleted record with ID: " + id);
        } else {
            System.out.println("⚠ Record not found with ID: " + id);
        }

        return deleted;
    }

    public static int deleteByCondition(String filename, RecordFilter filter) {
        List<String[]> records = readCSV(filename);
        int sizeBefore = records.size();

        records = records.stream()
                .filter(record -> !filter.matches(record))
                .collect(Collectors.toList());

        int deleted = sizeBefore - records.size();

        if (deleted > 0) {
            writeCSV(filename, records);
            System.out.println("✓ Deleted " + deleted + " records");
        }

        return deleted;
    }

    // ==================== ID GENERATION ====================

    public static long getNextNumericId(String filename) {
        List<String[]> records = readCSV(filename);
        long maxId = 0;

        for (String[] record : records) {
            if (record.length > 0) {
                try {
                    long id = Long.parseLong(record[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    // Skip non-numeric IDs
                }
            }
        }

        return maxId + 1;
    }

    public static String getNextAmbulanceId(String filename) {
        List<String[]> records = readCSV(filename);
        int maxNum = 0;

        for (String[] record : records) {
            if (record.length > 0 && record[0].startsWith("AMB-")) {
                try {
                    int num = Integer.parseInt(record[0].substring(4));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }

        return String.format("AMB-%03d", maxNum + 1);
    }

    public static boolean idExists(String filename, String id) {
        return findById(filename, id) != null;
    }

    // ==================== SORTING OPERATIONS ====================

    public static void sortByField(String filename, int fieldIndex, boolean ascending) {
        List<String[]> records = readCSV(filename);

        records.sort((a, b) -> {
            if (a.length <= fieldIndex || b.length <= fieldIndex) {
                return 0;
            }
            int comparison = a[fieldIndex].compareToIgnoreCase(b[fieldIndex]);
            return ascending ? comparison : -comparison;
        });

        writeCSV(filename, records);
        System.out.println("✓ Sorted records by field " + fieldIndex);
    }

    // ==================== UTILITY OPERATIONS ====================

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_FORMAT);
    }

    public static LocalDateTime parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.parse(timestamp, DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    public static int countRecords(String filename) {
        return readCSV(filename).size();
    }

    public static boolean fileExists(String filename) {
        Path path = Paths.get(DATA_DIR + filename);
        return Files.exists(path);
    }

    public static boolean deleteFile(String filename) {
        try {
            Path path = Paths.get(DATA_DIR + filename);
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("✓ Deleted file: " + filename);
                return true;
            } else {
                System.out.println("⚠ File does not exist: " + filename);
                return false;
            }
        } catch (IOException e) {
            System.err.println("✗ Error deleting file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    public static void clearFile(String filename) {
        try {
            Path path = Paths.get(DATA_DIR + filename);
            Files.write(path, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("✓ Cleared file: " + filename);
        } catch (IOException e) {
            System.err.println("✗ Error clearing file " + filename + ": " + e.getMessage());
        }
    }

    public static int getLineCount(String filename) {
        try {
            Path path = Paths.get(DATA_DIR + filename);
            if (Files.exists(path)) {
                return (int) Files.lines(path).count();
            }
        } catch (IOException e) {
            System.err.println("✗ Error counting lines in " + filename + ": " + e.getMessage());
        }
        return 0;
    }

    public static boolean backupFile(String filename) {
        try {
            Path source = Paths.get(DATA_DIR + filename);
            Path backup = Paths.get(DATA_DIR + filename + ".backup");

            if (Files.exists(source)) {
                Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✓ Created backup: " + filename + ".backup");
                return true;
            } else {
                System.out.println("⚠ Cannot backup - file does not exist: " + filename);
                return false;
            }
        } catch (IOException e) {
            System.err.println("✗ Error backing up file " + filename + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean restoreFromBackup(String filename) {
        try {
            Path backup = Paths.get(DATA_DIR + filename + ".backup");
            Path destination = Paths.get(DATA_DIR + filename);

            if (Files.exists(backup)) {
                Files.copy(backup, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✓ Restored from backup: " + filename);
                return true;
            } else {
                System.out.println("⚠ Cannot restore - backup does not exist: " + filename + ".backup");
                return false;
            }
        } catch (IOException e) {
            System.err.println("✗ Error restoring from backup " + filename + ": " + e.getMessage());
            return false;
        }
    }

    public static String getDataDirectory() {
        return DATA_DIR;
    }

    public static List<String> listFiles() {
        List<String> files = new ArrayList<>();
        try {
            Path dir = Paths.get(DATA_DIR);
            if (Files.exists(dir)) {
                Files.list(dir)
                        .filter(Files::isRegularFile)
                        .forEach(path -> files.add(path.getFileName().toString()));
            }
        } catch (IOException e) {
            System.err.println("✗ Error listing files: " + e.getMessage());
        }
        return files;
    }

    public static long getFileSize(String filename) {
        try {
            Path path = Paths.get(DATA_DIR + filename);
            if (Files.exists(path)) {
                return Files.size(path);
            }
        } catch (IOException e) {
            System.err.println("✗ Error getting file size: " + e.getMessage());
        }
        return 0;
    }

    public static void exportToJSON(String filename, String outputFile) {
        List<String[]> records = readCSV(filename);
        List<String> jsonLines = new ArrayList<>();
        jsonLines.add("[");
        for (int i = 0; i < records.size(); i++) {
            String[] record = records.get(i);
            StringBuilder json = new StringBuilder("  {");
            for (int j = 0; j < record.length; j++) {
                json.append("\"field").append(j).append("\": \"")
                        .append(record[j].replace("\"", "\\\"")).append("\"");
                if (j < record.length - 1) json.append(", ");
            }
            json.append("}");
            if (i < records.size() - 1) json.append(",");
            jsonLines.add(json.toString());
        }
        jsonLines.add("]");
        writeFile(outputFile, jsonLines);
        System.out.println("✓ Exported to JSON: " + outputFile);
    }

    public static void printFileStats(String filename) {
        System.out.println("\n┌── File Statistics: " + filename + " ───");
        System.out.println("Records: " + countRecords(filename));
        System.out.println("Lines: " + getLineCount(filename));
        System.out.println("Size: " + getFileSize(filename) + " bytes");
        System.out.println("Exists: " + (fileExists(filename) ? "Yes" : "No"));
        System.out.println("└────────────────────────────────────\n");
    }
}