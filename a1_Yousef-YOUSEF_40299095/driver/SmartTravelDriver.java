// -----------------------------------------------------
// Assignment 1
// Class: SmartTravelDriver
// Written by: Yousef Yousef (40299095) Hamza Shadeed (40341727)
// -----------------------------------------------------

package driver;

import java.util.Scanner;

import client.Client;
import travel.*;

 /** Main driver for SmartTravel */
public class SmartTravelDriver {

    private static final int MAX_CLIENTS = 50;
    private static final int MAX_TRANSPORT_OPTIONS = 50;
    private static final int MAX_ACCOM_OPTIONS = 50;
    private static final int MAX_TRIPS = 50;

    /** Program entry point: the main manages the menu loop and predefined scenario */
    public static void main(String[] args) {
        /** Single Scanner shared across menus to avoid input glitches */
        Scanner input = new Scanner(System.in);

        /** Fixed-size storage */
        Client[] clientList = new Client[MAX_CLIENTS];
        Transportation[] transportList = new Transportation[MAX_TRANSPORT_OPTIONS];
        Accommodation[] accommodationList = new Accommodation[MAX_ACCOM_OPTIONS];
        Trip[] tripList = new Trip[MAX_TRIPS];

        /** Counters track how many real objects are stored (arrays are partially filled) */
        int numClients = 0;
        int numTransports = 0;
        int numAccommodations = 0;
        int numTrips = 0;

        System.out.println("Welcome to SmartTravel (Internal Employee System) by Yousef Yousef");

        int mainChoice;

        /** Main loop: keep showing menus until the user chooses Exit */
        while (true) {
        	mainChoice = showMenu(
                    input,
                    "\n=== MAIN MENU ===",
                    "1) Client Management",
                    "2) Trip Management",
                    "3) Transportation Management",
                    "4) Accommodation Management",
                    "5) Additional Operations",
                    "6) Run Predefined Scenario",
                    "0) Exit"
            );


            if (mainChoice == 0) {
                System.out.println("Thank you for using SmartTravel, Goodbye!");
                break;
            }

            /** Route user to the correct subsystem without mixing responsibilities in main */
            switch (mainChoice) {

            case 1:
                numClients = clientMenu(input, clientList, numClients);
                break;

            case 2:
                numTrips = tripMenu(input, clientList, numClients, transportList, numTransports, accommodationList, numAccommodations, tripList, numTrips);
                break;

            case 3:
                numTransports = transportMenu(input, transportList, numTransports);
                break;

            case 4:
                numAccommodations = accommodationMenu(input, accommodationList, numAccommodations);
                break;

            case 5:
                additionalOpsMenu(input, tripList, numTrips, transportList, numTransports, accommodationList, numAccommodations);
                break;

            case 6:
                /** Predefined scenario fills arrays and prints */
                int[] counts = runPredefinedScenario(clientList, transportList, accommodationList, tripList);
                numClients = counts[0];
                numTransports = counts[1];
                numAccommodations = counts[2];
                numTrips = counts[3];
                break;

            default:
                System.out.println("Invalid choice.");
        }
      }

        input.close();
    }

