// -----------------------------------------------------
// Assignment 2
// Class: SmartTravelService
// Written by: Yousef Yousef (40299095) & Hamza Shadeed
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

    public void editClient(String clientId, String firstName, String lastName, String email)
            throws EntityNotFoundException, InvalidClientDataException {
        Client c = findClientById(clientId);

        if (!c.getEmail().equalsIgnoreCase(email) && emailExists(email)) {
            throw new DuplicateEmailException("Duplicate email: " + email);
        }

        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setEmail(email);
    }

    public void deleteClient(String clientId) throws EntityNotFoundException, InvalidClientDataException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && clientId.equals(trips[i].getClientId())) {
                throw new InvalidClientDataException("Cannot delete client with existing trips.");
            }
        }

        int index = findClientIndexById(clientId);
        shiftClientsLeft(index);
        clientCount--;
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

    public void addTransportation(Transportation t) throws InvalidTransportDataException {
        if (t == null) throw new InvalidTransportDataException("Cannot add null transportation.");
        if (transportCount >= transportations.length) throw new InvalidTransportDataException("Transportation list is full.");
        transportations[transportCount++] = t;
    }

    public void removeTransportation(String transportId) throws EntityNotFoundException, InvalidTransportDataException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && transportId.equals(trips[i].getTransportationId())) {
                throw new InvalidTransportDataException("Cannot remove transportation used by a trip.");
            }
        }

        int index = findTransportationIndexById(transportId);
        shiftTransportationsLeft(index);
        transportCount--;
    }

    public Transportation findTransportationById(String transportId) throws EntityNotFoundException {
        for (int i = 0; i < transportCount; i++) {
            if (transportations[i] != null && transportations[i].getTransportId().equals(transportId)) return transportations[i];
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    public void addAccommodation(Accommodation a) throws InvalidAccommodationDataException {
        if (a == null) throw new InvalidAccommodationDataException("Cannot add null accommodation.");
        if (accommodationCount >= accommodations.length) throw new InvalidAccommodationDataException("Accommodation list is full.");
        accommodations[accommodationCount++] = a;
    }

    public void removeAccommodation(String accommodationId) throws EntityNotFoundException, InvalidAccommodationDataException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && accommodationId.equals(trips[i].getAccommodationId())) {
                throw new InvalidAccommodationDataException("Cannot remove accommodation used by a trip.");
            }
        }

        int index = findAccommodationIndexById(accommodationId);
        shiftAccommodationsLeft(index);
        accommodationCount--;
    }

    public Accommodation findAccommodationById(String accommodationId) throws EntityNotFoundException {
        for (int i = 0; i < accommodationCount; i++) {
            if (accommodations[i] != null && accommodations[i].getAccommodationId().equals(accommodationId)) return accommodations[i];
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }

    public void addTrip(Trip t) throws InvalidTripDataException, EntityNotFoundException, InvalidClientDataException {
        if (t == null) throw new InvalidTripDataException("Cannot add null trip.");
        if (tripCount >= trips.length) throw new InvalidTripDataException("Trip list is full.");

        Client c = findClientById(t.getClientId());

        if (t.getTransportation() == null && t.getTransportationId() != null) {
            t.setTransportation(findTransportationById(t.getTransportationId()));
        }
        if (t.getAccommodation() == null && t.getAccommodationId() != null) {
            t.setAccommodation(findAccommodationById(t.getAccommodationId()));
        }

        trips[tripCount++] = t;
        c.addToAmountSpent(t.calculateTotalCost());
    }

    public void editTrip(String tripId, String destination, int duration, double basePrice,
                         String accommodationId, String transportationId)
            throws EntityNotFoundException, InvalidTripDataException, InvalidClientDataException {

        Trip t = findTripById(tripId);

        t.setDestination(destination);
        t.setDurationInDays(duration);
        t.setBasePrice(basePrice);

        if (accommodationId == null) {
            t.setAccommodationId(null);
            t.setAccommodation(null);
        } else {
            Accommodation a = findAccommodationById(accommodationId);
            t.setAccommodationId(accommodationId);
            t.setAccommodation(a);
        }

        if (transportationId == null) {
            t.setTransportationId(null);
            t.setTransportation(null);
        } else {
            Transportation tr = findTransportationById(transportationId);
            t.setTransportationId(transportationId);
            t.setTransportation(tr);
        }

        recomputeAllClientSpending();
    }

    public void cancelTrip(String tripId) throws EntityNotFoundException, InvalidClientDataException {
        int index = findTripIndexById(tripId);
        shiftTripsLeft(index);
        tripCount--;
        recomputeAllClientSpending();
    }

    public Trip findTripById(String tripId) throws EntityNotFoundException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && trips[i].getTripId().equals(tripId)) return trips[i];
        }
        throw new EntityNotFoundException("Trip not found: " + tripId);
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
            } else if (transportations[i] instanceof Train) {
                copy[i] = new Train((Train) transportations[i]);
            } else if (transportations[i] instanceof Bus) {
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
            } else if (accommodations[i] instanceof Hostel) {
                copy[i] = new Hostel((Hostel) accommodations[i]);
            }
        }

        return copy;
    }

    public void recomputeAllClientSpending() throws InvalidClientDataException {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null) clients[i].setAmountSpent(0.0);
        }

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

    public void loadAllData(String dataDir) throws IOException {
        clientCount = 0;
        tripCount = 0;
        transportCount = 0;
        accommodationCount = 0;

        clientCount = ClientFileManager.loadClients(clients, dataDir + "/clients.csv");
        transportCount = TransportationFileManager.loadTransportations(transportations, dataDir + "/transports.csv");
        accommodationCount = AccommodationFileManager.loadAccommodations(accommodations, dataDir + "/accommodations.csv");
        tripCount = TripFileManager.loadTrips(trips, dataDir + "/trips.csv", this);

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

    public void clearAllData() {
        for (int i = 0; i < clients.length; i++) clients[i] = null;
        for (int i = 0; i < trips.length; i++) trips[i] = null;
        for (int i = 0; i < transportations.length; i++) transportations[i] = null;
        for (int i = 0; i < accommodations.length; i++) accommodations[i] = null;

        clientCount = 0;
        tripCount = 0;
        transportCount = 0;
        accommodationCount = 0;

        Client.resetIdCounter();
        Trip.resetIdCounter();
        Transportation.resetIdCounter();
        Accommodation.resetIdCounter();
    }

    private int findClientIndexById(String clientId) throws EntityNotFoundException {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] != null && clients[i].getClientId().equals(clientId)) return i;
        }
        throw new EntityNotFoundException("Client not found: " + clientId);
    }

    private int findTripIndexById(String tripId) throws EntityNotFoundException {
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null && trips[i].getTripId().equals(tripId)) return i;
        }
        throw new EntityNotFoundException("Trip not found: " + tripId);
    }

    private int findTransportationIndexById(String transportId) throws EntityNotFoundException {
        for (int i = 0; i < transportCount; i++) {
            if (transportations[i] != null && transportations[i].getTransportId().equals(transportId)) return i;
        }
        throw new EntityNotFoundException("Transportation not found: " + transportId);
    }

    private int findAccommodationIndexById(String accommodationId) throws EntityNotFoundException {
        for (int i = 0; i < accommodationCount; i++) {
            if (accommodations[i] != null && accommodations[i].getAccommodationId().equals(accommodationId)) return i;
        }
        throw new EntityNotFoundException("Accommodation not found: " + accommodationId);
    }

    private void shiftClientsLeft(int start) {
        for (int i = start; i < clientCount - 1; i++) {
            clients[i] = clients[i + 1];
        }
        clients[clientCount - 1] = null;
    }

    private void shiftTripsLeft(int start) {
        for (int i = start; i < tripCount - 1; i++) {
            trips[i] = trips[i + 1];
        }
        trips[tripCount - 1] = null;
    }

    private void shiftTransportationsLeft(int start) {
        for (int i = start; i < transportCount - 1; i++) {
            transportations[i] = transportations[i + 1];
        }
        transportations[transportCount - 1] = null;
    }

    private void shiftAccommodationsLeft(int start) {
        for (int i = start; i < accommodationCount - 1; i++) {
            accommodations[i] = accommodations[i + 1];
        }
        accommodations[accommodationCount - 1] = null;
    }
}