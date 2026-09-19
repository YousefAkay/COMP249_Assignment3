package service;

import client.Client;
import exceptions.*;
import persistence.GenericFileManager;
import travel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Core service layer that coordinates SmartTravel data, validation, and analytics. */
public class SmartTravelService {

    private static final String DEFAULT_INPUT_DIRECTORY = "data";
    private static final String DEFAULT_OUTPUT_DIRECTORY = "output/data";
    private static final String CLIENTS_FILE_NAME = "clients.csv";
    private static final String TRANSPORTATIONS_FILE_NAME = "transports.csv";
    private static final String ACCOMMODATIONS_FILE_NAME = "accommodations.csv";
    private static final String TRIPS_FILE_NAME = "trips.csv";

    private final List<Client> clients;
    private final List<Trip> trips;
    private final List<Transportation> transportations;
    private final List<Accommodation> accommodations;

    private final Repository<Client> clientRepository;
    private final Repository<Trip> tripRepository;
    private final Repository<Transportation> transportationRepository;
    private final Repository<Accommodation> accommodationRepository;

    private final RecentList<Trip> recentTrips;

    /** Preserves the old constructor signature while migrating internal storage to collections. */
    public SmartTravelService(Client[] clients, Trip[] trips, Transportation[] transportations, Accommodation[] accommodations) {
        this();
        initializeFromArrays(clients, trips, transportations, accommodations);
    }

    /** Preferred collection-backed constructor for A3. */
    public SmartTravelService() {
        this.clients = new ArrayList<Client>();
        this.trips = new ArrayList<Trip>();
        this.transportations = new ArrayList<Transportation>();
        this.accommodations = new ArrayList<Accommodation>();

        this.clientRepository = new Repository<Client>();
        this.tripRepository = new Repository<Trip>();
        this.transportationRepository = new Repository<Transportation>();
        this.accommodationRepository = new Repository<Accommodation>();

        this.recentTrips = new RecentList<Trip>();
    }

    public int getClientCount() { return clients.size(); }
    public int getTripCount() { return trips.size(); }
    public int getTransportCount() { return transportations.size(); }
    public int getAccommodationCount() { return accommodations.size(); }

    /** Returns an array snapshot for A2-compatible callers such as the current driver and dashboard. */
    public Client[] getClients() { return clients.toArray(new Client[clients.size()]); }
    public Trip[] getTrips() { return trips.toArray(new Trip[trips.size()]); }
    public Transportation[] getTransportations() { return transportations.toArray(new Transportation[transportations.size()]); }
    public Accommodation[] getAccommodations() { return accommodations.toArray(new Accommodation[accommodations.size()]); }

    /** Exposes the live lists for A3 collection-based logic. */
    public List<Client> getClientList() { return new ArrayList<Client>(clients); }
    public List<Trip> getTripList() { return new ArrayList<Trip>(trips); }
    public List<Transportation> getTransportationList() { return new ArrayList<Transportation>(transportations); }
    public List<Accommodation> getAccommodationList() { return new ArrayList<Accommodation>(accommodations); }

    /** Adds a client only after checking for null input and duplicate emails. */
    public void addClient(Client client) throws DuplicateEmailException, InvalidClientDataException {
        if (client == null) throw new InvalidClientDataException("Cannot add null client.");
        if (emailExists(client.getEmail())) {
            throw new DuplicateEmailException("Duplicate email: " + client.getEmail());
        }

        clients.add(client);
        clientRepository.add(client);
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
        for (int tripIndex = 0; tripIndex < trips.size(); tripIndex++) {
            Trip trip = trips.get(tripIndex);
            if (trip != null && clientId.equals(trip.getClientId())) {
                throw new InvalidClientDataException("Cannot delete client with existing trips.");
            }
        }

        Client client = findClientById(clientId);
        clients.remove(client);
        clientRepository.removeById(clientId);
    }

    /** Searches the active client list to see whether an email is already used. */
    public boolean emailExists(String email) {
        if (email == null) return false;
        for (int clientIndex = 0; clientIndex < clients.size(); clientIndex++) {
            Client client = clients.get(clientIndex);
            if (client != null && email.equalsIgnoreCase(client.getEmail())) return true;
        }
        return false;
    }

