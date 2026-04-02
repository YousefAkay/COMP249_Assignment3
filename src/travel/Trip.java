// -----------------------------------------------------
// Assignment 2
// Class: Trip
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import client.Client;
import contracts.Billable;
import contracts.CsvPersistable;
import contracts.Identifiable;
import exceptions.InvalidTripDataException;

public class Trip implements Identifiable, Billable, CsvPersistable, Comparable<Trip> {

    private static int nextId = 2001;

    private String tripId;

    public static void resetIdCounter() {
        nextId = 2001;
    }

    /** Store related entity IDs so trips can be saved and loaded through CSV files. */
    private String clientId;
    private String accommodationId;
    private String transportationId;

    private Client client;
    private Accommodation accommodation;
    private Transportation transportation;

    private String destination;
    private int durationInDays;
    private double basePrice;

    /** Generates the next sequential trip ID for new trip objects. */
    private static String generateId() {
        return "T" + nextId++;
    }

    /** Syncs the next ID after loading records so future generated IDs stay unique. */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        if (!loadedId.startsWith("T")) return;
        try {
            int loadedNumber = Integer.parseInt(loadedId.substring(1));
            if (loadedNumber >= nextId) nextId = loadedNumber + 1;
        } catch (NumberFormatException ignoredException) {
        }
    }

    /** Builds a trip directly from already-resolved object references. */
    public Trip(Client client, Transportation transportation, Accommodation accommodation,
                String destination, int durationInDays, double basePrice) throws InvalidTripDataException {
        this.tripId = generateId();
        setDestination(destination);
        setDurationInDays(durationInDays);
        setBasePrice(basePrice);

        this.client = client;
        this.transportation = transportation;
        this.accommodation = accommodation;

        /** Derive persistence IDs from the linked objects whenever they are present. */
        this.clientId = (client == null) ? null : client.getClientId();
        this.transportationId = (transportation == null) ? null : transportation.getTransportId();
        this.accommodationId = (accommodation == null) ? null : accommodation.getAccommodationId();

        validateAtLeastOneBooking();
        validateClientIdExistsOrPresent();
    }

    /** Builds a trip from IDs first, which is mainly useful during CSV loading. */
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

    /** Creates a copy of the trip data but gives the copy a fresh generated ID. */
    public Trip(Trip otherTrip) {
        this.tripId = generateId();

        this.clientId = otherTrip.clientId;
        this.accommodationId = otherTrip.accommodationId;
        this.transportationId = otherTrip.transportationId;

        this.client = otherTrip.client;
        this.accommodation = otherTrip.accommodation;
        this.transportation = otherTrip.transportation;

        this.destination = otherTrip.destination;
        this.durationInDays = otherTrip.durationInDays;
        this.basePrice = otherTrip.basePrice;
    }

    /** Treats null or whitespace-only strings as blank input. */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Validates a loaded trip ID before storing it on the object. */
    private void setTripIdForLoad(String tripId) throws InvalidTripDataException {
        if (isBlank(tripId) || !tripId.startsWith("T")) {
            throw new InvalidTripDataException("Invalid tripId: " + tripId);
        }
        this.tripId = tripId.trim();
    }

    /** Enforces the rule that a trip must contain accommodation, transportation, or both. */
    private void validateAtLeastOneBooking() throws InvalidTripDataException {
        boolean hasAccommodation = !isBlank(accommodationId) || accommodation != null;
        boolean hasTransportation = !isBlank(transportationId) || transportation != null;
        if (!hasAccommodation && !hasTransportation) {
            throw new InvalidTripDataException("Trip must have at least one of: accommodation or transportation.");
        }
    }

    /** Ensures the trip always has either a client ID or a linked client object. */
    private void validateClientIdExistsOrPresent() throws InvalidTripDataException {
        if (isBlank(clientId) && client == null) {
            throw new InvalidTripDataException("Trip must have a clientId (or a Client reference).");
        }
    }

    public String getTripId() {
        return tripId;
    }

    @Override
    public String getId() {
        return getTripId();
    }

    public String getClientId() {
        return clientId;
    }

    /** Validates the client ID format before storing it. */
    public void setClientId(String clientId) throws InvalidTripDataException {
        if (isBlank(clientId) || !clientId.trim().startsWith("C")) {
            throw new InvalidTripDataException("Invalid clientId: " + clientId);
        }
        this.clientId = clientId.trim();
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public String getTransportationId() {
        return transportationId;
    }

    /** Accepts a blank value as no accommodation, otherwise enforces the A-prefix format. */
    public void setAccommodationId(String accommodationId) throws InvalidTripDataException {
        if (isBlank(accommodationId)) {
            this.accommodationId = null;
        } else {
            String trimmedAccommodationId = accommodationId.trim();
            if (!trimmedAccommodationId.startsWith("A")) {
                throw new InvalidTripDataException("Invalid accommodationId: " + accommodationId);
            }
            this.accommodationId = trimmedAccommodationId;
        }
    }

    /** Accepts a blank value as no transportation, otherwise enforces the TR-prefix format. */
    public void setTransportationId(String transportationId) throws InvalidTripDataException {
        if (isBlank(transportationId)) {
            this.transportationId = null;
        } else {
            String trimmedTransportationId = transportationId.trim();
            if (!trimmedTransportationId.startsWith("TR")) {
                throw new InvalidTripDataException("Invalid transportationId: " + transportationId);
            }
            this.transportationId = trimmedTransportationId;
        }
    }

    public Client getClient() {
        return client;
    }

    /** Updates the linked client object and keeps the stored client ID in sync. */
    public void setClient(Client client) throws InvalidTripDataException {
        this.client = client;
        this.clientId = (client == null) ? this.clientId : client.getClientId();
        validateClientIdExistsOrPresent();
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    /** Updates the linked accommodation object and keeps the stored ID in sync. */
    public void setAccommodation(Accommodation accommodation) throws InvalidTripDataException {
        this.accommodation = accommodation;
        this.accommodationId = (accommodation == null) ? null : accommodation.getAccommodationId();
        validateAtLeastOneBooking();
    }

    public Transportation getTransportation() {
        return transportation;
    }

    /** Updates the linked transportation object and keeps the stored ID in sync. */
    public void setTransportation(Transportation transportation) throws InvalidTripDataException {
        this.transportation = transportation;
        this.transportationId = (transportation == null) ? null : transportation.getTransportId();
        validateAtLeastOneBooking();
    }

    public String getDestination() {
        return destination;
    }

    /** Enforces that destination text cannot be blank. */
    public void setDestination(String destination) throws InvalidTripDataException {
        if (isBlank(destination)) {
            throw new InvalidTripDataException("Destination cannot be empty.");
        }
        this.destination = destination.trim();
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    /** Enforces the rule that trip duration must stay between 1 and 20 days. */
    public void setDurationInDays(int durationInDays) throws InvalidTripDataException {
        if (durationInDays < 1 || durationInDays > 20) {
            throw new InvalidTripDataException("Duration must be between 1 and 20 days.");
        }
        this.durationInDays = durationInDays;
    }

    public double getBasePrice() {
        return basePrice;
    }

    /** Enforces the rule that base price must be at least 100.00. */
    public void setBasePrice(double basePrice) throws InvalidTripDataException {
        if (basePrice < 100.0) {
            throw new InvalidTripDataException("Base price must be >= 100.00");
        }
        this.basePrice = basePrice;
    }

    /** Calculates the full trip cost by adding optional transport and accommodation costs. */
    public double calculateTotalCost() {
        double totalCost = basePrice;

        if (transportation != null) {
            totalCost += transportation.calculateCost(durationInDays);
        }
        if (accommodation != null) {
            totalCost += accommodation.calculateCost(durationInDays);
        }

        return totalCost;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (!(otherObject instanceof Trip)) return false;

        Trip otherTrip = (Trip) otherObject;

        if (clientId == null) {
            if (otherTrip.clientId != null) return false;
        } else if (!clientId.equals(otherTrip.clientId)) {
            return false;
        }

        if (accommodationId == null) {
            if (otherTrip.accommodationId != null) return false;
        } else if (!accommodationId.equals(otherTrip.accommodationId)) {
            return false;
        }

        if (transportationId == null) {
            if (otherTrip.transportationId != null) return false;
        } else if (!transportationId.equals(otherTrip.transportationId)) {
            return false;
        }

        if (destination == null) {
            if (otherTrip.destination != null) return false;
        } else if (!destination.equals(otherTrip.destination)) {
            return false;
        }

        return durationInDays == otherTrip.durationInDays &&
                Double.compare(basePrice, otherTrip.basePrice) == 0;
    }

    /** Returns a compact summary of the trip data for display and debugging. */
    @Override
    public String toString() {
        return "Trip{tripId='" + tripId + "', clientId='" + clientId +
                "', accommodationId='" + accommodationId + "', transportationId='" + transportationId +
                "', destination='" + destination + "', durationInDays=" + durationInDays +
                ", basePrice=" + basePrice + "}";
    }

    /** Exposes the trip's current total cost through the Billable contract. */
    @Override
    public double getBillableAmount() {
        return calculateTotalCost();
    }

    /** Serializes the trip using the current A2-compatible CSV format. */
    @Override
    public String toCsvRow() {
        String savedAccommodationId = (accommodationId == null) ? "" : accommodationId;
        String savedTransportationId = (transportationId == null) ? "" : transportationId;

        return tripId + ";" + clientId + ";" + savedAccommodationId + ";" +
                savedTransportationId + ";" + destination + ";" + durationInDays + ";" + basePrice;
    }

    /** Reconstructs one trip from an A2-compatible CSV row. */
    public static Trip fromCsvRow(String csvRow) throws InvalidTripDataException {
        if (csvRow == null) {
            throw new InvalidTripDataException("Trip CSV row cannot be null.");
        }

        String[] tokens = csvRow.split(";");
        if (tokens.length != 7) {
            throw new InvalidTripDataException("Bad TRIP token count: " + csvRow);
        }

        String savedAccommodationId = tokens[2].trim();
        String savedTransportationId = tokens[3].trim();

        if (savedAccommodationId.isEmpty()) {
            savedAccommodationId = null;
        }
        if (savedTransportationId.isEmpty()) {
            savedTransportationId = null;
        }

        return new Trip(
                tokens[0].trim(),
                tokens[1].trim(),
                savedAccommodationId,
                savedTransportationId,
                tokens[4].trim(),
                Integer.parseInt(tokens[5].trim()),
                Double.parseDouble(tokens[6].trim())
        );
    }

    /** Applies the A3 natural business ordering: total cost descending. */
    @Override
    public int compareTo(Trip otherTrip) {
        if (otherTrip == null) {
            return -1;
        }

        int costComparison = Double.compare(otherTrip.calculateTotalCost(), calculateTotalCost());
        if (costComparison != 0) {
            return costComparison;
        }

        return tripId.compareTo(otherTrip.tripId);
    }
}
