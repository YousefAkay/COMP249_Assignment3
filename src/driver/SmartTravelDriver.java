// -----------------------------------------------------
// Assignment 2
// Class: SmartTravelDriver
// Written by: Yousef Yousef (40299095) & Hamza Shadeed
// -----------------------------------------------------

package driver;

import client.Client;
import exceptions.*;
import service.SmartTravelService;
import travel.*;
import visualization.DashboardGenerator;

import java.io.IOException;
import java.util.Scanner;

public class SmartTravelDriver {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("\n===========================================");
        System.out.println("SMART TRAVEL MANAGEMENT SYSTEM");
        System.out.println("Developed by: Yousef Yousef & Hamza Shadeed");
        System.out.println("===========================================\n");

        Client[] clients = new Client[100];
        Trip[] trips = new Trip[200];
        Transportation[] transportations = new Transportation[50];
        Accommodation[] accommodations = new Accommodation[50];

        SmartTravelService svc = new SmartTravelService(clients, trips, transportations, accommodations);

        boolean done = false;

        while (!done) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1) Client Management");
            System.out.println("2) Trip Management");
            System.out.println("3) Transportation Management");
            System.out.println("4) Accommodation Management");
            System.out.println("5) Additional Operations");
            System.out.println("6) Run Predefined Scenario");
            System.out.println("7) List All Data Summary");
            System.out.println("8) Load All Data");
            System.out.println("9) Save All Data");
            System.out.println("10) Run A2 Predefined Scenario");
            System.out.println("11) Generate Dashboard");
            System.out.println("0) Exit");
            System.out.print("Enter choice: ");

            int choice = readInt(keyboard);

            switch (choice) {
                case 1:
                    clientMenu(keyboard, svc);
                    break;
                case 2:
                    tripMenu(keyboard, svc);
                    break;
                case 3:
                    transportMenu(keyboard, svc);
                    break;
                case 4:
                    accommodationMenu(keyboard, svc);
                    break;
                case 5:
                    additionalOperationsMenu(keyboard, svc);
                    break;
                case 6:
                    runA1PredefinedScenario(svc);
                    break;
                case 7:
                    listAllSummary(svc);
                    break;
                case 8:
                    loadAllData(svc);
                    break;
                case 9:
                    saveAllData(svc);
                    break;
                case 10:
                    runA2PredefinedScenario(svc);
                    break;
                case 11:
                    generateDashboard(svc);
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

    private static void clientMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== CLIENT MENU ===");
            System.out.println("1) Add Client");
            System.out.println("2) Edit Client");
            System.out.println("3) Delete Client");
            System.out.println("4) List Clients");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int c = readInt(keyboard);

            switch (c) {
                case 1:
                    try {
                        System.out.print("First name: ");
                        String fn = keyboard.nextLine();
                        System.out.print("Last name: ");
                        String ln = keyboard.nextLine();
                        System.out.print("Email: ");
                        String em = keyboard.nextLine();

                        Client cl = new Client(fn, ln, em);
                        svc.addClient(cl);
                        System.out.println("Added: " + cl);

                    } catch (DuplicateEmailException | InvalidClientDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Enter client ID to edit: ");
                        String clientId = keyboard.nextLine();

                        System.out.print("New first name: ");
                        String newFirst = keyboard.nextLine();

                        System.out.print("New last name: ");
                        String newLast = keyboard.nextLine();

                        System.out.print("New email: ");
                        String newEmail = keyboard.nextLine();

                        svc.editClient(clientId, newFirst, newLast, newEmail);
                        System.out.println("Client updated.");

                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter client ID to delete: ");
                        String clientId = keyboard.nextLine();
                        svc.deleteClient(clientId);
                        System.out.println("Client deleted.");
                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 4:
                    for (int i = 0; i < svc.getClientCount(); i++) {
                        if (svc.getClients()[i] != null) {
                            System.out.println(svc.getClients()[i]);
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

    private static void tripMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== TRIP MENU ===");
            System.out.println("1) Create Trip");
            System.out.println("2) Edit Trip");
            System.out.println("3) Cancel Trip");
            System.out.println("4) List All Trips");
            System.out.println("5) List Trips For Specific Client");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int c = readInt(keyboard);

            switch (c) {
                case 1:
                    try {
                        System.out.print("Client ID: ");
                        String cid = keyboard.nextLine();

                        System.out.print("Accommodation ID (blank if none): ");
                        String aid = keyboard.nextLine();

                        System.out.print("Transportation ID (blank if none): ");
                        String tid = keyboard.nextLine();

                        System.out.print("Destination: ");
                        String dest = keyboard.nextLine();

                        System.out.print("Duration (1-20 days): ");
                        int days = readInt(keyboard);

                        System.out.print("Base price (>=100): ");
                        double base = readDouble(keyboard);

                        Client client = svc.findClientById(cid);
                        Accommodation ac = aid.trim().isEmpty() ? null : svc.findAccommodationById(aid.trim());
                        Transportation tr = tid.trim().isEmpty() ? null : svc.findTransportationById(tid.trim());

                        Trip trip = new Trip(client, tr, ac, dest, days, base);
                        svc.addTrip(trip);

                        System.out.println("Trip created: " + trip);

                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Enter trip ID to edit: ");
                        String tripId = keyboard.nextLine();

                        System.out.print("New destination: ");
                        String destination = keyboard.nextLine();

                        System.out.print("New duration: ");
                        int duration = readInt(keyboard);

                        System.out.print("New base price: ");
                        double basePrice = readDouble(keyboard);

                        System.out.print("New accommodation ID (blank if none): ");
                        String accommodationId = keyboard.nextLine();

                        System.out.print("New transportation ID (blank if none): ");
                        String transportationId = keyboard.nextLine();

                        svc.editTrip(
                                tripId,
                                destination,
                                duration,
                                basePrice,
                                accommodationId.trim().isEmpty() ? null : accommodationId.trim(),
                                transportationId.trim().isEmpty() ? null : transportationId.trim()
                        );

                        System.out.println("Trip updated.");

                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter trip ID to cancel: ");
                        String tripId = keyboard.nextLine();
                        svc.cancelTrip(tripId);
                        System.out.println("Trip cancelled.");
                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 4:
                    for (int i = 0; i < svc.getTripCount(); i++) {
                        if (svc.getTrips()[i] != null) {
                            System.out.println(svc.getTrips()[i] + " | total=" +
                                    String.format("%.2f", svc.getTrips()[i].calculateTotalCost()));
                        }
                    }
                    break;

                case 5:
                    System.out.print("Enter client ID: ");
                    String clientId = keyboard.nextLine();
                    boolean found = false;

                    for (int i = 0; i < svc.getTripCount(); i++) {
                        Trip t = svc.getTrips()[i];
                        if (t != null && clientId.equals(t.getClientId())) {
                            System.out.println(t + " | total=" + String.format("%.2f", t.calculateTotalCost()));
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

    private static void transportMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== TRANSPORTATION MENU ===");
            System.out.println("1) Add Transportation");
            System.out.println("2) Remove Transportation");
            System.out.println("3) List Transportation By Type");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int c = readInt(keyboard);

            switch (c) {
                case 1:
                    System.out.println("\nChoose transport type:");
                    System.out.println("1) Flight");
                    System.out.println("2) Train");
                    System.out.println("3) Bus");
                    System.out.print("Enter choice: ");
                    int t = readInt(keyboard);

                    try {
                        System.out.print("Company name: ");
                        String company = keyboard.nextLine();
                        System.out.print("Departure city: ");
                        String dep = keyboard.nextLine();
                        System.out.print("Arrival city: ");
                        String arr = keyboard.nextLine();

                        if (t == 1) {
                            System.out.print("Airline name: ");
                            String airline = keyboard.nextLine();
                            System.out.print("Luggage allowance (kg): ");
                            double lug = readDouble(keyboard);

                            Flight f = new Flight(company, dep, arr, airline, lug);
                            svc.addTransportation(f);
                            System.out.println("Added: " + f);

                        } else if (t == 2) {
                            System.out.print("Train type: ");
                            String trainType = keyboard.nextLine();

                            Train tr = new Train(company, dep, arr, trainType);
                            svc.addTransportation(tr);
                            System.out.println("Added: " + tr);

                        } else if (t == 3) {
                            System.out.print("Number of stops: ");
                            int stops = readInt(keyboard);

                            Bus b = new Bus(company, dep, arr, stops);
                            svc.addTransportation(b);
                            System.out.println("Added: " + b);

                        } else {
                            System.out.println("Invalid transport type.");
                        }

                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Enter transportation ID to remove: ");
                        String transportId = keyboard.nextLine();
                        svc.removeTransportation(transportId);
                        System.out.println("Transportation removed.");
                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("1) Flight");
                    System.out.println("2) Train");
                    System.out.println("3) Bus");
                    System.out.print("Choose type: ");
                    int type = readInt(keyboard);

                    for (int i = 0; i < svc.getTransportCount(); i++) {
                        Transportation tr = svc.getTransportations()[i];
                        if (tr == null) continue;

                        if (type == 1 && tr instanceof Flight) System.out.println(tr);
                        if (type == 2 && tr instanceof Train) System.out.println(tr);
                        if (type == 3 && tr instanceof Bus) System.out.println(tr);
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

    private static void accommodationMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== ACCOMMODATION MENU ===");
            System.out.println("1) Add Accommodation");
            System.out.println("2) Remove Accommodation");
            System.out.println("3) List Accommodation By Type");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int c = readInt(keyboard);

            switch (c) {
                case 1:
                    System.out.println("\nChoose accommodation type:");
                    System.out.println("1) Hotel");
                    System.out.println("2) Hostel");
                    System.out.print("Enter choice: ");
                    int a = readInt(keyboard);

                    try {
                        System.out.print("Name: ");
                        String name = keyboard.nextLine();
                        System.out.print("Location: ");
                        String loc = keyboard.nextLine();
                        System.out.print("Price per night: ");
                        double ppn = readDouble(keyboard);

                        if (a == 1) {
                            System.out.print("Stars (1-5): ");
                            int stars = readInt(keyboard);
                            Hotel h = new Hotel(name, loc, ppn, stars);
                            svc.addAccommodation(h);
                            System.out.println("Added: " + h);

                        } else if (a == 2) {
                            System.out.print("Shared room capacity: ");
                            int cap = readInt(keyboard);
                            Hostel ho = new Hostel(name, loc, ppn, cap);
                            svc.addAccommodation(ho);
                            System.out.println("Added: " + ho);

                        } else {
                            System.out.println("Invalid accommodation type.");
                        }

                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.print("Enter accommodation ID to remove: ");
                        String accommodationId = keyboard.nextLine();
                        svc.removeAccommodation(accommodationId);
                        System.out.println("Accommodation removed.");
                    } catch (Exception ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("1) Hotel");
                    System.out.println("2) Hostel");
                    System.out.print("Choose type: ");
                    int type = readInt(keyboard);

                    for (int i = 0; i < svc.getAccommodationCount(); i++) {
                        Accommodation ac = svc.getAccommodations()[i];
                        if (ac == null) continue;

                        if (type == 1 && ac instanceof Hotel) System.out.println(ac);
                        if (type == 2 && ac instanceof Hostel) System.out.println(ac);
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

    private static void additionalOperationsMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=== ADDITIONAL OPERATIONS ===");
            System.out.println("1) Display most expensive trip");
            System.out.println("2) Calculate total cost of a trip");
            System.out.println("3) Deep copy transportation array");
            System.out.println("4) Deep copy accommodation array");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");

            int choice = readInt(keyboard);

            switch (choice) {
                case 1:
                    try {
                        Trip t = svc.findMostExpensiveTrip();
                        System.out.println("\nMost expensive trip:");
                        System.out.println(t);
                        System.out.println("Total cost: " + t.calculateTotalCost());
                    } catch (EntityNotFoundException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    break;

                case 2:
                    if (svc.getTripCount() == 0) {
                        System.out.println("No trips available.");
                        break;
                    }

                    System.out.println("\nTrips:");
                    for (int i = 0; i < svc.getTripCount(); i++) {
                        System.out.println(i + " -> " + svc.getTrips()[i]);
                    }

                    System.out.print("Enter trip index: ");
                    int index = readInt(keyboard);

                    try {
                        double total = svc.calculateTripTotal(index);
                        System.out.println("Total cost = " + total);
                    } catch (InvalidTripDataException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    break;

                case 3:
                    if (svc.getTransportCount() == 0) {
                        System.out.println("No transportation records available.");
                        break;
                    }

                    Transportation[] copyT = svc.deepCopyTransportationArray();

                    System.out.println("\nBefore modifying copied transportation:");
                    System.out.println("Original[0] = " + svc.getTransportations()[0]);
                    System.out.println("Copy[0]     = " + copyT[0]);

                    try {
                        if (copyT[0] instanceof Flight) {
                            ((Flight) copyT[0]).setAirlineName("ModifiedCopyAir");
                        } else if (copyT[0] instanceof Train) {
                            ((Train) copyT[0]).setTrainType("ModifiedType");
                        } else if (copyT[0] instanceof Bus) {
                            ((Bus) copyT[0]).setNumberOfStops(((Bus) copyT[0]).getNumberOfStops() + 1);
                        }
                    } catch (Exception e) {
                        System.out.println("Modification error: " + e.getMessage());
                    }

                    System.out.println("\nAfter modifying copied transportation:");
                    System.out.println("Original[0] = " + svc.getTransportations()[0]);
                    System.out.println("Copy[0]     = " + copyT[0]);
                    break;

                case 4:
                    if (svc.getAccommodationCount() == 0) {
                        System.out.println("No accommodation records available.");
                        break;
                    }

                    Accommodation[] copyA = svc.deepCopyAccommodationArray();

                    System.out.println("\nBefore modifying copied accommodation:");
                    System.out.println("Original[0] = " + svc.getAccommodations()[0]);
                    System.out.println("Copy[0]     = " + copyA[0]);

                    try {
                        if (copyA[0] instanceof Hotel) {
                            ((Hotel) copyA[0]).setStars(1);
                        } else if (copyA[0] instanceof Hostel) {
                            ((Hostel) copyA[0]).setSharedRoomCapacity(((Hostel) copyA[0]).getSharedRoomCapacity() + 1);
                        }
                    } catch (Exception e) {
                        System.out.println("Modification error: " + e.getMessage());
                    }

                    System.out.println("\nAfter modifying copied accommodation:");
                    System.out.println("Original[0] = " + svc.getAccommodations()[0]);
                    System.out.println("Copy[0]     = " + copyA[0]);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listAllSummary(SmartTravelService svc) {
        System.out.println("\n--- Clients ---");
        for (int i = 0; i < svc.getClientCount(); i++) {
            if (svc.getClients()[i] != null) System.out.println(svc.getClients()[i]);
        }

        System.out.println("\n--- Transportation (ALL) ---");
        for (int i = 0; i < svc.getTransportCount(); i++) {
            if (svc.getTransportations()[i] != null) System.out.println(svc.getTransportations()[i]);
        }

        System.out.println("\n--- Accommodation (ALL) ---");
        for (int i = 0; i < svc.getAccommodationCount(); i++) {
            if (svc.getAccommodations()[i] != null) System.out.println(svc.getAccommodations()[i]);
        }

        System.out.println("\n--- Trips ---");
        for (int i = 0; i < svc.getTripCount(); i++) {
            if (svc.getTrips()[i] != null) {
                Trip t = svc.getTrips()[i];
                System.out.println(t + " | total=" + String.format("%.2f", t.calculateTotalCost()));
            }
        }
    }

    private static void loadAllData(SmartTravelService svc) {
        try {
            svc.loadAllData("data");
            System.out.println("Loaded all data from data/*.csv (errors logged to output/logs/errors.txt).");
        } catch (IOException ex) {
            System.out.println("LOAD ERROR: " + ex.getMessage());
        }
    }

    private static void saveAllData(SmartTravelService svc) {
        try {
            svc.saveAllData("output/data");
            System.out.println("Saved all data to output/data/*.csv");
        } catch (IOException ex) {
            System.out.println("SAVE ERROR: " + ex.getMessage());
        }
    }

    private static void generateDashboard(SmartTravelService svc) {
        try {
            DashboardGenerator.generateDashboard(
                    svc.getClients(),
                    svc.getClientCount(),
                    svc.getTrips(),
                    svc.getTripCount()
            );
            System.out.println("Dashboard generated in output/dashboard/ (and output/charts/ created).");
        } catch (IOException ex) {
            System.out.println("DASHBOARD ERROR: " + ex.getMessage());
        }
    }

    private static void runA1PredefinedScenario(SmartTravelService svc) {
        try {
            svc.clearAllData();

            Client c1 = new Client("Malcolm", "White", "malcolm@example.com");
            Client c2 = new Client("Sara", "Black", "sara@example.com");
            Client c3 = new Client("Danny", "Brown", "danny@example.com");

            svc.addClient(c1);
            svc.addClient(c2);
            svc.addClient(c3);

            Flight f1 = new Flight("AirX", "Montreal", "Paris", "AirX", 25.0);
            Flight f2 = new Flight("SkyJet", "Dubai", "Rome", "SkyJet", 18.0);
            Train tr1 = new Train("RailPro", "Toronto", "Ottawa", "HighSpeed");
            Train tr2 = new Train("EuroRail", "Rome", "Milan", "Standard");
            Bus b1 = new Bus("Greyhound", "NYC", "Boston", 3);
            Bus b2 = new Bus("CoachX", "Madrid", "Valencia", 2);

            svc.addTransportation(f1);
            svc.addTransportation(f2);
            svc.addTransportation(tr1);
            svc.addTransportation(tr2);
            svc.addTransportation(b1);
            svc.addTransportation(b2);

            Hotel h1 = new Hotel("Hilton", "Rome", 280.0, 4);
            Hotel h2 = new Hotel("Sheraton", "Paris", 320.0, 5);
            Hostel ho1 = new Hostel("Backpackers", "Rome", 55.0, 6);
            Hostel ho2 = new Hostel("CityHostel", "Ottawa", 70.0, 4);

            svc.addAccommodation(h1);
            svc.addAccommodation(h2);
            svc.addAccommodation(ho1);
            svc.addAccommodation(ho2);

            Trip t1 = new Trip(c1, f1, h1, "Rome", 5, 1800.0);
            Trip t2 = new Trip(c2, tr1, ho2, "Ottawa", 3, 500.0);
            Trip t3 = new Trip(c3, b2, ho1, "Valencia", 4, 700.0);

            svc.addTrip(t1);
            svc.addTrip(t2);
            svc.addTrip(t3);

            System.out.println("\n===== PREDEFINED SCENARIO =====");
            listAllSummary(svc);

            System.out.println("\n===== EQUALS TESTS =====");
            System.out.println("Different classes (Flight vs Hotel): " + f1.equals(h1));
            System.out.println("Same class, different attributes (Hotel h1 vs h2): " + h1.equals(h2));
            Hotel h3 = new Hotel("Hilton", "Rome", 280.0, 4);
            System.out.println("Same class, identical attributes except ID: " + h1.equals(h3));

            System.out.println("\n===== MOST EXPENSIVE TRIP =====");
            Trip mostExp = svc.findMostExpensiveTrip();
            System.out.println(mostExp);
            System.out.println("Total = " + mostExp.calculateTotalCost());

            System.out.println("\n===== DEEP COPY TEST: TRANSPORT =====");
            Transportation[] copyT = svc.deepCopyTransportationArray();
            System.out.println("Original[0] = " + svc.getTransportations()[0]);
            System.out.println("Copy[0]     = " + copyT[0]);
            if (copyT[0] instanceof Flight) {
                ((Flight) copyT[0]).setAirlineName("ChangedAirline");
            }
            System.out.println("Original[0] = " + svc.getTransportations()[0]);
            System.out.println("Copy[0]     = " + copyT[0]);

            System.out.println("\n===== DEEP COPY TEST: ACCOMMODATION =====");
            Accommodation[] copyA = svc.deepCopyAccommodationArray();
            System.out.println("Original[0] = " + svc.getAccommodations()[0]);
            System.out.println("Copy[0]     = " + copyA[0]);
            if (copyA[0] instanceof Hotel) {
                ((Hotel) copyA[0]).setStars(1);
            }
            System.out.println("Original[0] = " + svc.getAccommodations()[0]);
            System.out.println("Copy[0]     = " + copyA[0]);

        } catch (Exception ex) {
            System.out.println("SCENARIO ERROR: " + ex.getMessage());
        }
    }

    private static void runA2PredefinedScenario(SmartTravelService svc) {
        svc.clearAllData();
        System.out.println("\nRunning A2 predefined scenario...");

        loadAllData(svc);

        if (svc.getClientCount() > 0) {
            try {
                String duplicateEmail = svc.getClients()[0].getEmail();
                svc.addClient(new Client("Test", "User", duplicateEmail));
            } catch (DuplicateEmailException ex) {
                System.out.println("Caught expected DuplicateEmailException: " + ex.getMessage());
            } catch (InvalidClientDataException ex) {
                System.out.println("Unexpected client validation error: " + ex.getMessage());
            }
        }

        saveAllData(svc);
        generateDashboard(svc);

        System.out.println("A2 scenario done.");
    }

    private static int readInt(Scanner keyboard) {
        while (true) {
            String s = keyboard.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter an integer: ");
            }
        }
    }

    private static double readDouble(Scanner keyboard) {
        while (true) {
            String s = keyboard.nextLine();
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}