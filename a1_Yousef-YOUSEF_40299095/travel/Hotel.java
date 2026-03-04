// -----------------------------------------------------
// Assignment 1
// Class: Hotel
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Hotel entity in the SmartTravel system */
public class Hotel extends Accommodation {

    private int starRating;

    // Default constructor
    /** Builds a valid Hotel object (ID handled automatically) */
    public Hotel() {
        super();
        this.starRating = 0;
    }

    // Parameterized constructor
    /** Builds a valid Hotel object (ID handled automatically) */
    public Hotel(String name, String location, double pricePerNight, int starRating) {
        super(name, location, pricePerNight);
        this.starRating = starRating;
    }

    // Copy constructor (new ID, not copied)
    /** Builds a valid Hotel object (ID handled automatically) */
    public Hotel(Hotel other) {
        super(other.getName(), other.getLocation(), other.getPricePerNight());
        this.starRating = other.starRating;
    }

    public int getStarRating() {
        return starRating;
    }
    
    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    
    /** Calculates the hotel cost based on trip duration */
    @Override
    public double calculateCost(int numberOfDays) {
        double nights = numberOfDays;
        double total = nights * getPricePerNight();

        /** 10% surcharge */
        total = total * 1.10;

        return total;
    }


    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Hotel{" +
                "accommodationId='" + getAccommodationId() + "'" +
                ", name='" + getName() + "'" +
                ", location='" + getLocation() + "'" +
                ", pricePerNight=" + getPricePerNight() +
                ", starRating=" + starRating +
                "}";
    }


    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth))
            return false;
        
        Hotel other = (Hotel) oth;
        return this.starRating == other.starRating;
    }
}