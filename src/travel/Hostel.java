// -----------------------------------------------------
// Assignment 2 (based on Assignment 1 code)
// Class: Hostel
// Written by: Yousef Yousef (40299095) , Hamza Shadeed(40341727)
// -----------------------------------------------------

package travel;

import exceptions.InvalidAccommodationDataException;

/** Core Hostel entity in the SmartTravel system */
public class Hostel extends Accommodation {

    private int sharedRoomCapacity;

    /** Default constructor */
    public Hostel() {
        super();
        this.sharedRoomCapacity = 4;
    }

    /** constructor */
    public Hostel(String name, String location, double pricePerNight, int sharedRoomCapacity)
            throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setSharedRoomCapacity(sharedRoomCapacity);
        // A2 rule: Hostel price <= 150
        validateHostelPrice(pricePerNight);
    }

    /** load-time constructor (explicit ID) */
    public Hostel(String accommodationId, String name, String location, double pricePerNight, int sharedRoomCapacity)
            throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        setSharedRoomCapacity(sharedRoomCapacity);
        validateHostelPrice(pricePerNight);
    }

    /** Copy constructor */
    public Hostel(Hostel other) {
        super(other);
        this.sharedRoomCapacity = other.sharedRoomCapacity;
    }

    private void validateHostelPrice(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight > 150.0) {
            throw new InvalidAccommodationDataException("Hostel pricePerNight must be <= 150.");
        }
    }

    public int getSharedRoomCapacity() {
        return sharedRoomCapacity;
    }

    public void setSharedRoomCapacity(int sharedRoomCapacity) throws InvalidAccommodationDataException {
        if (sharedRoomCapacity < 1) {
            throw new InvalidAccommodationDataException("Hostel sharedRoomCapacity must be >= 1.");
        }
        this.sharedRoomCapacity = sharedRoomCapacity;
    }

    /** multiply by 0.85 */
    @Override
    public double calculateCost(int numberOfDays) {
        double nights = numberOfDays;
        double total = nights * getPricePerNight();

        total = total * 0.85;

        return total;
    }

    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth)) return false;
        if (!(oth instanceof Hostel)) return false;

        Hostel other = (Hostel) oth;
        return sharedRoomCapacity == other.sharedRoomCapacity;
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Hostel{accommodationId='" + getAccommodationId() + "', name='" + getName() +
                "', location='" + getLocation() + "', pricePerNight=" + getPricePerNight() +
                ", sharedRoomCapacity=" + sharedRoomCapacity + "}";
    }

    @Override
    public String getType() { return "Hostel"; }

    // EQUALS
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Hostel other = (Hostel) obj;
        return sharedRoomCapacity == other.sharedRoomCapacity;
    }
}