    /** Checks whether the given client ID exists in the repository. */
    public boolean clientExists(String clientId) {
        try {
            clientRepository.findById(clientId);
            return true;
        } catch (EntityNotFoundException exception) {
            return false;
        }
    }

    /** Finds and returns a client by ID or throws if the client does not exist. */
    public Client findClientById(String clientId) throws EntityNotFoundException {
        return clientRepository.findById(clientId);
    }

    /** Adds transportation after checking for null input. */
    public void addTransportation(Transportation transportation) throws InvalidTransportDataException {
        if (transportation == null) throw new InvalidTransportDataException("Cannot add null transportation.");
        transportations.add(transportation);
        transportationRepository.add(transportation);
    }

    /** Removes transportation only if no existing trip is still using it. */
    public void removeTransportation(String transportId) throws EntityNotFoundException, InvalidTransportDataException {
        for (int tripIndex = 0; tripIndex < trips.size(); tripIndex++) {
            Trip trip = trips.get(tripIndex);
            if (trip != null && transportId.equals(trip.getTransportationId())) {
                throw new InvalidTransportDataException("Cannot remove transportation used by a trip.");
            }
        }

        Transportation transportation = findTransportationById(transportId);
        transportations.removeIf(item -> item == transportation);
        transportationRepository.removeById(transportId);
    }

    /** Finds and returns transportation by ID or throws if it does not exist. */
    public Transportation findTransportationById(String transportId) throws EntityNotFoundException {
        return transportationRepository.findById(transportId);
    }

    /** Adds accommodation after checking for null input. */
    public void addAccommodation(Accommodation accommodation) throws InvalidAccommodationDataException {
        if (accommodation == null) throw new InvalidAccommodationDataException("Cannot add null accommodation.");
        accommodations.add(accommodation);
        accommodationRepository.add(accommodation);
    }

    /** Removes accommodation only if no existing trip is still using it. */
    public void removeAccommodation(String accommodationId) throws EntityNotFoundException, InvalidAccommodationDataException {
        for (int tripIndex = 0; tripIndex < trips.size(); tripIndex++) {
            Trip trip = trips.get(tripIndex);
            if (trip != null && accommodationId.equals(trip.getAccommodationId())) {
                throw new InvalidAccommodationDataException("Cannot remove accommodation used by a trip.");
            }
        }

        Accommodation accommodation = findAccommodationById(accommodationId);
        accommodations.removeIf(item -> item == accommodation);
        accommodationRepository.removeById(accommodationId);
    }

    /** Finds and returns accommodation by ID or throws if it does not exist. */
    public Accommodation findAccommodationById(String accommodationId) throws EntityNotFoundException {
        return accommodationRepository.findById(accommodationId);
    }

