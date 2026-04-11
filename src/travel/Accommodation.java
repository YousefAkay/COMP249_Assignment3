// -----------------------------------------------------
// Assignment 3
// Class: Accommodation
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import interfaces.CsvPersistable;
import interfaces.Identifiable;
import exceptions.InvalidAccommodationDataException;

/** Core Accommodation entity in the SmartTravel system */
public abstract class Accommodation implements Identifiable, CsvPersistable, Comparable<Accommodation> {

    private static int nextId = 4001;

    private String accommodationId;
    private String name;
    private String location;
    private double pricePerNight;

    /** Generates the next sequential ID for accommodation objects. */
    private static String generateId() {
        return "A" + nextId++;
    }

    public static void resetIdCounter() {
        nextId = 4001;
    }

    /** Updates the next ID after loading records so new IDs do not collide. */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        if (!loadedId.startsWith("A")) return;

        try {
            int loadedNumber = Integer.parseInt(loadedId.substring(1));
            if (loadedNumber >= nextId) {
                nextId = loadedNumber + 1;
            }
        } catch (NumberFormatException ignore) {
        }
    }

    /** Builds a default accommodation object with placeholder values. */
    public Accommodation() {
        this.accommodationId = generateId();
        this.name = "Unknown";
        this.location = "Unknown";
        this.pricePerNight = 1.0;
    }

    /** Builds an accommodation object using validated user values. */
    public Accommodation(String name, String location, double pricePerNight)
            throws InvalidAccommodationDataException {
        this.accommodationId = generateId();
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
    }

    /** Builds an accommodation object from file data using an explicit ID. */
    protected Accommodation(String accommodationId, String name, String location, double pricePerNight)
            throws InvalidAccommodationDataException {
        setAccommodationIdForLoad(accommodationId);
        setName(name);
        setLocation(location);
        setPricePerNight(pricePerNight);
        syncNextIdFromLoadedId(accommodationId);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Accommodation(Accommodation otherAccommodation) {
        this.accommodationId = generateId();
        this.name = otherAccommodation.name;
        this.location = otherAccommodation.location;
        this.pricePerNight = otherAccommodation.pricePerNight;
    }

    /** Treats null or whitespace-only text as blank input. */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Validates a loaded ID before storing it on the object. */
    private void setAccommodationIdForLoad(String accommodationId) throws InvalidAccommodationDataException {
        if (isBlank(accommodationId) || !accommodationId.startsWith("A")) {
            throw new InvalidAccommodationDataException("Invalid accommodationId: " + accommodationId);
        }
        this.accommodationId = accommodationId.trim();
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    @Override
    public String getId() {
        return getAccommodationId();
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

    /** Enforces that the nightly price must stay positive. */
    public void setPricePerNight(double pricePerNight) throws InvalidAccommodationDataException {
        if (pricePerNight <= 0) {
            throw new InvalidAccommodationDataException("pricePerNight must be > 0.");
        }
        this.pricePerNight = pricePerNight;
    }

    /** Lets each subclass define its own total-cost formula. */
    public abstract double calculateCost(int numberOfDays);

    /** Compares the meaningful state of two accommodation objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (this.getClass() != otherObject.getClass()) return false;

        Accommodation otherAccommodation = (Accommodation) otherObject;

        if (name == null) {
            if (otherAccommodation.name != null) return false;
        } else if (!name.equals(otherAccommodation.name)) {
            return false;
        }

        if (location == null) {
            if (otherAccommodation.location != null) return false;
        } else if (!location.equals(otherAccommodation.location)) {
            return false;
        }

        return Double.compare(pricePerNight, otherAccommodation.pricePerNight) == 0;
    }

    public abstract String getType();

    /** Returns a clean text summary for menus, testing, and logging. */
    @Override
    public String toString() {
        return "Accommodation{accommodationId='" + accommodationId + "', name='" + name +
                "', location='" + location + "', pricePerNight=" + pricePerNight + "}";
    }

    /** Reconstructs one accommodation object by dispatching on the CSV type prefix. */
    public static Accommodation fromCsvRow(String csvRow) throws InvalidAccommodationDataException {
        if (csvRow == null) {
            throw new InvalidAccommodationDataException("Accommodation CSV row cannot be null.");
        }

        String[] tokens = csvRow.split(";", -1);
        if (tokens.length < 2) {
            throw new InvalidAccommodationDataException("Bad accommodation CSV row: " + csvRow);
        }

        String typePrefix = tokens[0].trim().toUpperCase();

        if ("HOTEL".equals(typePrefix)) {
            if (tokens.length != 6) {
                throw new InvalidAccommodationDataException("Bad HOTEL token count: " + csvRow);
            }

            return new Hotel(
                    tokens[1].trim(),
                    tokens[2].trim(),
                    tokens[3].trim(),
                    Double.parseDouble(tokens[4].trim()),
                    Integer.parseInt(tokens[5].trim())
            );
        }

        if ("HOSTEL".equals(typePrefix)) {
            if (tokens.length != 6) {
                throw new InvalidAccommodationDataException("Bad HOSTEL token count: " + csvRow);
            }

            return new Hostel(
                    tokens[1].trim(),
                    tokens[2].trim(),
                    tokens[3].trim(),
                    Double.parseDouble(tokens[4].trim()),
                    Integer.parseInt(tokens[5].trim())
            );
        }

        throw new InvalidAccommodationDataException("Unknown accommodation type prefix: " + typePrefix);
    }

    /** Applies the A3 natural business ordering: pricePerNight descending. */
    @Override
    public int compareTo(Accommodation otherAccommodation) {
        if (otherAccommodation == null) {
            return -1;
        }

        int priceComparison = Double.compare(otherAccommodation.pricePerNight, pricePerNight);
        if (priceComparison != 0) {
            return priceComparison;
        }

        return accommodationId.compareTo(otherAccommodation.accommodationId);
    }
}
