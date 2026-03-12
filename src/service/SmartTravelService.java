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

    // ---------------------------
    // Counts + arrays access
    // ---------------------------

    public int getClientCount() { return clientCount; }
    public int getTripCount() { return tripCount; }
    public int getTransportCount() { return transportCount; }
    public int getAccommodationCount() { return accommodationCount; }

    public Client[] getClients() { return clients; }
    public Trip[] getTrips() { return trips; }
    public Transportation[] getTransportations() { return transportations; }
    public Accommodation[] getAccommodations() { return accommodations; }

    // ---------------------------
    // Client operations
    // ---------------------------

    public void addClient(Client c) throws DuplicateEmailException, InvalidClientDataException {
        if (c == null) throw new InvalidClientDataException("Cannot add null client.");
        if (emailExists(c.getEmail())) {
            throw new DuplicateEmailException("Duplicate email: " + c.getEmail());
        }
        if (clientCount >= clients.length) {
            throw new InvalidClientDataException("Client list is full.");
        }
        clients[clientCount++] = c;
    }

    public boolean emailExists(String email) {
        if (email == null) return false;
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && email.equalsIgnoreCase(clients[i].getEmail())) return true;
        }
        return false;
    }

    public boolean clientExists(String clientId) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getClientId().equals(clientId)) return true;
        }
        return false;
    }

    public Client findClientById(String clientId) throws EntityNotFoundException {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getClientId().equals(clientId)) return clients[i];
        }
        throw new EntityNotFoundException("Client not found: " + clientId);
    }

    // ---------------------------
    // Transportation operations
    // ---------------------------

    public void addTransportation(Transportation t) throws InvalidTransportDataException {
        if (t == null) throw new InvalidTransportDataException("Cannot add null transportation.");
        if (transportCount >= transportations.length) throw new InvalidTransportDataException("Transportation list is full.");
        transportations[transportCount++] = t;
    }

    public Transportation findTransportationById(String transportId) throws EntityNotFoundException {
        for (int i = 0; i < transportCount; i++) {
            if (transportations[i] != null && transportations[i].getTransportId().equals(transportId)) return transportations[i];
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    // ---------------------------
    // Accommodation operations
    // ---------------------------

    public void addAccommodation(Accommodation a) throws InvalidAccommodationDataException {
        if (a == null) throw new InvalidAccommodationDataException("Cannot add null accommodation.");
        if (accommodationCount >= accommodations.length) throw new InvalidAccommodationDataException("Accommodation list is full.");
        accommodations[accommodationCount++] = a;
    }

    public Accommodation findAccommodationById(String accommodationId) throws EntityNotFoundException {
        for (int i = 0; i < accommodationCount; i++) {
            if (accommodations[i] != null && accommodations[i].getAccommodationId().equals(accommodationId)) return accommodations[i];
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }

    // ---------------------------
    // Trip operations
    // ---------------------------

    public void addTrip(Trip t) throws InvalidTripDataException, EntityNotFoundException, InvalidClientDataException {
        if (t == null) throw new InvalidTripDataException("Cannot add null trip.");
        if (tripCount >= trips.length) throw new InvalidTripDataException("Trip list is full.");

        // A2 requirement: clientId must exist in system
        String cid = t.getClientId();
        Client c = findClientById(cid);

        // Resolve optional references if IDs exist
        if (t.getTransportation() == null && t.getTransportationId() != null) {
            try { t.setTransportation(findTransportationById(t.getTransportationId())); }
            catch (EntityNotFoundException ignore) { /* load may keep it null; TripFileManager handles strictness */ }
        }
        if (t.getAccommodation() == null && t.getAccommodationId() != null) {
            try { t.setAccommodation(findAccommodationById(t.getAccommodationId())); }
            catch (EntityNotFoundException ignore) { /* same */ }
        }

        trips[tripCount++] = t;

        // Update amount spent (A2)
        c.addToAmountSpent(t.calculateTotalCost());
    }

    public double calculateTripTotal(int index) throws InvalidTripDataException {
        if (index < 0 || index >= tripCount || trips[index] == null) {
            throw new InvalidTripDataException("Invalid trip index: " + index);
        }
        return trips[index].calculateTotalCost();
    }

    public Trip findMostExpensiveTrip() throws EntityNotFoundException {
        if (tripCount == 0) {
            throw new EntityNotFoundException("No trips available.");
        }

        Trip mostExpensive = null;
        double maxCost = -1;

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null) {
                double cost = trips[i].calculateTotalCost();

                if (mostExpensive == null || cost > maxCost) {
                    mostExpensive = trips[i];
                    maxCost = cost;
                }
            }
        }

        return mostExpensive;
    }

    public Transportation[] deepCopyTransportationArray() {

        Transportation[] copy = new Transportation[transportCount];

        for (int i = 0; i < transportCount; i++) {

            if (transportations[i] instanceof Flight) {
                copy[i] = new Flight((Flight) transportations[i]);
            }

            else if (transportations[i] instanceof Train) {
                copy[i] = new Train((Train) transportations[i]);
            }

            else if (transportations[i] instanceof Bus) {
                copy[i] = new Bus((Bus) transportations[i]);
            }
        }

        return copy;
    }

    public Accommodation[] deepCopyAccommodationArray() {

        Accommodation[] copy = new Accommodation[accommodationCount];

        for (int i = 0; i < accommodationCount; i++) {

            if (accommodations[i] instanceof Hotel) {
                copy[i] = new Hotel((Hotel) accommodations[i]);
            }

            else if (accommodations[i] instanceof Hostel) {
                copy[i] = new Hostel((Hostel) accommodations[i]);
            }
        }

        return copy;
    }

    // For Trip Chart Generator
    public Trip[] getAllTrips() {
        return trips;
    }

    /** Recompute amountSpent from scratch (useful after bulk-load) */
    public void recomputeAllClientSpending() throws InvalidClientDataException {
        // reset
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null) clients[i].setAmountSpent(0.0);
        }
        // add totals
        for (int i = 0; i < tripCount; i++) {
            Trip t = trips[i];
            if (t == null) continue;
            for (int j = 0; j < clientCount; j++) {
                if (clients[j] != null && clients[j].getClientId().equals(t.getClientId())) {
                    clients[j].addToAmountSpent(t.calculateTotalCost());
                    break;
                }
            }
        }
    }

    // ---------------------------
    // Persistence orchestration
    // ---------------------------

    public void loadAllData(String dataDir) throws IOException {
        // reset counts (arrays are reused)
        clientCount = 0;
        tripCount = 0;
        transportCount = 0;
        accommodationCount = 0;

        // Clients first
        clientCount = ClientFileManager.loadClients(clients, dataDir + "/clients.csv");

        // Transportation + accommodation next
        transportCount = TransportationFileManager.loadTransportations(transportations, dataDir + "/transports.csv");
        accommodationCount = AccommodationFileManager.loadAccommodations(accommodations, dataDir + "/accommodations.csv");

        // Trips last (depends on IDs existing)
        tripCount = TripFileManager.loadTrips(trips, dataDir + "/trips.csv", this);

        // After load, amountSpent must reflect trips
        try {
            recomputeAllClientSpending();
        } catch (InvalidClientDataException e) {
            ErrorLogger.log("Recompute spending error: " + e.getMessage());
        }
    }

    public void saveAllData(String outDir) throws IOException {
        ClientFileManager.saveClients(clients, clientCount, outDir + "/clients.csv");
        TransportationFileManager.saveTransportations(transportations, transportCount, outDir + "/transports.csv");
        AccommodationFileManager.saveAccommodations(accommodations, accommodationCount, outDir + "/accommodations.csv");
        TripFileManager.saveTrips(trips, tripCount, outDir + "/trips.csv");
    }

    // ---------------------------
