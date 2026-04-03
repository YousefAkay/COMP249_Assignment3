// -----------------------------------------------------
// Assignment 3
// Class: Hostel
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import exceptions.InvalidAccommodationDataException;

/** Core Hostel entity in the SmartTravel system */
public class Hostel extends Accommodation {

    private int sharedRoomCapacity;

    /** Builds a default hostel with a typical shared-room capacity. */
    public Hostel() {
        super();
        this.sharedRoomCapacity = 4;
    }

    /** Builds a hostel using validated user input. */
    public Hostel(String name, String location, double pricePerNight, int sharedRoomCapacity)
            throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setSharedRoomCapacity(sharedRoomCapacity);
        validateHostelPrice(pricePerNight);
    }

    /** Builds a hostel from file data using an explicit accommodation ID. */
    public Hostel(String accommodationId, String name, String location, double pricePerNight, int sharedRoomCapacity)
            throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        setSharedRoomCapacity(sharedRoomCapacity);
        validateHostelPrice(pricePerNight);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Hostel(Hostel otherHostel) {
        super(otherHostel);
        this.sharedRoomCapacity = otherHostel.sharedRoomCapacity;
    }

    /** Enforces the hostel business rule for maximum nightly price. */
    private void validateHostelPrice(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight > 150.0) {
            throw new InvalidAccommodationDataException("Hostel pricePerNight must be <= 150.");
        }
    }

    public int getSharedRoomCapacity() {
        return sharedRoomCapacity;
    }

    /** Enforces that at least one person can fit in the room. */
    public void setSharedRoomCapacity(int sharedRoomCapacity) throws InvalidAccommodationDataException {
        if (sharedRoomCapacity < 1) {
            throw new InvalidAccommodationDataException("Hostel sharedRoomCapacity must be >= 1.");
        }
        this.sharedRoomCapacity = sharedRoomCapacity;
    }

    /** Applies the hostel discount to the total stay cost. */
    @Override
    public double calculateCost(int numberOfDays) {
        double totalCost = numberOfDays * getPricePerNight();
        return totalCost * 0.85;
    }

    /** Compares the meaningful state of two hostel objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (!(otherObject instanceof Hostel)) return false;

        Hostel otherHostel = (Hostel) otherObject;
        return sharedRoomCapacity == otherHostel.sharedRoomCapacity;
    }

    /** Returns a clean text summary for menus, testing, and logging using super. */
    @Override
    public String toString() {
        return super.toString().replace("Accommodation", "Hostel").replace("}", "") +
                ", sharedRoomCapacity=" + sharedRoomCapacity + "}";
    }

    /** Serializes the hostel using the current A2-compatible CSV format. */
    @Override
    public String toCsvRow() {
        return "HOSTEL;" + getAccommodationId() + ";" + getName() + ";" +
                getLocation() + ";" + getPricePerNight() + ";" + sharedRoomCapacity;
    }

    @Override
    public String getType() {
        return "HOSTEL";
    }
}
