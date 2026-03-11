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

        // A2 required arrays + max sizes
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
            System.out.println("10) Run Predefined Scenario (A2)");
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

    // ---------------------------
    // Menus (kept simple + A2 safe)
    // ---------------------------

    private static void clientMenu(Scanner keyboard, SmartTravelService svc) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== CLIENT MENU ===");
            System.out.println("1) Add Client");
            System.out.println("2) List Clients");
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
                    } catch (DuplicateEmailException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    } catch (InvalidClientDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    for (int i = 0; i < svc.getClientCount(); i++) {
                        if (svc.getClients()[i] != null) System.out.println(svc.getClients()[i]);
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
            System.out.println("\n=== TRANSPORT MENU ===");
            System.out.println("1) Add Transportation");
            System.out.println("2) List All");
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
                            System.out.print("Seat class: ");
                            String seatClass = keyboard.nextLine();

                            Train tr = new Train(company, dep, arr, trainType, seatClass);
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
                    } catch (InvalidTransportDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    for (int i = 0; i < svc.getTransportCount(); i++) {
                        if (svc.getTransportations()[i] != null) System.out.println(svc.getTransportations()[i]);
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
            System.out.println("2) List All");
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
                    } catch (InvalidAccommodationDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    for (int i = 0; i < svc.getAccommodationCount(); i++) {
                        if (svc.getAccommodations()[i] != null) System.out.println(svc.getAccommodations()[i]);
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
            System.out.println("1) Add Trip (by IDs)");
            System.out.println("2) List Trips");
            System.out.println("0) Back");
            System.out.print("Enter choice: ");
            int c = readInt(keyboard);

            switch (c) {
                case 1:
                    try {
                        System.out.print("Client ID (e.g., C1001): ");
                        String cid = keyboard.nextLine();

                        System.out.print("Accommodation ID (blank if none): ");
                        String aid = keyboard.nextLine();

                        System.out.print("Transportation ID (blank if none): ");
                        String tid = keyboard.nextLine();

                        System.out.print("Destination: ");
                        String dest = keyboard.nextLine();

                        System.out.print("Duration (days 1-20): ");
                        int days = readInt(keyboard);

                        System.out.print("Base price (>=100): ");
                        double base = readDouble(keyboard);

                        Client client = svc.findClientById(cid);
                        Transportation tr = null;
                        Accommodation ac = null;

                        if (!aid.trim().isEmpty()) {
                            ac = svc.findAccommodationById(aid.trim());
                        }
                        if (!tid.trim().isEmpty()) {
                            tr = svc.findTransportationById(tid.trim());
                        }

                        Trip real = new Trip(client, tr, ac, dest, days, base);
                        svc.addTrip(real);

                        System.out.println("Added: " + real);

                    } catch (EntityNotFoundException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    } catch (InvalidTripDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    } catch (InvalidClientDataException ex) {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                    break;

                case 2:
                    for (int i = 0; i < svc.getTripCount(); i++) {
                        if (svc.getTrips()[i] != null) System.out.println(svc.getTrips()[i] + " | total=" +
                                String.format("%.2f", svc.getTrips()[i].calculateTotalCost()));
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

                    Transportation[] copyT = svc.deepCopyTransportationArray();

                    System.out.println("\nOriginal transportation array:");

                    for (int i = 0; i < svc.getTransportCount(); i++) {
                        System.out.println("Original[" + i + "] = " + svc.getTransportations()[i]);
                    }

                    System.out.println("\nCopied transportation array:");

                    for (int i = 0; i < copyT.length; i++) {
                        System.out.println("Copy[" + i + "] = " + copyT[i]);
                    }

                    break;

                case 4:

                    Accommodation[] copyA = svc.deepCopyAccommodationArray();

                    System.out.println("\nOriginal accommodation array:");

                    for (int i = 0; i < svc.getAccommodationCount(); i++) {
                        System.out.println("Original[" + i + "] = " + svc.getAccommodations()[i]);
                    }

                    System.out.println("\nCopied accommodation array:");

                    for (int i = 0; i < copyA.length; i++) {
                        System.out.println("Copy[" + i + "] = " + copyA[i]);
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

    // ---------------------------
    // A2 menu options
    // ---------------------------

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
            DashboardGenerator.generateDashboard(svc.getClients(), svc.getClientCount(), svc.getTrips(), svc.getTripCount());
            System.out.println("Dashboard generated in output/dashboard/ (and output/charts/ created).");
        } catch (IOException ex) {
            System.out.println("DASHBOARD ERROR: " + ex.getMessage());
        }
    }

    // ---------------------------
    // Predefined scenarios
    // ---------------------------

    /** A1 scenario idea kept: minimal sample data */
    private static void runA1PredefinedScenario(SmartTravelService svc) {
        try {

            svc.clearAllData();

            // Add clients
            svc.addClient(new Client("Malcolm", "White", "malcolm@example.com"));
            svc.addClient(new Client("Sara", "Black", "sara@example.com"));
            svc.addClient(new Client("Danny", "Brown", "danny@example.com"));

            // Add transport (A1 base fares preserved by constructors)
            svc.addTransportation(new Flight("AirX", "Montreal", "Paris", "AirX", 25.0));
            svc.addTransportation(new Train("RailPro", "Toronto", "Ottawa", "HighSpeed", "First"));
            svc.addTransportation(new Bus("Greyhound", "NYC", "Boston", 3));

            // Add accom
            svc.addAccommodation(new Hotel("Hilton", "Rome", 280.0, 4));
            svc.addAccommodation(new Hostel("Backpackers", "Rome", 55.0, 6));

            // Make trips
            Client c1 = svc.findClientById(svc.getClients()[0].getClientId());
            Trip t1 = new Trip(c1, svc.getTransportations()[0], svc.getAccommodations()[0], "Rome", 5, 1800.0);
            svc.addTrip(t1);

            System.out.println("Predefined scenario executed.");
            listAllSummary(svc);

        } catch (Exception ex) {
            System.out.println("SCENARIO ERROR: " + ex.getMessage());
        }
    }

    /** A2 scenario should demonstrate load/save + validation */
    private static void runA2PredefinedScenario(SmartTravelService svc) {
        svc.clearAllData();

        System.out.println("\nRunning A2 predefined scenario...");

        // 1) Try loading
        loadAllData(svc);

        // 2) Trigger an exception example (duplicate email)
        try {
            svc.addClient(new Client("Test", "User", "malcolm@example.com"));
        } catch (DuplicateEmailException ex) {
            System.out.println("Caught expected DuplicateEmailException: " + ex.getMessage());
        } catch (InvalidClientDataException ex) {
            System.out.println("Unexpected client validation error: " + ex.getMessage());
        }

        // 3) Save
        saveAllData(svc);

        System.out.println("A2 scenario done.");
    }

    // ---------------------------
    // Input helpers
    // ---------------------------

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