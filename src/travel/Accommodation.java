// -----------------------------------------------------
// Assignment 2
// Class: Accommodation
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import exceptions.InvalidAccommodationDataException;

/** Core Accommodation entity in the SmartTravel system */
public abstract class Accommodation {

    private static int nextId = 4001;

    private String accommodationId;
    private String name;
    private String location;
    private double pricePerNight;

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "A" + nextId++;
    }

    /** Sync nextId so IDs don't collide after CSV load */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        if (!loadedId.startsWith("A")) return;
        try {
            int n = Integer.parseInt(loadedId.substring(1));
            if (n >= nextId) nextId = n + 1;
        } catch (NumberFormatException ignore) {
        }
    }

    /** Default constructor */
    public Accommodation() {
        this.accommodationId = generateId();
        this.name = "Unknown";
        this.location = "Unknown";
        this.pricePerNight = 1.0; // A2 requires > 0
    }

    /** constructor */
    public Accommodation(String name, String location, double pricePerNight) throws InvalidAccommodationDataException {
        this.accommodationId = generateId();
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    /** load-time constructor: explicit ID */
    protected Accommodation(String accommodationId, String name, String location, double pricePerNight)
            throws InvalidAccommodationDataException {
        setAccommodationIdForLoad(accommodationId);
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
        syncNextIdFromLoadedId(accommodationId);
    }

    /** Copy constructor (new ID generated) */
    public Accommodation(Accommodation other) {
        this.accommodationId = generateId();
        this.name = other.name;
        this.location = other.location;
        this.pricePerNight = other.pricePerNight;
    }

    /** Validation helpers */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void setAccommodationIdForLoad(String id) throws InvalidAccommodationDataException {
        if (isBlank(id) || !id.startsWith("A")) {
            throw new InvalidAccommodationDataException("Invalid accommodationId: " + id);
        }
        this.accommodationId = id.trim();
    }

    /** Getters & Setters */
    public String getAccommodationId() {
        return accommodationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidAccommodationDataException {
        if (isBlank(name)) {
            throw new InvalidAccommodationDataException("Accommodation name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws InvalidAccommodationDataException {
        if (isBlank(location)) {
            throw new InvalidAccommodationDataException("Accommodation location cannot be empty.");
        }
        this.location = location.trim();
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        // A2 rule: price per night > 0
        if (pricePerNight <= 0) {
            throw new InvalidAccommodationDataException("pricePerNight must be > 0.");
        }
        this.pricePerNight = pricePerNight;
    }


    public abstract double calculateCost(int numberOfDays);

    /** Logical equality: meaningful attributes only */
    @Override
    public boolean equals(Object oth) {
        if (oth == null) return false;
        if (this.getClass() != oth.getClass()) return false;

        Accommodation other = (Accommodation) oth;

        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;

        if (location == null) {
            if (other.location != null) return false;
        } else if (!location.equals(other.location)) return false;

        return Double.compare(pricePerNight, other.pricePerNight) == 0;
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Accommodation{accommodationId='" + accommodationId + "', name='" + name +
                "', location='" + location + "', pricePerNight=" + pricePerNight + "}";
    }
}