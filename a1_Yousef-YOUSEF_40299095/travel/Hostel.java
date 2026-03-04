// -----------------------------------------------------
// Assignment 1
// Class: Hostel
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Hostel entity in the SmartTravel system */
public class Hostel extends Accommodation {

    private int sharedBedsPerRoom;

    // Default constructor
    /** Builds a valid Hostel object (ID handled automatically) */
    public Hostel() {
        super();
        this.sharedBedsPerRoom = 0;
    }

    // Parameterized constructor
    /** Builds a valid Hostel object (ID handled automatically) */
    public Hostel(String name, String location, double pricePerNight, int sharedBedsPerRoom) {
        super(name, location, pricePerNight);
        this.sharedBedsPerRoom = sharedBedsPerRoom;
    }

    // Copy constructor (new ID, not copied)
    /** Builds a valid Hostel object (ID handled automatically) */
    public Hostel(Hostel other) {
        super(other.getName(), other.getLocation(), other.getPricePerNight());
        this.sharedBedsPerRoom = other.sharedBedsPerRoom;
    }

    public int getSharedBedsPerRoom() {
        return sharedBedsPerRoom;
    }

    public void setSharedBedsPerRoom(int sharedBedsPerRoom) {
        this.sharedBedsPerRoom = sharedBedsPerRoom;
    }


    /** Calculates the hostel cost based on trip duration */
    @Override
    public double calculateCost(int numberOfDays) {
        double nights = numberOfDays;
        double total = nights * getPricePerNight();

        /** 15% surcharge */
        total = total * 0.85;

        return total;
    }


    /** Provides a clean summary for display  */
    @Override
    public String toString() {
        return "Hostel{" +
                "accommodationId='" + getAccommodationId() + "'" +
                ", name='" + getName() + "'" +
                ", location='" + getLocation() + "'" +
                ", pricePerNight=" + getPricePerNight() +
                ", sharedBedsPerRoom=" + sharedBedsPerRoom +
                "}";
    }


    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth))
            return false;
        Hostel other = (Hostel) oth;
        return this.sharedBedsPerRoom == other.sharedBedsPerRoom;
    }
}