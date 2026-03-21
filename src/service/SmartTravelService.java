// -----------------------------------------------------
// Assignment 2
// Class: SmartTravelService
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package service;

import client.Client;
import exceptions.*;
import persistence.*;
import travel.*;

import java.io.IOException;

public class SmartTravelService {

    private final Client[] clients;
    private final Trip[] trips;
    private final Transportation[] transportations;
    private final Accommodation[] accommodations;

    private int clientCount;
    private int tripCount;
    private int transportCount;
    private int accommodationCount;

    /** Stores the shared arrays that hold all system data in memory. */
    public SmartTravelService(Client[] clients, Trip[] trips, Transportation[] transportations, Accommodation[] accommodations) {
        this.clients = clients;
        this.trips = trips;
        this.transportations = transportations;
        this.accommodations = accommodations;
        this.clientCount = 0;
        this.tripCount = 0;
        this.transportCount = 0;
        this.accommodationCount = 0;
    }

    public int getClientCount() { return clientCount; }
    public int getTripCount() { return tripCount; }
    public int getTransportCount() { return transportCount; }
    public int getAccommodationCount() { return accommodationCount; }

    public Client[] getClients() { return clients; }
    public Trip[] getTrips() { return trips; }
    public Transportation[] getTransportations() { return transportations; }
    public Accommodation[] getAccommodations() { return accommodations; }

    /** Adds a client only after checking for null input, duplicates, and free array space. */
    public void addClient(Client client) throws DuplicateEmailException, InvalidClientDataException {
        if (client == null) throw new InvalidClientDataException("Cannot add null client.");
        if (emailExists(client.getEmail())) {
            throw new DuplicateEmailException("Duplicate email: " + client.getEmail());
        }
        if (clientCount >= clients.length) {
            throw new InvalidClientDataException("Client list is full.");
        }
        clients[clientCount++] = client;
    }

    /** Validates the replacement client data first so the object is not left half-updated. */
    public void editClient(String clientId, String firstName, String lastName, String email)
            throws EntityNotFoundException, InvalidClientDataException {

        Client client = findClientById(clientId);

        String newFirstName = (firstName == null) ? null : firstName.trim();
        String newLastName = (lastName == null) ? null : lastName.trim();
        String newEmail = (email == null) ? null : email.trim();

        if (newFirstName == null || newFirstName.isEmpty()) {
            throw new InvalidClientDataException("First name cannot be empty.");
        }
        if (newFirstName.length() > 50) {
            throw new InvalidClientDataException("First name must be <= 50 characters.");
        }

        if (newLastName == null || newLastName.isEmpty()) {
            throw new InvalidClientDataException("Last name cannot be empty.");
        }
        if (newLastName.length() > 50) {
            throw new InvalidClientDataException("Last name must be <= 50 characters.");
        }

        if (newEmail == null || newEmail.isEmpty()) {
            throw new InvalidClientDataException("Email cannot be empty.");
        }
        if (newEmail.length() > 100) {
            throw new InvalidClientDataException("Email must be <= 100 characters.");
        }
        if (newEmail.contains(" ")) {
            throw new InvalidClientDataException("Email cannot contain spaces.");
        }
        if (!(newEmail.contains("@") && newEmail.contains("."))) {
            throw new InvalidClientDataException("Email must contain '@' and '.'.");
        }

        if (!client.getEmail().equalsIgnoreCase(newEmail) && emailExists(newEmail)) {
            throw new DuplicateEmailException("Duplicate email: " + newEmail);
        }

        client.setFirstName(newFirstName);
        client.setLastName(newLastName);
        client.setEmail(newEmail);
    }

