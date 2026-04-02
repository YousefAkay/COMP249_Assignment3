// -----------------------------------------------------
// Assignment 2
// Class: Hotel
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import exceptions.InvalidAccommodationDataException;

/** Core Hotel entity in the SmartTravel system */
public class Hotel extends Accommodation {

    private int stars;

    /** Builds a default hotel with a middle-range star value. */
    public Hotel() {
        super();
        this.stars = 3;
    }

    /** Builds a hotel using validated user input. */
    public Hotel(String name, String location, double pricePerNight, int stars)
            throws InvalidAccommodationDataException {
        super(name, location, pricePerNight);
        setStars(stars);
    }

    /** Builds a hotel from file data using an explicit accommodation ID. */
    public Hotel(String accommodationId, String name, String location, double pricePerNight, int stars)
            throws InvalidAccommodationDataException {
        super(accommodationId, name, location, pricePerNight);
        setStars(stars);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Hotel(Hotel otherHotel) {
        super(otherHotel);
        this.stars = otherHotel.stars;
    }

    public int getStars() {
        return stars;
    }

    /** Enforces the rule that hotel stars must stay between 1 and 5. */
    public void setStars(int stars) throws InvalidAccommodationDataException {
        if (stars < 1 || stars > 5) {
            throw new InvalidAccommodationDataException("Hotel stars must be between 1 and 5.");
        }
        this.stars = stars;
    }

    /** Multiplies nightly cost by a bonus that depends on the star rating. */
    @Override
    public double calculateCost(int numberOfDays) {
        double starMultiplier;

        if (stars == 1) {
            starMultiplier = 1.0;
        } else if (stars == 2) {
            starMultiplier = 1.2;
        } else if (stars == 3) {
            starMultiplier = 1.5;
        } else if (stars == 4) {
            starMultiplier = 1.8;
        } else {
            starMultiplier = 2.2;
        }

        return getPricePerNight() * numberOfDays * starMultiplier;
    }

    /** Compares the meaningful state of two hotel objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (!(otherObject instanceof Hotel)) return false;

        Hotel otherHotel = (Hotel) otherObject;
        return stars == otherHotel.stars;
    }

    /** Returns a clean text summary for menus, testing, and logging using super. */
    @Override
    public String toString() {
        return super.toString().replace("Accommodation", "Hotel").replace("}", "") +
                ", stars=" + stars + "}";
    }

    @Override
    public String getType() {
        return "HOTEL";
    }
}