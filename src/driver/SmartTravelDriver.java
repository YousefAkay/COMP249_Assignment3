// -----------------------------------------------------
// Assignment 3
// Class: SmartTravelDriver
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package driver;

import client.Client;
import exceptions.*;
import service.SmartTravelService;
import travel.*;
import visualization.DashboardGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SmartTravelDriver {

    /** Starts the full SmartTravel menu system and keeps running until the user exits. */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        /* Build the startup banner shown when the program launches. */
        System.out.println("\n===========================================");
        System.out.println("SMART TRAVEL MANAGEMENT SYSTEM");
        System.out.println("Developed by: Yousef Yousef & Hamza Shaheed");
        System.out.println("===========================================\n");

        /* Build the collection-backed service while preserving compatibility behavior. */
        SmartTravelService smartTravelService = new SmartTravelService();

        /* Keep the program running until the user selects the exit option. */
        boolean done = false;

        while (!done) {
            /* Display the main menu before reading the user's next action. */
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Client Management");
            System.out.println("2) Trip Management");
            System.out.println("3) Transportation Management");
            System.out.println("4) Accommodation Management");
            System.out.println("5) Additional Operations");
            System.out.println("6) Run A1 Predefined Scenario");
            System.out.println("7) Advanced Analytics");
            System.out.println("8) Load All Data");
            System.out.println("9) Save All Data");
            System.out.println("10) Run Predefined Scenario");
            System.out.println("11) Generate Dashboard");
            System.out.println("0) Exit");
            System.out.print("Enter choice: ");

            int mainChoice = readInt(keyboard);

            /* Route the user's menu choice to the matching operation. */
            switch (mainChoice) {
                case 1:
                    clientMenu(keyboard, smartTravelService);
                    break;
                case 2:
                    tripMenu(keyboard, smartTravelService);
                    break;
                case 3:
                    transportMenu(keyboard, smartTravelService);
                    break;
                case 4:
                    accommodationMenu(keyboard, smartTravelService);
                    break;
                case 5:
                    additionalOperationsMenu(keyboard, smartTravelService);
                    break;
                case 6:
                    runCoreScenarioDemo(smartTravelService);
                    break;
                case 7:
                    advancedAnalyticsMenu(keyboard, smartTravelService);
                    break;
                case 8:
                    loadAllData(smartTravelService);
                    break;
                case 9:
                    saveAllData(smartTravelService);
                    break;
                case 10:
                    runPersistenceScenarioDemo(smartTravelService);
                    break;
                case 11:
                    generateDashboard(smartTravelService);
                    break;
                case 0:
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        keyboard.close();
        System.out.println("\nThank You for Using SmartTravel, Goodbye!");
        System.out.println("Program terminated.");
    }

    /** Handles all client-related menu actions until the user chooses to go back. */
    private static void clientMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            /* Show the client submenu before reading the next client action. */
            System.out.println("\n=== CLIENT MENU ===");
            System.out.println("1) Add Client");
            System.out.println("2) Edit Client");
            System.out.println("3) Delete Client");
            System.out.println("4) List Clients");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int clientMenuChoice = readInt(keyboard);

            switch (clientMenuChoice) {
                case 1:
                    try {
                        /* Read the new client's basic details from the keyboard. */
                        System.out.print("First name: ");
                        String firstName = keyboard.nextLine();
                        System.out.print("Last name: ");
                        String lastName = keyboard.nextLine();
                        System.out.print("Email: ");
                        String email = keyboard.nextLine();

                        /* Create the client object and pass it to the service layer. */
                        Client client = new Client(firstName, lastName, email);
                        smartTravelService.addClient(client);
                        System.out.println("Added: " + client);

                    } catch (DuplicateEmailException | InvalidClientDataException exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 2:
                    try {
                        /* Read the target ID and the replacement client values. */
                        System.out.print("Enter client ID to edit: ");
                        String clientId = keyboard.nextLine();

                        System.out.print("New first name: ");
                        String newFirstName = keyboard.nextLine();

                        System.out.print("New last name: ");
                        String newLastName = keyboard.nextLine();

                        System.out.print("New email: ");
                        String newEmail = keyboard.nextLine();

                        /* Forward the edit request to the service layer. */
                        smartTravelService.editClient(clientId, newFirstName, newLastName, newEmail);
                        System.out.println("Client updated.");

                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 3:
                    try {
                        /* Read the client ID and request deletion through the service. */
                        System.out.print("Enter client ID to delete: ");
                        String clientId = keyboard.nextLine();
                        smartTravelService.deleteClient(clientId);
                        System.out.println("Client deleted.");
                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 4:
                    /* Print every active client currently stored in the array. */
                    for (int clientIndex = 0; clientIndex < smartTravelService.getClientCount(); clientIndex++) {
                        if (smartTravelService.getClients()[clientIndex] != null) {
                            System.out.println(smartTravelService.getClients()[clientIndex]);
                        }
                    }
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Handles all trip-related menu actions until the user chooses to go back. */
    private static void tripMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            /* Show the trip submenu before reading the next trip action. */
            System.out.println("\n=== TRIP MENU ===");
            System.out.println("1) Create Trip");
            System.out.println("2) Edit Trip");
            System.out.println("3) Cancel Trip");
            System.out.println("4) List All Trips");
            System.out.println("5) List Trips For Specific Client");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int tripMenuChoice = readInt(keyboard);

            switch (tripMenuChoice) {
                case 1:
                    try {
                        /* Read the booking IDs and trip details needed to create a trip. */
                        System.out.print("Client ID: ");
                        String clientId = keyboard.nextLine();

                        System.out.print("Accommodation ID (blank if none): ");
                        String accommodationId = keyboard.nextLine();

                        System.out.print("Transportation ID (blank if none): ");
                        String transportationId = keyboard.nextLine();

                        System.out.print("Destination: ");
                        String destination = keyboard.nextLine();

                        System.out.print("Duration (1-20 days): ");
                        int durationInDays = readInt(keyboard);

                        System.out.print("Base price (>=100): ");
                        double basePrice = readDouble(keyboard);

                        /* Resolve the referenced objects before building the trip. */
                        Client client = smartTravelService.findClientById(clientId);
                        Accommodation accommodation = accommodationId.trim().isEmpty()
                                ? null
                                : smartTravelService.findAccommodationById(accommodationId.trim());
                        Transportation transportation = transportationId.trim().isEmpty()
                                ? null
                                : smartTravelService.findTransportationById(transportationId.trim());

                        /* Create the trip and store it through the service layer. */
                        Trip trip = new Trip(client, transportation, accommodation, destination, durationInDays, basePrice);
                        smartTravelService.addTrip(trip);

                        System.out.println("Trip created: " + trip);

                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 2:
                    try {
                        /* Read the trip ID and the replacement values for the edit. */
                        System.out.print("Enter trip ID to edit: ");
                        String tripId = keyboard.nextLine();

                        System.out.print("New destination: ");
                        String destination = keyboard.nextLine();

                        System.out.print("New duration: ");
                        int durationInDays = readInt(keyboard);

                        System.out.print("New base price: ");
                        double basePrice = readDouble(keyboard);

                        System.out.print("New accommodation ID (blank if none): ");
                        String accommodationId = keyboard.nextLine();

                        System.out.print("New transportation ID (blank if none): ");
                        String transportationId = keyboard.nextLine();

                        /* Normalize blank optional IDs to null before calling the service. */
                        smartTravelService.editTrip(
                                tripId,
                                destination,
                                durationInDays,
                                basePrice,
                                accommodationId.trim().isEmpty() ? null : accommodationId.trim(),
                                transportationId.trim().isEmpty() ? null : transportationId.trim()
                        );

                        System.out.println("Trip updated.");

                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 3:
                    try {
                        /* Read the trip ID and cancel it through the service. */
                        System.out.print("Enter trip ID to cancel: ");
                        String tripId = keyboard.nextLine();
                        smartTravelService.cancelTrip(tripId);
                        System.out.println("Trip cancelled.");
                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 4:
                    /* Print every active trip along with its computed total cost. */
                    for (int tripIndex = 0; tripIndex < smartTravelService.getTripCount(); tripIndex++) {
                        if (smartTravelService.getTrips()[tripIndex] != null) {
                            System.out.println(smartTravelService.getTrips()[tripIndex] + " | total=" +
                                    String.format("%.2f", smartTravelService.getTrips()[tripIndex].calculateTotalCost()));
                        }
                    }
                    break;

                case 5:
                    /* Filter the trip list manually to show only one client's trips. */
                    System.out.print("Enter client ID: ");
                    String clientId = keyboard.nextLine();
                    boolean found = false;

                    for (int tripIndex = 0; tripIndex < smartTravelService.getTripCount(); tripIndex++) {
                        Trip trip = smartTravelService.getTrips()[tripIndex];
                        if (trip != null && clientId.equals(trip.getClientId())) {
                            System.out.println(trip + " | total=" + String.format("%.2f", trip.calculateTotalCost()));
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No trips found for that client.");
                    }
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Handles all transportation-related menu actions until the user chooses to go back. */
    private static void transportMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            /* Show the transportation submenu before reading the next action. */
            System.out.println("\n=== TRANSPORTATION MENU ===");
            System.out.println("1) Add Transportation");
            System.out.println("2) Remove Transportation");
            System.out.println("3) List Transportation By Type");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int transportMenuChoice = readInt(keyboard);

            switch (transportMenuChoice) {
                case 1:
                    /* Ask the user which concrete transportation subclass to create. */
                    System.out.println("\nChoose transport type:");
                    System.out.println("1) Flight");
                    System.out.println("2) Train");
                    System.out.println("3) Bus");
                    System.out.print("Enter choice: ");
                    int transportTypeChoice = readInt(keyboard);

                    try {
                        /* Read the common transportation fields first. */
                        System.out.print("Company name: ");
                        String companyName = keyboard.nextLine();
                        System.out.print("Departure city: ");
                        String departureCity = keyboard.nextLine();
                        System.out.print("Arrival city: ");
                        String arrivalCity = keyboard.nextLine();

                        if (transportTypeChoice == 1) {
                            /* Read flight-specific fields and add a Flight object. */
                            System.out.print("Airline name: ");
                            String airlineName = keyboard.nextLine();
                            System.out.print("Luggage allowance (kg): ");
                            double luggageAllowanceKg = readDouble(keyboard);

                            Flight flight = new Flight(companyName, departureCity, arrivalCity, airlineName, luggageAllowanceKg);
                            smartTravelService.addTransportation(flight);
                            System.out.println("Added: " + flight);

                        } else if (transportTypeChoice == 2) {
                            /* Read train-specific fields and add a Train object. */
                            System.out.print("Train type: ");
                            String trainType = keyboard.nextLine();

                            Train train = new Train(companyName, departureCity, arrivalCity, trainType);
                            smartTravelService.addTransportation(train);
                            System.out.println("Added: " + train);

                        } else if (transportTypeChoice == 3) {
                            /* Read bus-specific fields and add a Bus object. */
                            System.out.print("Number of stops: ");
                            int numberOfStops = readInt(keyboard);

                            Bus bus = new Bus(companyName, departureCity, arrivalCity, numberOfStops);
                            smartTravelService.addTransportation(bus);
                            System.out.println("Added: " + bus);

                        } else {
                            System.out.println("Invalid transport type.");
                        }

                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 2:
                    try {
                        /* Read the transportation ID and request removal through the service. */
                        System.out.print("Enter transportation ID to remove: ");
                        String transportId = keyboard.nextLine();
                        smartTravelService.removeTransportation(transportId);
                        System.out.println("Transportation removed.");
                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 3:
                    /* Filter the transportation array by concrete subclass type. */
                    System.out.println("1) Flight");
                    System.out.println("2) Train");
                    System.out.println("3) Bus");
                    System.out.print("Choose type: ");
                    int transportTypeFilter = readInt(keyboard);

                    for (int transportIndex = 0; transportIndex < smartTravelService.getTransportCount(); transportIndex++) {
                        Transportation transportation = smartTravelService.getTransportations()[transportIndex];
                        if (transportation == null) continue;

                        if (transportTypeFilter == 1 && transportation instanceof Flight) System.out.println(transportation);
                        if (transportTypeFilter == 2 && transportation instanceof Train) System.out.println(transportation);
                        if (transportTypeFilter == 3 && transportation instanceof Bus) System.out.println(transportation);
                    }
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Handles all accommodation-related menu actions until the user chooses to go back. */
    private static void accommodationMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            /* Show the accommodation submenu before reading the next action. */
            System.out.println("\n=== ACCOMMODATION MENU ===");
            System.out.println("1) Add Accommodation");
            System.out.println("2) Remove Accommodation");
            System.out.println("3) List Accommodation By Type");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int accommodationMenuChoice = readInt(keyboard);

            switch (accommodationMenuChoice) {
                case 1:
                    /* Ask the user which concrete accommodation subclass to create. */
                    System.out.println("\nChoose accommodation type:");
                    System.out.println("1) Hotel");
                    System.out.println("2) Hostel");
                    System.out.print("Enter choice: ");
                    int accommodationTypeChoice = readInt(keyboard);

                    try {
                        /* Read the common accommodation fields first. */
                        System.out.print("Name: ");
                        String name = keyboard.nextLine();
                        System.out.print("Location: ");
                        String location = keyboard.nextLine();
                        System.out.print("Price per night: ");
                        double pricePerNight = readDouble(keyboard);

                        if (accommodationTypeChoice == 1) {
                            /* Read hotel-specific fields and add a Hotel object. */
                            System.out.print("Stars (1-5): ");
                            int stars = readInt(keyboard);
                            Hotel hotel = new Hotel(name, location, pricePerNight, stars);
                            smartTravelService.addAccommodation(hotel);
                            System.out.println("Added: " + hotel);

                        } else if (accommodationTypeChoice == 2) {
                            /* Read hostel-specific fields and add a Hostel object. */
                            System.out.print("Shared room capacity: ");
                            int sharedRoomCapacity = readInt(keyboard);
                            Hostel hostel = new Hostel(name, location, pricePerNight, sharedRoomCapacity);
                            smartTravelService.addAccommodation(hostel);
                            System.out.println("Added: " + hostel);

                        } else {
                            System.out.println("Invalid accommodation type.");
                        }

                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 2:
                    try {
                        /* Read the accommodation ID and request removal through the service. */
                        System.out.print("Enter accommodation ID to remove: ");
                        String accommodationId = keyboard.nextLine();
                        smartTravelService.removeAccommodation(accommodationId);
                        System.out.println("Accommodation removed.");
                    } catch (Exception exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 3:
                    /* Filter the accommodation array by concrete subclass type. */
                    System.out.println("1) Hotel");
                    System.out.println("2) Hostel");
                    System.out.print("Choose type: ");
                    int accommodationTypeFilter = readInt(keyboard);

                    for (int accommodationIndex = 0; accommodationIndex < smartTravelService.getAccommodationCount(); accommodationIndex++) {
                        Accommodation accommodation = smartTravelService.getAccommodations()[accommodationIndex];
                        if (accommodation == null) continue;

                        if (accommodationTypeFilter == 1 && accommodation instanceof Hotel) System.out.println(accommodation);
                        if (accommodationTypeFilter == 2 && accommodation instanceof Hostel) System.out.println(accommodation);
                    }
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Handles the extra utility operations such as deep-copy tests and total-cost checks. */
    private static void additionalOperationsMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            /* Show the additional operations menu before reading the next action. */
            System.out.println("\n=== ADDITIONAL OPERATIONS ===");
            System.out.println("1) Display most expensive trip");
            System.out.println("2) Calculate total cost of a trip");
            System.out.println("3) Deep copy transportation array");
            System.out.println("4) Deep copy accommodation array");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int additionalChoice = readInt(keyboard);

            switch (additionalChoice) {
                case 1:
                    try {
                        /* Fetch and print the most expensive trip currently stored. */
                        Trip mostExpensiveTrip = smartTravelService.findMostExpensiveTrip();
                        System.out.println("\nMost expensive trip:");
                        System.out.println(mostExpensiveTrip);
                        System.out.println("Total cost: " + mostExpensiveTrip.calculateTotalCost());
                    } catch (EntityNotFoundException exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 2:
                    /* Show indexed trips first so the user can choose a valid array position. */
                    if (smartTravelService.getTripCount() == 0) {
                        System.out.println("No trips available.");
                        break;
                    }

                    System.out.println("\nTrips:");
                    for (int tripIndex = 0; tripIndex < smartTravelService.getTripCount(); tripIndex++) {
                        System.out.println(tripIndex + " -> " + smartTravelService.getTrips()[tripIndex]);
                    }

                    System.out.print("Enter trip index: ");
                    int tripIndex = readInt(keyboard);

                    try {
                        /* Calculate the total cost using the selected trip index. */
                        double totalCost = smartTravelService.calculateTripTotal(tripIndex);
                        System.out.println("Total cost = " + totalCost);
                    } catch (InvalidTripDataException exception) {
                        System.out.println("ERROR: " + exception.getMessage());
                    }
                    break;

                case 3:
                    /* Demonstrate that transportation deep copies do not affect originals. */
                    if (smartTravelService.getTransportCount() == 0) {
                        System.out.println("No transportation records available.");
                        break;
                    }

                    Transportation[] copiedTransportations = smartTravelService.deepCopyTransportationArray();

                    System.out.println("\nBefore modifying copied transportation:");
                    System.out.println("Original[0] = " + smartTravelService.getTransportations()[0]);
                    System.out.println("Copy[0]     = " + copiedTransportations[0]);

                    try {
                        /* Mutate only the copied object to prove the original stays unchanged. */
                        if (copiedTransportations[0] instanceof Flight) {
                            ((Flight) copiedTransportations[0]).setAirlineName("ModifiedCopyAir");
                        } else if (copiedTransportations[0] instanceof Train) {
                            ((Train) copiedTransportations[0]).setTrainType("ModifiedType");
                        } else if (copiedTransportations[0] instanceof Bus) {
                            ((Bus) copiedTransportations[0]).setNumberOfStops(((Bus) copiedTransportations[0]).getNumberOfStops() + 1);
                        }
                    } catch (Exception exception) {
                        System.out.println("Modification error: " + exception.getMessage());
                    }

                    System.out.println("\nAfter modifying copied transportation:");
                    System.out.println("Original[0] = " + smartTravelService.getTransportations()[0]);
                    System.out.println("Copy[0]     = " + copiedTransportations[0]);
                    break;

                case 4:
                    /* Demonstrate that accommodation deep copies do not affect originals. */
                    if (smartTravelService.getAccommodationCount() == 0) {
                        System.out.println("No accommodation records available.");
                        break;
                    }

                    Accommodation[] copiedAccommodations = smartTravelService.deepCopyAccommodationArray();

                    System.out.println("\nBefore modifying copied accommodation:");
                    System.out.println("Original[0] = " + smartTravelService.getAccommodations()[0]);
                    System.out.println("Copy[0]     = " + copiedAccommodations[0]);

                    try {
                        // Mutate only the copied object to prove the original stays unchanged.
                        if (copiedAccommodations[0] instanceof Hotel) {
                            ((Hotel) copiedAccommodations[0]).setStars(1);
                        } else if (copiedAccommodations[0] instanceof Hostel) {
                            ((Hostel) copiedAccommodations[0]).setSharedRoomCapacity(((Hostel) copiedAccommodations[0]).getSharedRoomCapacity() + 1);
                        }
                    } catch (Exception exception) {
                        System.out.println("Modification error: " + exception.getMessage());
                    }

                    System.out.println("\nAfter modifying copied accommodation:");
                    System.out.println("Original[0] = " + smartTravelService.getAccommodations()[0]);
                    System.out.println("Copy[0]     = " + copiedAccommodations[0]);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Prints all currently stored data grouped by category for quick review. */
    private static void listAllSummary(SmartTravelService smartTravelService) {
        System.out.println("\n--- Clients ---");
        for (int clientIndex = 0; clientIndex < smartTravelService.getClientCount(); clientIndex++) {
            if (smartTravelService.getClients()[clientIndex] != null) {
                System.out.println(smartTravelService.getClients()[clientIndex]);
            }
        }

        System.out.println("\n--- Transportation (ALL) ---");
        for (int transportIndex = 0; transportIndex < smartTravelService.getTransportCount(); transportIndex++) {
            if (smartTravelService.getTransportations()[transportIndex] != null) {
                System.out.println(smartTravelService.getTransportations()[transportIndex]);
            }
        }

        System.out.println("\n--- Accommodation (ALL) ---");
        for (int accommodationIndex = 0; accommodationIndex < smartTravelService.getAccommodationCount(); accommodationIndex++) {
            if (smartTravelService.getAccommodations()[accommodationIndex] != null) {
                System.out.println(smartTravelService.getAccommodations()[accommodationIndex]);
            }
        }

        System.out.println("\n--- Trips ---");
        for (int tripIndex = 0; tripIndex < smartTravelService.getTripCount(); tripIndex++) {
            if (smartTravelService.getTrips()[tripIndex] != null) {
                Trip trip = smartTravelService.getTrips()[tripIndex];
                System.out.println(trip + " | total=" + String.format("%.2f", trip.calculateTotalCost()));
            }
        }
    }

    /** Handles the A3 analytics submenu while preserving the rest of the driver flow. */
    private static void advancedAnalyticsMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== ADVANCED ANALYTICS ===");
            System.out.println("7.1) Trips by Destination");
            System.out.println("7.2) Trips by Cost Range");
            System.out.println("7.3) Top Clients by Spending");
            System.out.println("7.4) Recent Trips");
            System.out.println("7.5) Smart Sort Collections");
            System.out.println("7.6) Back");
            System.out.print("Enter choice: ");

            String analyticsChoice = keyboard.nextLine().trim();

            switch (analyticsChoice) {
                case "7.1":
                case "1":
                    System.out.print("Enter destination: ");
                    String destination = keyboard.nextLine();
                    List<Trip> destinationTrips = smartTravelService.filterTripsByDestination(destination);
                    printTripResults(destinationTrips);
                    break;

                case "7.2":
                case "2":
                    System.out.print("Enter minimum total cost: ");
                    double minimumCost = readDouble(keyboard);
                    System.out.print("Enter maximum total cost: ");
                    double maximumCost = readDouble(keyboard);

                    if (maximumCost < minimumCost) {
                        System.out.println("ERROR: maximum cost must be >= minimum cost.");
                        break;
                    }

                    List<Trip> rangedTrips = smartTravelService.filterTripsByTotalCostRange(minimumCost, maximumCost);
                    printTripResults(rangedTrips);
                    break;

                case "7.3":
                case "3":
                    printClients(smartTravelService.getTopClientsBySpending());
                    break;

                case "7.4":
                case "4":
                    printTripResults(smartTravelService.getRecentTrips());
                    break;

                case "7.5":
                case "5":
                    smartSortCollectionsMenu(keyboard, smartTravelService);
                    break;

                case "7.6":
                case "6":
                back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Displays one of the naturally sorted collections required by the A3 analytics workflow. */
    private static void smartSortCollectionsMenu(Scanner keyboard, SmartTravelService smartTravelService) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== SMART SORT COLLECTIONS ===");
            System.out.println("1) Clients");
            System.out.println("2) Trips");
            System.out.println("3) Accommodations");
            System.out.println("4) Transportations");
            System.out.println("5) Back");
            System.out.print("Enter choice: ");

            int sortChoice = readInt(keyboard);

            switch (sortChoice) {
                case 1:
                    printClients(smartTravelService.getSmartSortedClients());
                    break;

                case 2:
                    printTripResults(smartTravelService.getSmartSortedTrips());
                    break;

                case 3:
                    printAccommodations(smartTravelService.getSmartSortedAccommodations());
                    break;

                case 4:
                    printTransportations(smartTravelService.getSmartSortedTransportations());
                    break;

                case 5:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Prints trip results consistently for analytics screens. */
    private static void printTripResults(List<Trip> trips) {
        if (trips == null || trips.isEmpty()) {
            System.out.println("No trips found.");
            return;
        }

        for (int index = 0; index < trips.size(); index++) {
            Trip trip = trips.get(index);
            System.out.println(trip + " | total=" + String.format("%.2f", trip.calculateTotalCost()));
        }
    }

    /** Prints client results consistently for analytics screens. */
    private static void printClients(List<Client> clients) {
        if (clients == null || clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }

        for (int index = 0; index < clients.size(); index++) {
            System.out.println(clients.get(index));
        }
    }

    /** Prints accommodation results consistently for analytics screens. */
    private static void printAccommodations(List<Accommodation> accommodations) {
        if (accommodations == null || accommodations.isEmpty()) {
            System.out.println("No accommodations found.");
            return;
        }

        for (int index = 0; index < accommodations.size(); index++) {
            System.out.println(accommodations.get(index));
        }
    }

    /** Prints transportation results consistently for analytics screens. */
    private static void printTransportations(List<Transportation> transportations) {
        if (transportations == null || transportations.isEmpty()) {
            System.out.println("No transportations found.");
            return;
        }

        for (int index = 0; index < transportations.size(); index++) {
            System.out.println(transportations.get(index));
        }
    }

    /** Loads all persisted CSV data through the service layer. */
    private static void loadAllData(SmartTravelService smartTravelService) {
        try {
            smartTravelService.loadAllData("data");
            System.out.println("Loaded all data from data/*.csv (errors logged to output/logs/errors.txt).");
        } catch (IOException exception) {
            System.out.println("LOAD ERROR: " + exception.getMessage());
        }
    }

    /** Saves all current in-memory data to the CSV output folder. */
    private static void saveAllData(SmartTravelService smartTravelService) {
        try {
            smartTravelService.saveAllData("output/data");
            System.out.println("Saved all data to output/data/*.csv");
        } catch (IOException exception) {
            System.out.println("SAVE ERROR: " + exception.getMessage());
        }
    }

    /** Calls the dashboard generator using the current service data. */
    private static void generateDashboard(SmartTravelService smartTravelService) {
        try {
            DashboardGenerator.generateDashboard(
                    smartTravelService.getClients(),
                    smartTravelService.getClientCount(),
                    smartTravelService.getTrips(),
                    smartTravelService.getTripCount()
            );
            System.out.println("Dashboard generated in output/dashboard/ (and output/charts/ created).");
        } catch (IOException exception) {
            System.out.println("DASHBOARD ERROR: " + exception.getMessage());
        }
    }

    /** Runs the Assignment 3 core scenario to demonstrate the main system features. */
    private static void runCoreScenarioDemo(SmartTravelService smartTravelService) {
        try {
            // Start from a clean in-memory state before building scenario data. */
            smartTravelService.clearAllData();

            Client clientOne = new Client("Malcolm", "White", "malcolm@example.com");
            Client clientTwo = new Client("Sara", "Black", "sara@example.com");
            Client clientThree = new Client("Danny", "Brown", "danny@example.com");

            smartTravelService.addClient(clientOne);
            smartTravelService.addClient(clientTwo);
            smartTravelService.addClient(clientThree);

            Flight flightOne = new Flight("AirX", "Montreal", "Paris", "AirX", 25.0);
            Flight flightTwo = new Flight("SkyJet", "Dubai", "Rome", "SkyJet", 18.0);
            Train trainOne = new Train("RailPro", "Toronto", "Ottawa", "HighSpeed");
            Train trainTwo = new Train("EuroRail", "Rome", "Milan", "Standard");
            Bus busOne = new Bus("Greyhound", "NYC", "Boston", 3);
            Bus busTwo = new Bus("CoachX", "Madrid", "Valencia", 2);

            smartTravelService.addTransportation(flightOne);
            smartTravelService.addTransportation(flightTwo);
            smartTravelService.addTransportation(trainOne);
            smartTravelService.addTransportation(trainTwo);
            smartTravelService.addTransportation(busOne);
            smartTravelService.addTransportation(busTwo);

            Hotel hotelOne = new Hotel("Hilton", "Rome", 280.0, 4);
            Hotel hotelTwo = new Hotel("Sheraton", "Paris", 320.0, 5);
            Hostel hostelOne = new Hostel("Backpackers", "Rome", 55.0, 6);
            Hostel hostelTwo = new Hostel("CityHostel", "Ottawa", 70.0, 4);

            smartTravelService.addAccommodation(hotelOne);
            smartTravelService.addAccommodation(hotelTwo);
            smartTravelService.addAccommodation(hostelOne);
            smartTravelService.addAccommodation(hostelTwo);

            Trip tripOne = new Trip(clientOne, flightOne, hotelOne, "Rome", 5, 1800.0);
            Trip tripTwo = new Trip(clientTwo, trainOne, hostelTwo, "Ottawa", 3, 500.0);
            Trip tripThree = new Trip(clientThree, busTwo, hostelOne, "Valencia", 4, 700.0);

            smartTravelService.addTrip(tripOne);
            smartTravelService.addTrip(tripTwo);
            smartTravelService.addTrip(tripThree);

            System.out.println("\n===== CORE SCENARIO DEMO =====");
            listAllSummary(smartTravelService);

            /* Demonstrate equality behavior across different object cases. */
            System.out.println("\n===== EQUALS TESTS =====");
            System.out.println("Different classes (Flight vs Hotel): " + flightOne.equals(hotelOne));
            System.out.println("Same class, different attributes (Hotel h1 vs h2): " + hotelOne.equals(hotelTwo));
            Hotel hotelThree = new Hotel("Hilton", "Rome", 280.0, 4);
            System.out.println("Same class, identical attributes except ID: " + hotelOne.equals(hotelThree));

            /* Show the most expensive trip returned by the service layer. */
            System.out.println("\n===== MOST EXPENSIVE TRIP =====");
            Trip mostExpensiveTrip = smartTravelService.findMostExpensiveTrip();
            System.out.println(mostExpensiveTrip);
            System.out.println("Total = " + mostExpensiveTrip.calculateTotalCost());

            /* Prove the transportation deep copy uses separate objects. */
            System.out.println("\n===== DEEP COPY TEST: TRANSPORT =====");
            Transportation[] copiedTransportations = smartTravelService.deepCopyTransportationArray();
            System.out.println("Original[0] = " + smartTravelService.getTransportations()[0]);
            System.out.println("Copy[0]     = " + copiedTransportations[0]);
            if (copiedTransportations[0] instanceof Flight) {
                ((Flight) copiedTransportations[0]).setAirlineName("ChangedAirline");
            }
            System.out.println("Original[0] = " + smartTravelService.getTransportations()[0]);
            System.out.println("Copy[0]     = " + copiedTransportations[0]);

            /* Prove the accommodation deep copy uses separate objects. */
            System.out.println("\n===== DEEP COPY TEST: ACCOMMODATION =====");
            Accommodation[] copiedAccommodations = smartTravelService.deepCopyAccommodationArray();
            System.out.println("Original[0] = " + smartTravelService.getAccommodations()[0]);
            System.out.println("Copy[0]     = " + copiedAccommodations[0]);
            if (copiedAccommodations[0] instanceof Hotel) {
                ((Hotel) copiedAccommodations[0]).setStars(1);
            }
            System.out.println("Original[0] = " + smartTravelService.getAccommodations()[0]);
            System.out.println("Copy[0]     = " + copiedAccommodations[0]);

        } catch (Exception exception) {
            System.out.println("SCENARIO ERROR: " + exception.getMessage());
        }
    }

    /** Runs the Assignment 3 persistence scenario to demonstrate loading, saving, validation, and dashboard generation. */
    private static void runPersistenceScenarioDemo(SmartTravelService smartTravelService) {
        smartTravelService.clearAllData();
        System.out.println("\n===== RUNNING PERSISTENCE SCENARIO DEMO =====");

        /* First load any persisted data into memory. */
        loadAllData(smartTravelService);

        /* Show the loaded state before adding new demo content. */
        System.out.println("\n--- After Load ---");
        listAllSummary(smartTravelService);

        /* Intentionally trigger a duplicate email case to show exception handling. */
        if (smartTravelService.getClientCount() > 0) {
            try {
                String duplicateEmail = smartTravelService.getClients()[0].getEmail();
                smartTravelService.addClient(new Client("Test", "User", duplicateEmail));
            } catch (DuplicateEmailException exception) {
                System.out.println("Caught expected DuplicateEmailException: " + exception.getMessage());
            } catch (InvalidClientDataException exception) {
                System.out.println("Unexpected client validation error: " + exception.getMessage());
            }
        }

        // Add one full set of valid demo objects to the current data. */
        try {
            Client demoClient = new Client("A3", "Demo", "a3demo@example.com");
            smartTravelService.addClient(demoClient);

            Flight demoFlight = new Flight("DemoAir", "Montreal", "Rome", "DemoAir", 23.0);
            smartTravelService.addTransportation(demoFlight);

            Hotel demoHotel = new Hotel("Demo Hotel", "Rome", 220.0, 4);
            smartTravelService.addAccommodation(demoHotel);

            Trip demoTrip = new Trip(demoClient, demoFlight, demoHotel, "Rome", 5, 1500.0);
            smartTravelService.addTrip(demoTrip);

            System.out.println("\nAdded valid Assignment 3 demo data:");
            System.out.println(demoClient);
            System.out.println(demoFlight);
            System.out.println(demoHotel);
            System.out.println(demoTrip);
            System.out.println("Trip total = " + String.format("%.2f", demoTrip.calculateTotalCost()));

        } catch (Exception exception) {
            System.out.println("Unexpected add error in persistence scenario: " + exception.getMessage());
        }

        // Intentionally trigger a validation failure for transportation rules. */
        try {
            new Bus("BadBus", "CityA", "CityB", 0);
        } catch (InvalidTransportDataException exception) {
            System.out.println("Caught expected InvalidTransportDataException: " + exception.getMessage());
        }

        /* Save the current state and generate the dashboard output. */
        saveAllData(smartTravelService);
        generateDashboard(smartTravelService);

        System.out.println("\n--- Final Summary ---");
        listAllSummary(smartTravelService);

        System.out.println("\n===== PERSISTENCE SCENARIO DEMO COMPLETE =====");
    }

    /** Reads an integer safely and keeps asking until the input is valid. */
    private static int readInt(Scanner keyboard) {
        while (true) {
            String input = keyboard.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException exception) {
                System.out.print("Enter an integer: ");
            }
        }
    }

    /** Reads a double safely and keeps asking until the input is valid. */
    private static double readDouble(Scanner keyboard) {
        while (true) {
            String input = keyboard.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException exception) {
                System.out.print("Enter a number: ");
            }
        }
    }
}