    /** Deletes a client only if no existing trip still references that client. */
    public void deleteClient(String clientId) throws EntityNotFoundException, InvalidClientDataException {
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null && clientId.equals(trips[tripIndex].getClientId())) {
                throw new InvalidClientDataException("Cannot delete client with existing trips.");
            }
        }

        int clientIndex = findClientIndexById(clientId);
        shiftClientsLeft(clientIndex);
        clientCount--;
    }

    /** Searches the active client range to see whether an email is already used. */
    public boolean emailExists(String email) {
        if (email == null) return false;
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] != null && email.equalsIgnoreCase(clients[clientIndex].getEmail())) return true;
        }
        return false;
    }

    /** Checks whether the given client ID exists in the active client array. */
    public boolean clientExists(String clientId) {
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] != null && clients[clientIndex].getClientId().equals(clientId)) return true;
        }
        return false;
    }

    /** Finds and returns a client by ID or throws if the client does not exist. */
    public Client findClientById(String clientId) throws EntityNotFoundException {
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] != null && clients[clientIndex].getClientId().equals(clientId)) return clients[clientIndex];
        }
        throw new EntityNotFoundException("Client not found: " + clientId);
    }

    /** Adds transportation only after checking for null input and available array capacity. */
    public void addTransportation(Transportation transportation) throws InvalidTransportDataException {
        if (transportation == null) throw new InvalidTransportDataException("Cannot add null transportation.");
        if (transportCount >= transportations.length) throw new InvalidTransportDataException("Transportation list is full.");
        transportations[transportCount++] = transportation;
    }

    /** Removes transportation only if no existing trip is still using it. */
    public void removeTransportation(String transportId) throws EntityNotFoundException, InvalidTransportDataException {
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null && transportId.equals(trips[tripIndex].getTransportationId())) {
                throw new InvalidTransportDataException("Cannot remove transportation used by a trip.");
            }
        }

        int transportationIndex = findTransportationIndexById(transportId);
        shiftTransportationsLeft(transportationIndex);
        transportCount--;
    }

    /** Finds and returns transportation by ID or throws if it does not exist. */
    public Transportation findTransportationById(String transportId) throws EntityNotFoundException {
        for (int transportationIndex = 0; transportationIndex < transportCount; transportationIndex++) {
            if (transportations[transportationIndex] != null &&
                    transportations[transportationIndex].getTransportId().equals(transportId)) {
                return transportations[transportationIndex];
            }
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    /** Adds accommodation only after checking for null input and available array capacity. */
    public void addAccommodation(Accommodation accommodation) throws InvalidAccommodationDataException {
        if (accommodation == null) throw new InvalidAccommodationDataException("Cannot add null accommodation.");
        if (accommodationCount >= accommodations.length) throw new InvalidAccommodationDataException("Accommodation list is full.");
        accommodations[accommodationCount++] = accommodation;
    }

    /** Removes accommodation only if no existing trip is still using it. */
    public void removeAccommodation(String accommodationId) throws EntityNotFoundException, InvalidAccommodationDataException {
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null && accommodationId.equals(trips[tripIndex].getAccommodationId())) {
                throw new InvalidAccommodationDataException("Cannot remove accommodation used by a trip.");
            }
        }

        int accommodationIndex = findAccommodationIndexById(accommodationId);
        shiftAccommodationsLeft(accommodationIndex);
        accommodationCount--;
    }

    /** Finds and returns accommodation by ID or throws if it does not exist. */
    public Accommodation findAccommodationById(String accommodationId) throws EntityNotFoundException {
        for (int accommodationIndex = 0; accommodationIndex < accommodationCount; accommodationIndex++) {
            if (accommodations[accommodationIndex] != null &&
                    accommodations[accommodationIndex].getAccommodationId().equals(accommodationId)) {
                return accommodations[accommodationIndex];
            }
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }

    /** Adds a trip after resolving any referenced objects and updating client spending. */
    public void addTrip(Trip trip) throws InvalidTripDataException, EntityNotFoundException, InvalidClientDataException {
        if (trip == null) throw new InvalidTripDataException("Cannot add null trip.");
        if (tripCount >= trips.length) throw new InvalidTripDataException("Trip list is full.");

        Client client = findClientById(trip.getClientId());

        if (trip.getTransportation() == null && trip.getTransportationId() != null) {
            trip.setTransportation(findTransportationById(trip.getTransportationId()));
        }
        if (trip.getAccommodation() == null && trip.getAccommodationId() != null) {
            trip.setAccommodation(findAccommodationById(trip.getAccommodationId()));
        }

        trips[tripCount++] = trip;
        client.addToAmountSpent(trip.calculateTotalCost());
    }

    /** Validates all replacement values first so the trip is not left partially edited. */
    public void editTrip(String tripId, String destination, int duration, double basePrice,
                         String accommodationId, String transportationId)
            throws EntityNotFoundException, InvalidTripDataException, InvalidClientDataException {

        Trip trip = findTripById(tripId);

        String newDestination = (destination == null) ? null : destination.trim();
        String newAccommodationId = (accommodationId == null || accommodationId.trim().isEmpty())
                ? null : accommodationId.trim();
        String newTransportationId = (transportationId == null || transportationId.trim().isEmpty())
                ? null : transportationId.trim();

        if (newDestination == null || newDestination.isEmpty()) {
            throw new InvalidTripDataException("Destination cannot be empty.");
        }

        if (duration < 1 || duration > 20) {
            throw new InvalidTripDataException("Duration must be between 1 and 20 days.");
        }

        if (basePrice < 100.0) {
            throw new InvalidTripDataException("Base price must be >= 100.00");
        }

        Accommodation newAccommodation = null;
        Transportation newTransportation = null;

        if (newAccommodationId != null) {
            newAccommodation = findAccommodationById(newAccommodationId);
        }

        if (newTransportationId != null) {
            newTransportation = findTransportationById(newTransportationId);
        }

        if (newAccommodation == null && newTransportation == null) {
            throw new InvalidTripDataException(
                    "Trip must have at least one of: accommodation or transportation."
            );
        }

        trip.setAccommodationId(newAccommodationId);
        trip.setTransportationId(newTransportationId);

        trip.setAccommodation(newAccommodation);
        trip.setTransportation(newTransportation);

        trip.setDestination(newDestination);
        trip.setDurationInDays(duration);
        trip.setBasePrice(basePrice);

        recomputeAllClientSpending();
    }

    /** Cancels a trip by shifting the array left and rebuilding client spending totals. */
    public void cancelTrip(String tripId) throws EntityNotFoundException, InvalidClientDataException {
        int tripIndex = findTripIndexById(tripId);
        shiftTripsLeft(tripIndex);
        tripCount--;
        recomputeAllClientSpending();
    }

    /** Finds and returns a trip by ID or throws if it does not exist. */
    public Trip findTripById(String tripId) throws EntityNotFoundException {
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null && trips[tripIndex].getTripId().equals(tripId)) return trips[tripIndex];
        }
        throw new EntityNotFoundException("Trip not found: " + tripId);
    }

    /** Calculates the total cost for a trip using its array index. */
    public double calculateTripTotal(int tripIndex) throws InvalidTripDataException {
        if (tripIndex < 0 || tripIndex >= tripCount || trips[tripIndex] == null) {
            throw new InvalidTripDataException("Invalid trip index: " + tripIndex);
        }
        return trips[tripIndex].calculateTotalCost();
    }

    /** Scans the active trip list and returns the trip with the highest total cost. */
    public Trip findMostExpensiveTrip() throws EntityNotFoundException {
        if (tripCount == 0) {
            throw new EntityNotFoundException("No trips available.");
        }

        Trip mostExpensiveTrip = null;
        double highestTripCost = -1;

        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null) {
                double currentTripCost = trips[tripIndex].calculateTotalCost();
                if (mostExpensiveTrip == null || currentTripCost > highestTripCost) {
                    mostExpensiveTrip = trips[tripIndex];
                    highestTripCost = currentTripCost;
                }
            }
        }

        return mostExpensiveTrip;
    }

    /** Builds a deep-copied transportation array using the correct subclass copy constructor. */
    public Transportation[] deepCopyTransportationArray() {
        Transportation[] copiedTransportations = new Transportation[transportCount];

        for (int transportationIndex = 0; transportationIndex < transportCount; transportationIndex++) {
            if (transportations[transportationIndex] instanceof Flight) {
                copiedTransportations[transportationIndex] = new Flight((Flight) transportations[transportationIndex]);
            } else if (transportations[transportationIndex] instanceof Train) {
                copiedTransportations[transportationIndex] = new Train((Train) transportations[transportationIndex]);
            } else if (transportations[transportationIndex] instanceof Bus) {
                copiedTransportations[transportationIndex] = new Bus((Bus) transportations[transportationIndex]);
            }
        }

        return copiedTransportations;
    }

    /** Builds a deep-copied accommodation array using the correct subclass copy constructor. */
    public Accommodation[] deepCopyAccommodationArray() {
        Accommodation[] copiedAccommodations = new Accommodation[accommodationCount];

        for (int accommodationIndex = 0; accommodationIndex < accommodationCount; accommodationIndex++) {
            if (accommodations[accommodationIndex] instanceof Hotel) {
                copiedAccommodations[accommodationIndex] = new Hotel((Hotel) accommodations[accommodationIndex]);
            } else if (accommodations[accommodationIndex] instanceof Hostel) {
                copiedAccommodations[accommodationIndex] = new Hostel((Hostel) accommodations[accommodationIndex]);
            }
        }

        return copiedAccommodations;
    }

    /** Recomputes every client's amount spent from scratch using the current trip list. */
    public void recomputeAllClientSpending() throws InvalidClientDataException {
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] != null) clients[clientIndex].setAmountSpent(0.0);
        }

        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            Trip trip = trips[tripIndex];
            if (trip == null) continue;

            for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
                if (clients[clientIndex] != null && clients[clientIndex].getClientId().equals(trip.getClientId())) {
                    clients[clientIndex].addToAmountSpent(trip.calculateTotalCost());
                    break;
                }
            }
        }
    }

    /** Loads clients first, then related entities, and trips last to preserve dependencies. */
    public void loadAllData(String dataDirectory) throws IOException {
        clearAllData();

        clientCount = ClientFileManager.loadClients(clients, dataDirectory + "/clients.csv");
        transportCount = TransportationFileManager.loadTransportations(transportations, dataDirectory + "/transports.csv");
        accommodationCount = AccommodationFileManager.loadAccommodations(accommodations, dataDirectory + "/accommodations.csv");
        tripCount = TripFileManager.loadTrips(trips, dataDirectory + "/trips.csv", this);

        try {
            recomputeAllClientSpending();
        } catch (InvalidClientDataException exception) {
            ErrorLogger.log("Recompute spending error: " + exception.getMessage());
        }
    }

    /** Saves all current in-memory arrays to the expected CSV output files. */
    public void saveAllData(String outputDirectory) throws IOException {
        ClientFileManager.saveClients(clients, clientCount, outputDirectory + "/clients.csv");
        TransportationFileManager.saveTransportations(transportations, transportCount, outputDirectory + "/transports.csv");
        AccommodationFileManager.saveAccommodations(accommodations, accommodationCount, outputDirectory + "/accommodations.csv");
        TripFileManager.saveTrips(trips, tripCount, outputDirectory + "/trips.csv");
    }

    /** Clears every array slot and resets all counters and ID generators. */
    public void clearAllData() {
        for (int clientIndex = 0; clientIndex < clients.length; clientIndex++) clients[clientIndex] = null;
        for (int tripIndex = 0; tripIndex < trips.length; tripIndex++) trips[tripIndex] = null;
        for (int transportationIndex = 0; transportationIndex < transportations.length; transportationIndex++) transportations[transportationIndex] = null;
        for (int accommodationIndex = 0; accommodationIndex < accommodations.length; accommodationIndex++) accommodations[accommodationIndex] = null;

        clientCount = 0;
        tripCount = 0;
        transportCount = 0;
        accommodationCount = 0;

        Client.resetIdCounter();
        Trip.resetIdCounter();
        Transportation.resetIdCounter();
        Accommodation.resetIdCounter();
    }

    /** Finds the client array index used by delete and shift operations. */
    private int findClientIndexById(String clientId) throws EntityNotFoundException {
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] != null && clients[clientIndex].getClientId().equals(clientId)) return clientIndex;
        }
        throw new EntityNotFoundException("Client not found: " + clientId);
    }

    /** Finds the trip array index used by delete and shift operations. */
    private int findTripIndexById(String tripId) throws EntityNotFoundException {
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null && trips[tripIndex].getTripId().equals(tripId)) return tripIndex;
        }
        throw new EntityNotFoundException("Trip not found: " + tripId);
    }

    /** Finds the transportation array index used by delete and shift operations. */
    private int findTransportationIndexById(String transportId) throws EntityNotFoundException {
        for (int transportationIndex = 0; transportationIndex < transportCount; transportationIndex++) {
            if (transportations[transportationIndex] != null &&
                    transportations[transportationIndex].getTransportId().equals(transportId)) {
                return transportationIndex;
            }
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    /** Finds the accommodation array index used by delete and shift operations. */
    private int findAccommodationIndexById(String accommodationId) throws EntityNotFoundException {
        for (int accommodationIndex = 0; accommodationIndex < accommodationCount; accommodationIndex++) {
            if (accommodations[accommodationIndex] != null &&
                    accommodations[accommodationIndex].getAccommodationId().equals(accommodationId)) {
                return accommodationIndex;
            }
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }

    /** Shifts client elements left after deletion to keep the active range contiguous. */
    private void shiftClientsLeft(int startIndex) {
        for (int clientIndex = startIndex; clientIndex < clientCount - 1; clientIndex++) {
            clients[clientIndex] = clients[clientIndex + 1];
        }
        clients[clientCount - 1] = null;
    }

    /** Shifts trip elements left after deletion to keep the active range contiguous. */
    private void shiftTripsLeft(int startIndex) {
        for (int tripIndex = startIndex; tripIndex < tripCount - 1; tripIndex++) {
            trips[tripIndex] = trips[tripIndex + 1];
        }
        trips[tripCount - 1] = null;
    }

    /** Shifts transportation elements left after deletion to keep the active range contiguous. */
    private void shiftTransportationsLeft(int startIndex) {
        for (int transportationIndex = startIndex; transportationIndex < transportCount - 1; transportationIndex++) {
            transportations[transportationIndex] = transportations[transportationIndex + 1];
        }
        transportations[transportCount - 1] = null;
    }

    /** Shifts accommodation elements left after deletion to keep the active range contiguous. */
    private void shiftAccommodationsLeft(int startIndex) {
        for (int accommodationIndex = startIndex; accommodationIndex < accommodationCount - 1; accommodationIndex++) {
            accommodations[accommodationIndex] = accommodations[accommodationIndex + 1];
        }
        accommodations[accommodationCount - 1] = null;
    }
}