// Reset service data (for predefined scenario)
// ---------------------------

    public void clearAllData() {
        for (int i = 0; i < clientCount; i++) {
            clients[i] = null;
        }
        for (int i = 0; i < tripCount; i++) {
            trips[i] = null;
        }
        for (int i = 0; i < transportCount; i++) {
            transportations[i] = null;
        }
        for (int i = 0; i < accommodationCount; i++) {
            accommodations[i] = null;
        }

        clientCount = 0;
        tripCount = 0;
        transportCount = 0;
        accommodationCount = 0;
    }

    // Delete a Client
    public void deleteClient(String clientId) throws EntityNotFoundException {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getClientId().equals(clientId)) {
                // shift left
                for (int j = i; j < clientCount - 1; j++) {
                    clients[j] = clients[j + 1];
                }
                clients[--clientCount] = null;
                return;
            }
        }
        throw new EntityNotFoundException("Client not found: " + clientId);
    }

    // Delete Trip
    public void deleteTrip(String tripId) throws EntityNotFoundException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && trips[i].getTripId().equals(tripId)) {
                for (int j = i; j < tripCount - 1; j++) {
                    trips[j] = trips[j + 1];
                }
                trips[--tripCount] = null;
                return;
            }
        }
        throw new EntityNotFoundException("Trip not found: " + tripId);
    }

// DELETE TRANSPORTATION
    public void deleteTransportation(String transportId) throws EntityNotFoundException {
        for (int i = 0; i < transportCount; i++) {
            if (transportations[i] != null && transportations[i].getTransportId().equals(transportId)) {
                for (int j = i; j < transportCount - 1; j++)
                    transportations[j] = transportations[j + 1];
                transportations[--transportCount] = null;
                return;
            }
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    // DELETE ACCOMMODATION
    public void deleteAccommodation(String accommodationId) throws EntityNotFoundException {
        for (int i = 0; i < accommodationCount; i++) {
            if (accommodations[i] != null && accommodations[i].getAccommodationId().equals(accommodationId)) {
                for (int j = i; j < accommodationCount - 1; j++)
                    accommodations[j] = accommodations[j + 1];
                accommodations[--accommodationCount] = null;
                return;
            }
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }
}