    /** Adds a trip after resolving any referenced objects and updating client spending. */
    public void addTrip(Trip trip) throws InvalidTripDataException, EntityNotFoundException, InvalidClientDataException {
        if (trip == null) throw new InvalidTripDataException("Cannot add null trip.");

        Client client = findClientById(trip.getClientId());
        trip.setClient(client);

        if (trip.getTransportation() == null && trip.getTransportationId() != null) {
            trip.setTransportation(findTransportationById(trip.getTransportationId()));
        }
        if (trip.getAccommodation() == null && trip.getAccommodationId() != null) {
            trip.setAccommodation(findAccommodationById(trip.getAccommodationId()));
        }

        trips.add(trip);
        tripRepository.add(trip);
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

    /** Cancels a trip and rebuilds client spending totals. */
    public void cancelTrip(String tripId) throws EntityNotFoundException, InvalidClientDataException {
        Trip trip = findTripById(tripId);
        trips.remove(trip);
        tripRepository.removeById(tripId);
        recomputeAllClientSpending();
    }

    /** Finds and returns a trip by ID or throws if it does not exist. */
    public Trip findTripById(String tripId) throws EntityNotFoundException {
        return tripRepository.findById(tripId);
    }

    /** Calculates the total cost for a trip using its current list index. */
    public double calculateTripTotal(int tripIndex) throws InvalidTripDataException {
        if (tripIndex < 0 || tripIndex >= trips.size()) {
            throw new InvalidTripDataException("Invalid trip index: " + tripIndex);
        }
        Trip trip = trips.get(tripIndex);
        rememberTrip(trip);
        return trip.calculateTotalCost();
    }

    /** Scans the current trip list and returns the trip with the highest total cost. */
    public Trip findMostExpensiveTrip() throws EntityNotFoundException {
        if (trips.isEmpty()) {
            throw new EntityNotFoundException("No trips available.");
        }

        Trip mostExpensiveTrip = null;
        double highestTripCost = -1;

        for (int tripIndex = 0; tripIndex < trips.size(); tripIndex++) {
            Trip trip = trips.get(tripIndex);
            if (trip != null) {
                double currentTripCost = trip.calculateTotalCost();
                if (mostExpensiveTrip == null || currentTripCost > highestTripCost) {
                    mostExpensiveTrip = trip;
                    highestTripCost = currentTripCost;
                }
            }
        }

        rememberTrip(mostExpensiveTrip);
        return mostExpensiveTrip;
    }

    /** Builds a deep-copied transportation array using the correct subclass copy constructor. */
    public Transportation[] deepCopyTransportationArray() {
        Transportation[] copiedTransportations = new Transportation[transportations.size()];

        for (int transportationIndex = 0; transportationIndex < transportations.size(); transportationIndex++) {
            Transportation transportation = transportations.get(transportationIndex);
            if (transportation instanceof Flight) {
                copiedTransportations[transportationIndex] = new Flight((Flight) transportation);
            } else if (transportation instanceof Train) {
                copiedTransportations[transportationIndex] = new Train((Train) transportation);
            } else if (transportation instanceof Bus) {
                copiedTransportations[transportationIndex] = new Bus((Bus) transportation);
            }
        }

        return copiedTransportations;
    }

    /** Builds a deep-copied accommodation array using the correct subclass copy constructor. */
    public Accommodation[] deepCopyAccommodationArray() {
        Accommodation[] copiedAccommodations = new Accommodation[accommodations.size()];

        for (int accommodationIndex = 0; accommodationIndex < accommodations.size(); accommodationIndex++) {
            Accommodation accommodation = accommodations.get(accommodationIndex);
            if (accommodation instanceof Hotel) {
                copiedAccommodations[accommodationIndex] = new Hotel((Hotel) accommodation);
            } else if (accommodation instanceof Hostel) {
                copiedAccommodations[accommodationIndex] = new Hostel((Hostel) accommodation);
            }
        }

        return copiedAccommodations;
    }

    /** Recomputes every client's amount spent from scratch using the current trip list. */
    public void recomputeAllClientSpending() throws InvalidClientDataException {
        for (int clientIndex = 0; clientIndex < clients.size(); clientIndex++) {
            clients.get(clientIndex).setAmountSpent(0.0);
        }

        for (int tripIndex = 0; tripIndex < trips.size(); tripIndex++) {
            Trip trip = trips.get(tripIndex);
            Client client = trip.getClient();

            if (client == null && trip.getClientId() != null) {
                try {
                    client = findClientById(trip.getClientId());
                    trip.setClient(client);
                } catch (EntityNotFoundException | InvalidTripDataException exception) {
                    continue;
                }
            }

            if (client != null) {
                client.addToAmountSpent(trip.calculateTotalCost());
            }
        }
    }

    /** Loads all CSV data through the primary A3 GenericFileManager path, then applies service-level validation and relationship resolution in A2-compatible order. */
    public void loadAllData(String directory) throws IOException {
        clearAllData();
        String dataDirectory = resolveDirectory(directory, DEFAULT_INPUT_DIRECTORY);

        List<Client> loadedClients = new ArrayList<Client>();
        try {
            loadedClients = GenericFileManager.load(
                    buildCsvPath(dataDirectory, CLIENTS_FILE_NAME),
                    Client.class
            );
        } catch (IOException exception) {
            persistence.ErrorLogger.log("CLIENT LOAD ERROR | " + exception.getMessage());
        }

        for (int index = 0; index < loadedClients.size(); index++) {
            try {
                addClient(loadedClients.get(index));
            } catch (Exception exception) {
                persistence.ErrorLogger.log(
                        "CLIENT LOAD ERROR | " + exception.getMessage() +
                                " | line=" + loadedClients.get(index).toCsvRow()
                );
            }
        }

        List<Transportation> loadedTransportations = new ArrayList<Transportation>();
        try {
            loadedTransportations = GenericFileManager.load(
                    buildCsvPath(dataDirectory, TRANSPORTATIONS_FILE_NAME),
                    Transportation.class
            );
        } catch (IOException exception) {
            persistence.ErrorLogger.log("TRANSPORT LOAD ERROR | " + exception.getMessage());
        }

        for (int index = 0; index < loadedTransportations.size(); index++) {
            try {
                addTransportation(loadedTransportations.get(index));
            } catch (Exception exception) {
                persistence.ErrorLogger.log(
                        "TRANSPORT LOAD ERROR | " + exception.getMessage() +
                                " | line=" + loadedTransportations.get(index).toCsvRow()
                );
            }
        }

        List<Accommodation> loadedAccommodations = new ArrayList<Accommodation>();
        try {
            loadedAccommodations = GenericFileManager.load(
                    buildCsvPath(dataDirectory, ACCOMMODATIONS_FILE_NAME),
                    Accommodation.class
            );
        } catch (IOException exception) {
            persistence.ErrorLogger.log("ACCOM LOAD ERROR | " + exception.getMessage());
        }

        for (int index = 0; index < loadedAccommodations.size(); index++) {
            try {
                addAccommodation(loadedAccommodations.get(index));
            } catch (Exception exception) {
                persistence.ErrorLogger.log(
                        "ACCOM LOAD ERROR | " + exception.getMessage() +
                                " | line=" + loadedAccommodations.get(index).toCsvRow()
                );
            }
        }

        List<Trip> loadedTrips = new ArrayList<Trip>();
        try {
            loadedTrips = GenericFileManager.load(
                    buildCsvPath(dataDirectory, TRIPS_FILE_NAME),
                    Trip.class
            );
        } catch (IOException exception) {
            persistence.ErrorLogger.log("TRIP LOAD ERROR | " + exception.getMessage());
        }

        for (int index = 0; index < loadedTrips.size(); index++) {
            try {
                addTrip(loadedTrips.get(index));
            } catch (Exception exception) {
                persistence.ErrorLogger.log("TRIP LOAD ERROR | " + exception.getMessage() + " | line=" + loadedTrips.get(index).toCsvRow());
            }
        }

        try {
            recomputeAllClientSpending();
        } catch (InvalidClientDataException exception) {
            persistence.ErrorLogger.log("Recompute spending error: " + exception.getMessage());
        }
    }

    /** Saves all current in-memory lists through the primary A3 GenericFileManager path. */
    public void saveAllData(String directory) throws IOException {
        String outputDirectory = resolveDirectory(directory, DEFAULT_OUTPUT_DIRECTORY);

        GenericFileManager.save(clients, buildCsvPath(outputDirectory, CLIENTS_FILE_NAME));
        GenericFileManager.save(transportations, buildCsvPath(outputDirectory, TRANSPORTATIONS_FILE_NAME));
        GenericFileManager.save(accommodations, buildCsvPath(outputDirectory, ACCOMMODATIONS_FILE_NAME));
        GenericFileManager.save(trips, buildCsvPath(outputDirectory, TRIPS_FILE_NAME));
    }

    /** Clears all lists, repositories, recent history, and ID generators. */
    public void clearAllData() {
        clients.clear();
        trips.clear();
        transportations.clear();
        accommodations.clear();

        clientRepository.clear();
        tripRepository.clear();
        transportationRepository.clear();
        accommodationRepository.clear();

        recentTrips.clear();

        Client.resetIdCounter();
        Trip.resetIdCounter();
        Transportation.resetIdCounter();
        Accommodation.resetIdCounter();
    }

    /** Filters trips by destination and records the viewed trips in recent history. */
    public List<Trip> filterTripsByDestination(String destination) {
        String normalizedDestination = (destination == null) ? "" : destination.trim();
        List<Trip> filteredTrips = tripRepository.filter(new java.util.function.Predicate<Trip>() {
            @Override
            public boolean test(Trip trip) {
                return trip != null && trip.getDestination().equalsIgnoreCase(normalizedDestination);
            }
        });

        rememberTrips(filteredTrips);
        return filteredTrips;
    }

    /** Filters trips whose total cost lies within the provided inclusive range. */
    public List<Trip> filterTripsByTotalCostRange(double minimumTotalCost, double maximumTotalCost) {
        List<Trip> filteredTrips = tripRepository.filter(new java.util.function.Predicate<Trip>() {
            @Override
            public boolean test(Trip trip) {
                if (trip == null) {
                    return false;
                }
                double totalCost = trip.calculateTotalCost();
                return totalCost >= minimumTotalCost && totalCost <= maximumTotalCost;
            }
        });

        rememberTrips(filteredTrips);
        return filteredTrips;
    }

    /** Returns clients sorted by their natural business ordering. */
    public List<Client> getTopClientsBySpending() {
        return clientRepository.getSorted();
    }

    /** Returns the recent trip history snapshot from newest to oldest. */
    public List<Trip> getRecentTrips() {
        return recentTrips.getItems();
    }

    /** Prints up to the requested number of recent trips using the RecentList display path. */
    public void printRecentTrips(int maxToShow) {
        recentTrips.printRecent(maxToShow);
    }

    /** Returns trips sorted by their natural business ordering. */
    public List<Trip> getSmartSortedTrips() {
        List<Trip> sortedTrips = tripRepository.getSorted();
        rememberTrips(sortedTrips);
        return sortedTrips;
    }

    /** Returns clients sorted by their natural business ordering. */
    public List<Client> getSmartSortedClients() {
        return clientRepository.getSorted();
    }

    /** Returns accommodations sorted by their natural business ordering. */
    public List<Accommodation> getSmartSortedAccommodations() {
        return accommodationRepository.getSorted();
    }

    /** Returns transportation objects sorted by their natural business ordering. */
    public List<Transportation> getSmartSortedTransportations() {
        return transportationRepository.getSorted();
    }

    /** Adds a group of trips to the recent-trip history. */
    private void rememberTrips(List<Trip> viewedTrips) {
        for (int index = viewedTrips.size() - 1; index >= 0; index--) {
            rememberTrip(viewedTrips.get(index));
        }
    }

    /** Adds one viewed trip to the recent-trip history. */
    private void rememberTrip(Trip viewedTrip) {
        if (viewedTrip != null) {
            recentTrips.addRecent(viewedTrip);
        }
    }

    /** Falls back to the default directory only when the caller passes null or blank text. */
    private String resolveDirectory(String directory, String defaultDirectory) {
        if (directory == null || directory.trim().isEmpty()) {
            return defaultDirectory;
        }
        return directory.trim();
    }

    /** Builds one CSV file path while accepting directory values with or without a trailing slash. */
    private String buildCsvPath(String directory, String fileName) {
        if (directory.endsWith("/") || directory.endsWith("\\")) {
            return directory + fileName;
        }
        return directory + "/" + fileName;
    }

    /** Preserves the old array-based startup data path when the driver still passes arrays in. */
    private void initializeFromArrays(Client[] initialClients, Trip[] initialTrips,
                                      Transportation[] initialTransportations, Accommodation[] initialAccommodations) {
        if (initialClients != null) {
            for (int index = 0; index < initialClients.length; index++) {
                if (initialClients[index] != null) {
                    clients.add(initialClients[index]);
                    clientRepository.add(initialClients[index]);
                }
            }
        }

        if (initialTransportations != null) {
            for (int index = 0; index < initialTransportations.length; index++) {
                if (initialTransportations[index] != null) {
                    transportations.add(initialTransportations[index]);
                    transportationRepository.add(initialTransportations[index]);
                }
            }
        }

        if (initialAccommodations != null) {
            for (int index = 0; index < initialAccommodations.length; index++) {
                if (initialAccommodations[index] != null) {
                    accommodations.add(initialAccommodations[index]);
                    accommodationRepository.add(initialAccommodations[index]);
                }
            }
        }

        if (initialTrips != null) {
            for (int index = 0; index < initialTrips.length; index++) {
                if (initialTrips[index] != null) {
                    trips.add(initialTrips[index]);
                    tripRepository.add(initialTrips[index]);
                }
            }
        }

        try {
            recomputeAllClientSpending();
        } catch (InvalidClientDataException ignore) {
        }
    }
}
