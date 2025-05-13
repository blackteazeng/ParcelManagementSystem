package model;

/**
 * Parcel class - contains attributes and methods of a parcel
 */
public class Parcel {
    private String parcelID;
    private int daysInDepot;
    private double weight;
    private int length;
    private int width;
    private int height;
    private boolean collected;

    /**
     * Constructor for Parcel
     * @param parcelID Unique ID of the parcel
     * @param daysInDepot Number of days in depot
     * @param weight Weight of parcel
     * @param length Length of parcel
     * @param width Width of parcel
     * @param height Height of parcel
     */
    public Parcel(String parcelID, int daysInDepot, double weight, int length, int width, int height) {
        this.parcelID = parcelID;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.collected = false;
    }

    /**
     * Get the parcel ID
     * @return The parcel ID
     */
    public String getParcelID() {
        return parcelID;
    }

    /**
     * Get the number of days in depot
     * @return Days in depot
     */
    public int getDaysInDepot() {
        return daysInDepot;
    }

    /**
     * Get the weight of the parcel
     * @return Weight of parcel
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Get the length of the parcel
     * @return Length of parcel
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the width of the parcel
     * @return Width of parcel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the parcel
     * @return Height of parcel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Check if the parcel is collected
     * @return true if collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Set the parcel as collected
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /**
     * Get dimensions as a string
     * @return String representation of dimensions
     */
    public String getDimensions() {
        return length + " x " + width + " x " + height;
    }

    /**
     * Get a string representation of the parcel
     * @return String representation of the parcel
     */
    @Override
    public String toString() {
        return parcelID + "\t" + daysInDepot + "\t" + weight + "\t" + getDimensions();
    }
} 