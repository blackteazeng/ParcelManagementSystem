package model;

/**
 * Customer class - blueprint for customer objects
 */
public class Customer {
    private int seqNo;
    private String name;
    private String parcelID;

    /**
     * Constructor for Customer
     * @param seqNo Sequence number in queue
     * @param name Name of customer
     * @param parcelID ID of parcel to collect
     */
    public Customer(int seqNo, String name, String parcelID) {
        this.seqNo = seqNo;
        this.name = name;
        this.parcelID = parcelID;
    }

    /**
     * Get sequence number
     * @return Sequence number
     */
    public int getSeqNo() {
        return seqNo;
    }

    /**
     * Set sequence number
     * @param seqNo New sequence number
     */
    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    /**
     * Get name of customer
     * @return Name of customer
     */
    public String getName() {
        return name;
    }

    /**
     * Get parcel ID
     * @return Parcel ID
     */
    public String getParcelID() {
        return parcelID;
    }

    /**
     * String representation of customer
     * @return String representation
     */
    @Override
    public String toString() {
        return seqNo + "\t" + name + "\t" + parcelID;
    }
} 