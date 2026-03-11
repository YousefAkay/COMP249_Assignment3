// -----------------------------------------------------
// Assignment 2
// Class: Trip
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import client.Client;
import exceptions.InvalidTripDataException;

public class Trip {

    private static int nextId = 2001;

    private String tripId;

    /** store IDs for persistence */
    private String clientId;
    private String accommodationId;     // optional
    private String transportationId;    // optional

    private Client client;
    private Accommodation accommodation;
    private Transportation transportation;

    private String destination;
    private int durationInDays;
    private double basePrice;

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "T" + nextId++;
    }

    /** Sync nextId so IDs don't collide after CSV load */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        if (!loadedId.startsWith("T")) return;
        try {
            int n = Integer.parseInt(loadedId.substring(1));
            if (n >= nextId) nextId = n + 1;
        } catch (NumberFormatException ignore) {
        }
    }

    /** Default constructor (valid) */
    public Trip() {
        this.tripId = generateId();
        this.destination = "Unknown";
        this.durationInDays = 1;
        this.basePrice = 100.0;
        this.clientId = null;
        this.accommodationId = null;
        this.transportationId = null;
    }

    /** constructor using object references (validated) */
    public Trip(Client client, Transportation transportation, Accommodation accommodation,
                String destination, int durationInDays, double basePrice) throws InvalidTripDataException {
        this.tripId = generateId();
        setDestination(destination);
        setDurationInDays(durationInDays);
        setBasePrice(basePrice);

        this.client = client;
        this.transportation = transportation;
        this.accommodation = accommodation;

        /** A2 persistence IDs derived from objects if present */
        this.clientId = (client == null) ? null : client.getClientId();
        this.transportationId = (transportation == null) ? null : transportation.getTransportId();
        this.accommodationId = (accommodation == null) ? null : accommodation.getAccommodationId();

        validateAtLeastOneBooking();
        validateClientIdExistsOrPresent();
    }

    /** constructor from IDs (for CSV load) */
    public Trip(String tripId, String clientId, String accommodationId, String transportationId,
                String destination, int durationInDays, double basePrice) throws InvalidTripDataException {

        setTripIdForLoad(tripId);
        setClientId(clientId);
        setAccommodationId(accommodationId);
        setTransportationId(transportationId);

        setDestination(destination);
        setDurationInDays(durationInDays);
        setBasePrice(basePrice);

        validateAtLeastOneBooking();

        syncNextIdFromLoadedId(tripId);
    }

    /** Copy constructor (new ID) */
    public Trip(Trip other) {
        this.tripId = generateId();

        this.clientId = other.clientId;
        this.accommodationId = other.accommodationId;
        this.transportationId = other.transportationId;

        this.client = other.client;
        this.accommodation = other.accommodation;
        this.transportation = other.transportation;

        this.destination = other.destination;
        this.durationInDays = other.durationInDays;
        this.basePrice = other.basePrice;
    }

    /** Validation helpers */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void setTripIdForLoad(String tripId) throws InvalidTripDataException {
        if (isBlank(tripId) || !tripId.startsWith("T")) {
            throw new InvalidTripDataException("Invalid tripId: " + tripId);
        }
        this.tripId = tripId.trim();
    }

    private void validateAtLeastOneBooking() throws InvalidTripDataException {
        boolean hasAccom = !isBlank(accommodationId) || accommodation != null;
        boolean hasTrans = !isBlank(transportationId) || transportation != null;
        if (!hasAccom && !hasTrans) {
            throw new InvalidTripDataException("Trip must have at least one of: accommodation or transportation.");
        }
    }

    private void validateClientIdExistsOrPresent() throws InvalidTripDataException {
        // In A2, service layer verifies existence in clients[].
        // Here we just ensure the field is present.
        if (isBlank(clientId) && client == null) {
            throw new InvalidTripDataException("Trip must have a clientId (or a Client reference).");
        }
    }

    /** Getters & Setters (validated) */
    public String getTripId() {
        return tripId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) throws InvalidTripDataException {
        if (isBlank(clientId) || !clientId.trim().startsWith("C")) {
            throw new InvalidTripDataException("Invalid clientId: " + clientId);
        }
        this.clientId = clientId.trim();
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(String accommodationId) {
        if (isBlank(accommodationId)) {
            this.accommodationId = null;
        } else {
            this.accommodationId = accommodationId.trim();
        }
    }

    public String getTransportationId() {
        return transportationId;
    }

    public void setTransportationId(String transportationId) {
        if (isBlank(transportationId)) {
            this.transportationId = null;
        } else {
            this.transportationId = transportationId.trim();
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) throws InvalidTripDataException {
        this.client = client;
        this.clientId = (client == null) ? this.clientId : client.getClientId();
        validateClientIdExistsOrPresent();
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) throws InvalidTripDataException {
        this.accommodation = accommodation;
        this.accommodationId = (accommodation == null) ? this.accommodationId : accommodation.getAccommodationId();
        validateAtLeastOneBooking();
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) throws InvalidTripDataException {
        this.transportation = transportation;
        this.transportationId = (transportation == null) ? this.transportationId : transportation.getTransportId();
        validateAtLeastOneBooking();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) throws InvalidTripDataException {
        if (isBlank(destination)) {
            throw new InvalidTripDataException("Destination cannot be empty.");
        }
        this.destination = destination.trim();
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) throws InvalidTripDataException {
        // A2 rule: 1–20
        if (durationInDays < 1 || durationInDays > 20) {
            throw new InvalidTripDataException("Duration must be between 1 and 20 days.");
        }
        this.durationInDays = durationInDays;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) throws InvalidTripDataException {
        // A2 rule: base price >= 100.00
        if (basePrice < 100.0) {
            throw new InvalidTripDataException("Base price must be >= 100.00");
        }
        this.basePrice = basePrice;
    }


    public double calculateTotalCost() {
        double total = basePrice;

        if (transportation != null) {
            total += transportation.calculateCost(durationInDays);
        }
        if (accommodation != null) {
            total += accommodation.calculateCost(durationInDays);
        }

        return total;
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Trip{tripId='" + tripId + "', clientId='" + clientId +
                "', accommodationId='" + accommodationId + "', transportationId='" + transportationId +
                "', destination='" + destination + "', durationInDays=" + durationInDays +
                ", basePrice=" + basePrice + "}";
    }
}