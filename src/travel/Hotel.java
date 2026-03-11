// -----------------------------------------------------
// Assignment 2
// Class: Hotel
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import exceptions.InvalidAccommodationDataException;

/** Core Hotel entity in the SmartTravel system */
public class Hotel extends Accommodation {

    private int stars;

    /** Default constructor */
    public Hotel() {
        super();
        this.stars = 3;
    }

    /** constructor */
    public Hotel(String name, String location, double pricePerNight, int stars) throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setStars(stars);
    }

    /** load-time constructor (explicit ID) */
    public Hotel(String accommodationId, String name, String location, double pricePerNight, int stars)
            throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        setStars(stars);
    }

    /** Copy constructor */
    public Hotel(Hotel other) {
        super(other);
        this.stars = other.stars;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) throws InvalidAccommodationDataException {
        // A2 rule: stars 1–5
        if (stars < 1 || stars > 5) {
            throw new InvalidAccommodationDataException("Hotel stars must be between 1 and 5.");
        }
        this.stars = stars;
    }

    /** base nights cost + 10% surcharge */
    @Override
    public double calculateCost(int numberOfDays) {
        double nights = numberOfDays;
        double total = nights * getPricePerNight();
        total = total * 1.10; // 10% surcharge
        return total;
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Hotel{accommodationId='" + getAccommodationId() + "', name='" + getName() +
                "', location='" + getLocation() + "', pricePerNight=" + getPricePerNight() +
                ", stars=" + stars + "}";
    }
}