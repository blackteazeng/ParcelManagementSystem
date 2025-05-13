package collection;

import model.Parcel;
import util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ParcelMap class - uses a map data structure to store parcel objects
 */
public class ParcelMap {
    private Map<String, Parcel> parcels;
    private Log log;

    /**
     * Constructor for ParcelMap
     */
    public ParcelMap() {
        parcels = new HashMap<>();
        log = Log.getInstance();
    }

    /**
     * Load parcels from file
     * @param filename Name of file to load
     * @return true if successful, false otherwise
     */
    public boolean loadParcelsFromFile(String filename) {
        log.addLog("Starting to load parcels from file: " + filename);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            
            // Read and check the file header first
            String headerLine1 = reader.readLine();
            log.addLog("Header line 1: " + headerLine1);
            
            if (headerLine1 == null || !headerLine1.trim().equals("PARCELS")) {
                log.addLog("Error: First line should be 'PARCELS'");
                return false;
            }
            
            String headerLine2 = reader.readLine();
            log.addLog("Header line 2: " + headerLine2);
            
            if (headerLine2 == null) {
                log.addLog("Error: Missing column headers");
                return false;
            }
            
            int lineCount = 2; 
            
            Pattern dimensionPattern = Pattern.compile("(\\d+)\\s*x\\s*(\\d+)\\s*x\\s*(\\d+)");
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                log.addLog("Processing line " + lineCount + ": " + line);
                
                if (line.trim().isEmpty()) {
                    log.addLog("Skipping empty line");
                    continue;
                }
                
                try {
                    String[] parts = line.split("\\s+");
                    
                    if (parts.length < 4) {
                        log.addLog("Line " + lineCount + " has insufficient data: " + parts.length + " parts");
                        continue;
                    }
                    
                    String parcelID = parts[0];
                    int daysInDepot = Integer.parseInt(parts[1]);
                    double weight = Double.parseDouble(parts[2]);
                    
                    StringBuilder dimensionStr = new StringBuilder();
                    for (int i = 3; i < parts.length; i++) {
                        dimensionStr.append(parts[i]).append(" ");
                    }
                    
                    String dimensions = dimensionStr.toString().trim();
                    log.addLog("Dimensions string: " + dimensions);
                    
                    Matcher matcher = dimensionPattern.matcher(dimensions);
                    
                    if (matcher.find()) {
                        int length = Integer.parseInt(matcher.group(1));
                        int width = Integer.parseInt(matcher.group(2));
                        int height = Integer.parseInt(matcher.group(3));
                        
                        log.addLog("Successfully parsed dimensions: " + length + "x" + width + "x" + height);
                        
                        Parcel parcel = new Parcel(parcelID, daysInDepot, weight, length, width, height);
                        addParcel(parcel);
                    } else {
                        log.addLog("Failed to parse dimensions from: " + dimensions);
                    }
                } catch (NumberFormatException e) {
                    log.addLog("Error parsing numbers in line " + lineCount + ": " + e.getMessage());
                } catch (Exception e) {
                    log.addLog("Unexpected error processing line " + lineCount + ": " + e.getMessage());
                }
            }
            
            int loadedCount = parcels.size();
            log.addLog("Loaded " + loadedCount + " parcels from file");
            
            return loadedCount > 0;
        } catch (IOException e) {
            log.addLog("IO Error loading parcels: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add a parcel to the map
     * @param parcel Parcel to add
     */
    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelID(), parcel);
        log.addLog("Parcel added: " + parcel.getParcelID());
    }

    /**
     * Find a parcel by ID
     * @param parcelID ID of parcel to find
     * @return Parcel if found, null otherwise
     */
    public Parcel findParcelByID(String parcelID) {
        return parcels.get(parcelID);
    }

    /**
     * Mark a parcel as collected
     * @param parcelID ID of parcel to mark
     * @return true if successful, false otherwise
     */
    public boolean markParcelAsCollected(String parcelID) {
        Parcel parcel = findParcelByID(parcelID);
        if (parcel != null) {
            parcel.setCollected(true);
            log.addLog("Parcel marked as collected: " + parcelID);
            return true;
        }
        return false;
    }

    /**
     * Get all parcels
     * @return List of all parcels
     */
    public List<Parcel> getAllParcels() {
        return new ArrayList<>(parcels.values());
    }

    /**
     * Get all uncollected parcels
     * @return List of uncollected parcels
     */
    public List<Parcel> getUncollectedParcels() {
        return parcels.values().stream()
                .filter(parcel -> !parcel.isCollected())
                .collect(Collectors.toList());
    }

    /**
     * Get all collected parcels
     * @return List of collected parcels
     */
    public List<Parcel> getCollectedParcels() {
        return parcels.values().stream()
                .filter(Parcel::isCollected)
                .collect(Collectors.toList());
    }

    /**
     * Count parcels in depot more than n days
     * @param days Number of days threshold
     * @return Count of parcels
     */
    public int countParcelsInDepotMoreThanDays(int days) {
        return (int) parcels.values().stream()
                .filter(parcel -> parcel.getDaysInDepot() > days)
                .count();
    }
} 