    /** Prints a menu and returns a validated numeric choice (keeps main loop clean) */
    private static int showMenu(Scanner input, String title, String... options) {
        System.out.println(title);
        for (String opt : options)
            System.out.println(opt);

        System.out.print("Enter choice: ");
        while (!input.hasNextInt()) {
            input.nextLine();
            System.out.print("Enter choice: ");
        }
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    /** Client submenu: add/edit/delete/list using a partially-filled Client[] array */
    private static int clientMenu(Scanner input, Client[] clientList, int numClients) {
        int choice;

        while (true) {
            choice = showMenu(
                    input,
                    "\n=== CLIENT MENU ===",
                    "1) Add Client",
                    "2) Edit Client",
                    "3) Delete Client",
                    "4) List Clients",
                    "0) Back"
            );

            if (choice == 0)
                return numClients;

            switch (choice) {
                case 1:
                    numClients = addClient(input, clientList, numClients);
                    break;
                case 2:
                    editClient(input, clientList, numClients);
                    break;
                case 3:
                    numClients = deleteClient(input, clientList, numClients);
                    break;
                case 4:
                    /** Print current state of the array up to numClients (ignore null slots) */
                    System.out.println("\n--- Clients ---");
                    for (int i = 0; i < numClients; i++)
                        System.out.println(clientList[i]);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Creates a new Client from user input and stores it in the next free array slot */
    private static int addClient(Scanner input, Client[] clientList, int numClients) {
        if (numClients >= MAX_CLIENTS) {
            System.out.println("Client list is full.");
            return numClients;
        }

        System.out.print("First name: ");
        String first = input.nextLine();
        System.out.print("Last name: ");
        String last = input.nextLine();
        System.out.print("Email: ");
        String email = input.nextLine();

        /** Client ID is auto-generated inside the constructor */
        clientList[numClients] = new Client(first, last, email);
        System.out.println("Added: " + clientList[numClients]);

        return numClients + 1;
    }

    /** Edits an existing client by ID (demonstrates searching a partially-filled array) */
    private static void editClient(Scanner input, Client[] clientList, int numClients) {
        System.out.print("Enter client ID to edit: ");
        String id = input.nextLine();

        int idx = findClientIndex(clientList, numClients, id);
        if (idx == -1) {
            System.out.println("Client not found.");
            return;
        }

        System.out.print("New first name: ");
        clientList[idx].setFirstName(input.nextLine());
        System.out.print("New last name: ");
        clientList[idx].setLastName(input.nextLine());
        System.out.print("New email: ");
        clientList[idx].setEmail(input.nextLine());

        System.out.println("Updated: " + clientList[idx]);
    }

    /** Deletes a client by shifting the array left to avoid gaps (no collections allowed) */
    private static int deleteClient(Scanner input, Client[] clientList, int numClients) {
        System.out.print("Enter client ID to delete: ");
        String id = input.nextLine();

        int idx = findClientIndex(clientList, numClients, id);
        if (idx == -1) {
            System.out.println("Client not found.");
            return numClients;
        }

        /** Shift left so the array stays compact and counters stay meaningful */
        for (int i = idx; i < numClients - 1; i++) {
            clientList[i] = clientList[i + 1];
        }
        clientList[numClients - 1] = null;

        System.out.println("Client deleted.");
        return numClients - 1;
    }

    /** Trip submenu: create/edit/cancel/list trips and link them to existing clients */
    private static int tripMenu(Scanner input,
                               Client[] clientList, int numClients,
                               Transportation[] transportList, int numTransports,
                               Accommodation[] accommodationList, int numAccommodations,
                               Trip[] tripList, int numTrips) {

        int choice;

        while (true) {
            choice = showMenu(
                    input,
                    "\n=== TRIP MENU ===",
                    "1) Create Trip",
                    "2) Edit Trip",
                    "3) Cancel Trip",
                    "4) List Trips",
                    "5) List Trips for Client",
                    "0) Back"
            );

            if (choice == 0)
                return numTrips;

            switch (choice) {
                case 1:
                  numTrips = createTrip(input, clientList, numClients, transportList, numTransports, accommodationList, numAccommodations, tripList, numTrips);
                    break;

                case 2:
                  editTrip(input, clientList, numClients, transportList, numTransports, accommodationList, numAccommodations, tripList, numTrips);
                    break;

                case 3:
                  numTrips = cancelTrip(input, tripList, numTrips);
                    break;

                case 4:
                  System.out.println("\n--- Trips ---");
                    for (int i = 0; i < numTrips; i++)
                        System.out.println(tripList[i]);
                    break;

                case 5:
                  listTripsForClient(input, tripList, numTrips);
                    break;

                default:
                  System.out.println("Invalid choice.");
            }
        }
    }

    /** Builds a Trip and attaches optional Transportation/Accommodation selections */
    private static int createTrip(Scanner input, Client[] clientList, int numClients,
                                 Transportation[] transportList, int numTransports,
                                 Accommodation[] accommodationList, int numAccommodations,
                                 Trip[] tripList, int numTrips) {

        if (numTrips >= MAX_TRIPS) {
            System.out.println("Trip list is full.");
            return numTrips;
        }

        System.out.print("Destination: ");
        String destination = input.nextLine();

        System.out.print("Duration in days: ");
        while (!input.hasNextInt()) {
            input.nextLine();
            System.out.print("Duration in days: ");
        }
        int days = input.nextInt();
        input.nextLine();

        System.out.print("Base price: ");
        while (!input.hasNextDouble()) {
            input.nextLine();
            System.out.print("Base price: ");
        }
        double base = input.nextDouble();
        input.nextLine();

        /** Trip must link to an existing client; we pick by ID to avoid duplicates */
        System.out.print("Client ID: ");
        String clientId = input.nextLine();
        int clientIdx = findClientIndex(clientList, numClients, clientId);

        if (clientIdx == -1) {
            System.out.println("Client not found. Trip not created.");
            return numTrips;
        }

        /** User can attach optional services; stored as base types for polymorphism later */
        Transportation chosenTransport = chooseTransport(input, transportList, numTransports);
        Accommodation chosenAccommodation = chooseAccommodation(input, accommodationList, numAccommodations);

        tripList[numTrips] = new Trip(destination, days, base, clientList[clientIdx], chosenTransport, chosenAccommodation);
        System.out.println("Created: " + tripList[numTrips]);

        return numTrips + 1;
    }

    private static int cancelTrip(Scanner input, Trip[] tripList, int numTrips) {
        System.out.print("Enter trip ID to cancel: ");
        String tripId = input.nextLine();

        int tripIdx = findTripIndex(tripList, numTrips, tripId);
        if (tripIdx == -1) {
            System.out.println("Trip not found.");
            return numTrips;
        }

        for (int i = tripIdx; i < numTrips - 1; i++) {
            tripList[i] = tripList[i + 1];
        }
        tripList[numTrips - 1] = null;

        System.out.println("Trip cancelled.");
        return numTrips - 1;
    }

    
    /** Edits a trips core fields and optionally changes linked transport/accommodation */
    private static void editTrip(Scanner input,
            Client[] clientList, int numClients,
            Transportation[] transportList, int numTransports,
            Accommodation[] accommodationList, int numAccommodations,
            Trip[] tripList, int numTrips) {

    		System.out.print("Enter trip ID to edit: ");
    		String tripId = input.nextLine();
    		
    		int tripIdx = findTripIndex(tripList, numTrips, tripId);
    		if (tripIdx == -1) {
    			System.out.println("Trip not found.");
    			return;
    		}

    		System.out.print("New destination: ");
    		tripList[tripIdx].setDestination(input.nextLine());

    		int newDays = readInt(input, "New duration in days: ");
	tripList[tripIdx].setDurationInDays(newDays);

	double newBase = readDouble(input, "New base price: ");
	tripList[tripIdx].setBasePrice(newBase);

		/** Re-link to an existing client (optional) */
		System.out.print("New client ID: ");
		String newClientId = input.nextLine();
		int clientIdx = findClientIndex(clientList, numClients, newClientId);
		if (clientIdx != -1) {
			tripList[tripIdx].setClient(clientList[clientIdx]);
		}

		/** Optional reselect */
		tripList[tripIdx].setTransportation(chooseTransport(input, transportList, numTransports));
		tripList[tripIdx].setAccommodation(chooseAccommodation(input, accommodationList, numAccommodations));

		System.out.println("Updated: " + tripList[tripIdx]);
    }


    /** Lists trips for a given client ID (shows the Trip-Client association) */
    private static void listTripsForClient(Scanner input, Trip[] tripList, int numTrips) {
        System.out.print("Enter client ID: ");
        String clientId = input.nextLine();

        System.out.println("\n--- Trips for Client " + clientId + " ---");
        boolean found = false;

        for (int i = 0; i < numTrips; i++) {
            if (tripList[i] != null && tripList[i].hasClientId(clientId)) {
                System.out.println(tripList[i]);
                found = true;
            }
        }

        if (!found)
            System.out.println("No trips found for that client.");
    }

    /** Transportation sub-menu: add/remove/list transport options using a polymorphic base type */
    private static int transportMenu(Scanner input, Transportation[] transportList, int numTransports) {
        int choice;

        while (true) {
            choice = showMenu(
                    input,
                    "\n=== TRANSPORT MENU ===",
                    "1) Add Transportation",
                    "2) Remove Transportation",
                    "3) List by Type",
                    "4) List All",
                    "0) Back"
            );

            if (choice == 0)
                return numTransports;

            switch (choice) {
                case 1:
                    numTransports = addTransport(input, transportList, numTransports);
                    break;
                case 2:
                    numTransports = removeTransport(input, transportList, numTransports);
                    break;
                case 3:
                    listTransportByType(input, transportList, numTransports);
                    break;
                case 4:
                    System.out.println("\n--- Transportation Options ---");
                    for (int i = 0; i < numTransports; i++)
                        System.out.println(transportList[i]);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Adds a Flight/Train/Bus based on user choice */
    private static int addTransport(Scanner input, Transportation[] transportList, int numTransports) {
        if (numTransports >= MAX_TRANSPORT_OPTIONS) {
            System.out.println("Transportation list is full.");
            return numTransports;
        }

        int type = showMenu(
                input,
                "\nChoose transport type:",
                "1) Flight",
                "2) Train",
                "3) Bus"
        );

        System.out.print("Company name: ");
        String company = input.nextLine();
        System.out.print("Departure city: ");
        String from = input.nextLine();
        System.out.print("Arrival city: ");
        String to = input.nextLine();

        if (type == 1) {
            System.out.print("Airline name: ");
            String airline = input.nextLine();

            System.out.print("Luggage allowance (kg): ");
            while (!input.hasNextDouble()) {
                input.nextLine();
                System.out.print("Luggage allowance (kg): ");
            }
            double kg = input.nextDouble();
            input.nextLine();

            transportList[numTransports] = new Flight(company, from, to, airline, kg);

        } else if (type == 2) {
            System.out.print("Train type (e.g., high-speed, regional): ");
            String trainType = input.nextLine();

            System.out.print("Seat class (e.g., first, economy): ");
            String seatClass = input.nextLine();

            transportList[numTransports] = new Train(company, from, to, trainType, seatClass);

        } else if (type == 3) {
            System.out.print("Bus company: ");
            String busCompany = input.nextLine();

            System.out.print("Number of stops: ");
            while (!input.hasNextInt()) {
                input.nextLine();
                System.out.print("Number of stops: ");
            }
            int stops = input.nextInt();
            input.nextLine();

            transportList[numTransports] = new Bus(company, from, to, busCompany, stops);

        } else {
            System.out.println("Invalid transport type.");
            return numTransports;
        }

        System.out.println("Added: " + transportList[numTransports]);
        return numTransports + 1;
    }

    /** Removes a transport option by ID and shifts the array to keep it compact */
    private static int removeTransport(Scanner input, Transportation[] transportList, int numTransports) {
        System.out.print("Enter transport ID to remove: ");
        String id = input.nextLine();

        int idx = findTransportIndex(transportList, numTransports, id);
        if (idx == -1) {
            System.out.println("Transport not found.");
            return numTransports;
        }

        /** Shift left to keep options packed at the start of the array */
        for (int i = idx; i < numTransports - 1; i++) {
            transportList[i] = transportList[i + 1];
        }
        transportList[numTransports - 1] = null;

        System.out.println("Transport removed.");
        return numTransports - 1;
    }

    /** Filters transportation options by concrete subtype (Flight/Train/Bus) */
    private static void listTransportByType(Scanner input, Transportation[] transportList, int numTransports) {
        int type = showMenu(
                input,
                "\nFilter by type:",
                "1) Flight",
                "2) Train",
                "3) Bus"
        );

        System.out.println("\n--- Filtered Transportation ---");

        /** instanceof is used here strictly for filtering/printing by subtype */
        for (int i = 0; i < numTransports; i++) {
            if (type == 1 && transportList[i] instanceof Flight)
                System.out.println(transportList[i]);
            if (type == 2 && transportList[i] instanceof Train)
                System.out.println(transportList[i]);
            if (type == 3 && transportList[i] instanceof Bus)
                System.out.println(transportList[i]);
        }
    }

    /** Accommodation sub-menu: add/remove/list accommodation options using base-class references */
    private static int accommodationMenu(Scanner input, Accommodation[] accommodationList, int numAccommodations) {
        int choice;

        while (true) {
            choice = showMenu(
                    input,
                    "\n=== ACCOMMODATION MENU ===",
                    "1) Add Accommodation",
                    "2) Remove Accommodation",
                    "3) List by Type",
                    "4) List All",
                    "0) Back"
            );

            if (choice == 0)
                return numAccommodations;

            switch (choice) {
                case 1:
                    numAccommodations = addAccommodation(input, accommodationList, numAccommodations);
                    break;
                case 2:
                    numAccommodations = removeAccommodation(input, accommodationList, numAccommodations);
                    break;
                case 3:
                    listAccommodationByType(input, accommodationList, numAccommodations);
                    break;
                case 4:
                    System.out.println("\n--- Accommodation Options ---");
                    for (int i = 0; i < numAccommodations; i++)
                        System.out.println(accommodationList[i]);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Adds a Hotel/Hostel based on user choice */
    private static int addAccommodation(Scanner input, Accommodation[] accommodationList, int numAccommodations) {
        if (numAccommodations >= MAX_ACCOM_OPTIONS) {
            System.out.println("Accommodation list is full.");
            return numAccommodations;
        }

        int type = showMenu(
                input,
                "\nChoose accommodation type:",
                "1) Hotel",
                "2) Hostel"
        );

        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("Location: ");
        String location = input.nextLine();

        System.out.print("Price per night: ");
        while (!input.hasNextDouble()) {
            input.nextLine();
            System.out.print("Price per night: ");
        }
        double ppn = input.nextDouble();
        input.nextLine();

        /** Create a concrete subtype, but store it as Accommodation */
        if (type == 1) {
            System.out.print("Star rating: ");
            while (!input.hasNextInt()) {
                input.nextLine();
                System.out.print("Star rating: ");
            }
            int stars = input.nextInt();
            input.nextLine();

            accommodationList[numAccommodations] = new Hotel(name, location, ppn, stars);
        } else if (type == 2) {
            System.out.print("Number of beds: ");
            while (!input.hasNextInt()) {
                input.nextLine();
                System.out.print("Number of beds: ");
            }
            int beds = input.nextInt();
            input.nextLine();

            accommodationList[numAccommodations] = new Hostel(name, location, ppn, beds);
        } else {
            System.out.println("Invalid accommodation type.");
            return numAccommodations;
        }

        System.out.println("Added: " + accommodationList[numAccommodations]);
        return numAccommodations + 1;
    }

    /** Removes an accommodation option by ID and shifts the array to keep it compact */
    private static int removeAccommodation(Scanner input, Accommodation[] accommodationList, int numAccommodations) {
        System.out.print("Enter accommodation ID to remove: ");
        String id = input.nextLine();

        int idx = findAccommodationIndex(accommodationList, numAccommodations, id);
        if (idx == -1) {
            System.out.println("Accommodation not found.");
            return numAccommodations;
        }

        /** Shift left to avoid null gaps inside the used portion of the array */
        for (int i = idx; i < numAccommodations - 1; i++) {
            accommodationList[i] = accommodationList[i + 1];
        }
        accommodationList[numAccommodations - 1] = null;

        System.out.println("Accommodation removed.");
        return numAccommodations - 1;
    }

    /** Filters accommodation options by concrete subtype (Hotel/Hostel) */
    private static void listAccommodationByType(Scanner input, Accommodation[] accommodationList, int numAccommodations) {
        int type = showMenu(
                input,
                "\nFilter by type:",
                "1) Hotel",
                "2) Hostel"
        );

        System.out.println("\n--- Filtered Accommodation ---");

        /** instanceof is used here strictly for filtering/printing by subtype */
        for (int i = 0; i < numAccommodations; i++) {
            if (type == 1 && accommodationList[i] instanceof Hotel)
                System.out.println(accommodationList[i]);
            if (type == 2 && accommodationList[i] instanceof Hostel)
                System.out.println(accommodationList[i]);
        }
    }

    /** Runs the extra required operations: most expensive trip, total cost, deep copy demos, etc */
    private static void additionalOpsMenu(Scanner input,
                                         Trip[] tripList, int numTrips,
                                         Transportation[] transportList, int numTransports,
                                         Accommodation[] accommodationList, int numAccommodations) {

        int choice;

        while (true) {
            choice = showMenu(
                    input,
                    "\n=== ADDITIONAL OPERATIONS ===",
                    "1) Show most expensive trip",
                    "2) Show total cost of a trip",
                    "3) Deep copy transportation demo",
                    "4) Deep copy accommodation demo",
                    "0) Back"
            );

            if (choice == 0)
                return;

            switch (choice) {
                case 1:
                    showMostExpensiveTripUsingTripMethod(tripList, numTrips);
                    break;
                case 2:
                    showTripTotalCost(input, tripList, numTrips);
                    break;
                case 3:
                    deepCopyTransportDemo(transportList, numTransports);
                    break;
                case 4:
                    deepCopyAccommodationDemo(accommodationList, numAccommodations);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /** Finds and prints the most expensive trip using Trip.findMostExpensiveTrip(...) */
    private static void showMostExpensiveTripUsingTripMethod(Trip[] tripList, int numTrips) {
        Trip best = Trip.findMostExpensiveTrip(tripList, numTrips);
        if (best == null) {
            System.out.println("No trips to evaluate.");
        } else {
            System.out.println("Most expensive trip: " + best);
        }
    }

    /** Lets the user pick a trip and prints its total cost  */
    private static void showTripTotalCost(Scanner input, Trip[] tripList, int numTrips) {
        System.out.print("Enter trip ID: ");
        String id = input.nextLine();

        int idx = findTripIndex(tripList, numTrips, id);
        if (idx == -1) {
            System.out.println("Trip not found.");
            return;
        }

        System.out.println("Total cost: " + tripList[idx].calculateTotalCost());
    }

    public static Transportation[] copyTransportationArray(Transportation[] original) {
        return Transportation.copyTransportationArray(original);
    }

    public static Accommodation[] copyAccommodationArray(Accommodation[] original) {
        return Accommodation.copyAccommodationArray(original);
    }
    
    /** Demonstrates deep copy on Transportation[]: modify the copy and prove original unchanged */
    private static void deepCopyTransportDemo(Transportation[] transportList, int numTransports) {
        System.out.println("\n=== DEEP COPY (Transportation) ===");

        /** Call the signature in the driver */
        Transportation[] copied = copyTransportationArray(transportList);

        /** Mutate the copy only */
        if (numTransports > 0 && copied[0] != null) {
            copied[0].setCompanyName("CHANGED_IN_COPY_ONLY");
        }

        System.out.println("\n--- Original transportation ---");
        for (int i = 0; i < numTransports; i++) System.out.println(transportList[i]);

        System.out.println("\n--- Copied transportation ---");
        for (int i = 0; i < numTransports; i++) System.out.println(copied[i]);
    }

    /** Demonstrates deep copy on Accommodation[]: modify the copy and prove original unchanged */
    private static void deepCopyAccommodationDemo(Accommodation[] accommodationList, int numAccommodations) {
        System.out.println("\n=== DEEP COPY (Accommodation) ===");

        /** Call the signature in the driver */
        Accommodation[] copied = copyAccommodationArray(accommodationList);

        if (numAccommodations > 0 && copied[0] != null) {
            copied[0].setName("CHANGED_IN_COPY_ONLY");
        }

        System.out.println("\n--- Original accommodation ---");
        for (int i = 0; i < numAccommodations; i++) System.out.println(accommodationList[i]);

        System.out.println("\n--- Copied accommodation ---");
        for (int i = 0; i < numAccommodations; i++) System.out.println(copied[i]);
    }
    
    
    /** Auto-populates arrays and runs the tests */
    private static int[] runPredefinedScenario(Client[] clientList,
            Transportation[] transportList,
            Accommodation[] accommodationList,
            Trip[] tripList) {

    	System.out.println("\n=== PREDEFINED SCENARIO ===");

    	int numClients = 0;
    	int numTransports = 0;
    	int numAccommodations = 0;
    	int numTrips = 0;

    	/** 1) Create at least 3 clients */
    	clientList[numClients++] = new Client("Malcolm", "White", "malcolm@example.com");
    	clientList[numClients++] = new Client("Sara", "Black", "sara@example.com");
    	clientList[numClients++] = new Client("Danny", "Brown", "danny@example.com");

    	/** 2) Create at least 2 of each transportation type (Flight/Train/Bus) */
    	transportList[numTransports++] = new Flight("AirX", "Montreal", "Paris", "AirX", 25.0);
    	transportList[numTransports++] = new Flight("SkyJet", "Toronto", "London", "SkyJet", 18.0);

    	transportList[numTransports++] = new Train("RailPro", "Berlin", "Munich", "high-speed", "First");
    	transportList[numTransports++] = new Train("EuroRail", "Madrid", "Barcelona", "regional", "Economy");

    		transportList[numTransports++] = new Bus("CityBus", "Rome", "Naples", "Coach", 3);
			transportList[numTransports++] = new Bus("InterCity", "Milan", "Venice", "InterCity", 1);

		/** 3) Create at least 2 of each accommodation type (Hotel/Hostel) */
		accommodationList[numAccommodations++] = new Hotel("Grand Hotel", "Paris", 200.0, 5);
		accommodationList[numAccommodations++] = new Hotel("Budget Inn", "London", 120.0, 3);

			accommodationList[numAccommodations++] = new Hostel("Backpackers", "Berlin", 60.0, 6);
				accommodationList[numAccommodations++] = new Hostel("Youth Stay", "Barcelona", 55.0, 8);

		/** 4) Create at least 3 trips (mix services) */
		tripList[numTrips++] = new Trip("Paris", 5, 500.0, clientList[0], transportList[0], accommodationList[0]);	
		tripList[numTrips++] = new Trip("London", 4, 450.0, clientList[1], transportList[1], accommodationList[1]);
		tripList[numTrips++] = new Trip("Berlin", 3, 300.0, clientList[2], transportList[2], accommodationList[2]);

		/** 5) Display all created objects using toString() */
		System.out.println("\n--- Clients ---");
		for (int i = 0; i < numClients; i++) System.out.println(clientList[i]);

		System.out.println("\n--- Transportation (ALL) ---");
		for (int i = 0; i < numTransports; i++) System.out.println(transportList[i]);

		System.out.println("\n--- Accommodations (ALL) ---");
		for (int i = 0; i < numAccommodations; i++) System.out.println(accommodationList[i]);

		System.out.println("\n--- Trips ---");
		for (int i = 0; i < numTrips; i++) System.out.println(tripList[i]);

		/** 6) equals() tests required by scenario: 
		    - different classes
		    - same class different attributes
		    - same class identical attributes */
		System.out.println("\n--- equals() tests ---");

		/** Client tests */
		System.out.println("Client vs Trip: " + clientList[0].equals(tripList[0]));
		System.out.println("Client 0 vs Client 1): " + clientList[0].equals(clientList[1]));
		System.out.println("Client 0 vs Client copy: " + clientList[0].equals(new Client(clientList[0])));

		/** Transportation tests (use known indices) */
	System.out.println("Flight vs Train: " + transportList[0].equals(transportList[2]));
	System.out.println("Flight0 vs Flight1: " + transportList[0].equals(transportList[1]));
	System.out.println("Flight0 vs Flight0 copy: " +
			transportList[0].equals(new Flight((Flight) transportList[0])));

	/** Accommodation tests (use known indices) */
	System.out.println("Hotel vs Hostel: " + accommodationList[0].equals(accommodationList[2]));
	System.out.println("Hotel0 vs Hotel1: " + accommodationList[0].equals(accommodationList[1]));
	System.out.println("Hotel0 vs Hotel0 copy: " + accommodationList[0].equals(new Hotel((Hotel) accommodationList[0])));

	/** 7) calculates and displays total cost of multiple trips */
		System.out.println("\n--- Trip Total Costs ---");
	for (int i = 0; i < numTrips; i++) {
		System.out.println(tripList[i].getTripId() + " -> total cost = " + tripList[i].calculateTotalCost());
	}

	/** 8) Most expensive trip */
	showMostExpensiveTripUsingTripMethod(tripList, numTrips);

	/** 9) Deep copy demo (transport + accommodation) */
	deepCopyTransportDemo(transportList, numTransports);
	deepCopyAccommodationDemo(accommodationList, numAccommodations);

		return new int[]{numClients, numTransports, numAccommodations, numTrips};
    }

    /** Lets user select a Transportation option or skip (supports optional association in Trip) */
    private static Transportation chooseTransport(Scanner input, Transportation[] transportList, int numTransports) {
        if (numTransports == 0) {
            System.out.println("No transportation options available.");
            return null;
        }

        System.out.print("Add transportation? (y/n): ");
        String ans = input.nextLine();
        if (!ans.equalsIgnoreCase("y"))
            return null;

        System.out.println("\n--- Transportation Options ---");
        for (int i = 0; i < numTransports; i++) {
            System.out.println((i + 1) + ") " + transportList[i]);
        }

        System.out.print("Choose option #: ");
        while (!input.hasNextInt()) {
            input.nextLine();
            System.out.print("Choose option #: ");
        }
        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > numTransports) {
            System.out.println("Invalid choice.");
            return null;
        }

        return transportList[choice - 1];
    }

    /** Lets user select an Accommodation option or skip (supports optional association in Trip) */
    private static Accommodation chooseAccommodation(Scanner input, Accommodation[] accommodationList, int numAccommodations) {
        if (numAccommodations == 0) {
            System.out.println("No accommodation options available.");
            return null;
        }

        System.out.print("Add accommodation? (y/n): ");
        String ans = input.nextLine();
        if (!ans.equalsIgnoreCase("y"))
            return null;

        System.out.println("\n--- Accommodation Options ---");
        for (int i = 0; i < numAccommodations; i++) {
            System.out.println((i + 1) + ") " + accommodationList[i]);
        }

        System.out.print("Choose option #: ");
        while (!input.hasNextInt()) {
            input.nextLine();
            System.out.print("Choose option #: ");
        }
        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > numAccommodations) {
            System.out.println("Invalid choice.");
            return null;
        }

        return accommodationList[choice - 1];
    }

    
    private static int readInt(Scanner input, String prompt) {
        System.out.print(prompt);
        while (!input.hasNextInt()) {
            input.nextLine();
            System.out.print(prompt);
        }
        int val = input.nextInt();
        input.nextLine(); 
        return val;
    }

    private static double readDouble(Scanner input, String prompt) {
        System.out.print(prompt);
        while (!input.hasNextDouble()) {
            input.nextLine();
            System.out.print(prompt);
        }
        double val = input.nextDouble();
        input.nextLine();
        return val;
    }

    
    /** Linear search helper for locating a client by ID in a partially-filled array */
    private static int findClientIndex(Client[] clientList, int numClients, String clientId) {
        for (int i = 0; i < numClients; i++) {
            if (clientList[i] != null && clientList[i].getClientId().equals(clientId))
                return i;
        }
        return -1;
    }
    
    private static int findTripIndex(Trip[] tripList, int numTrips, String tripId) {
        for (int i = 0; i < numTrips; i++) {
            if (tripList[i] != null && tripList[i].getTripId().equals(tripId))
                return i;
        }
        return -1;
    }


    /** Linear search helper for locating transportation by ID in a partially-filled array */
    private static int findTransportIndex(Transportation[] transportList, int numTransports, String transportId) {
        for (int i = 0; i < numTransports; i++) {
            if (transportList[i] != null && transportList[i].getTransportId().equals(transportId))
                return i;
        }
        return -1;
    }

    /** Linear search helper for locating accommodation by ID in a partially-filled array */
    private static int findAccommodationIndex(Accommodation[] accommodationList, int numAccommodations, String accommodationId) {
        for (int i = 0; i < numAccommodations; i++) {
            if (accommodationList[i] != null && accommodationList[i].getAccommodationId().equals(accommodationId))
                return i;
        }
        return -1;
    }

}