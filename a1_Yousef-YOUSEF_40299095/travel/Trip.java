// -----------------------------------------------------
// Assignment 1
// Class: Trip
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import client.Client;

/** Core Trip entity in the SmartTravel system */
public class Trip {

    private static int nextId = 2001;

    private String tripId;
    private String destination;
    private int durationInDays;
    private double basePrice;

    private Client client;
    private Transportation transportation;   
    private Accommodation accommodation;    

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "T" + nextId++;
    }

    /** Default constructor
     Builds a valid Trip object (ID handled automatically) */
    public Trip() {
        this.tripId = generateId();
        this.destination = "Unknown";
        this.durationInDays = 1;
        this.basePrice = 0.0;
        this.client = null;
        this.transportation = null;
        this.accommodation = null;
    }

    /** Parameterized constructor (ID auto-generated)
     Builds a valid Trip object (ID handled automatically) */
    public Trip(String destination, int durationInDays, double basePrice, Client client, Transportation transportation, Accommodation accommodation) {
        this.tripId = generateId();
        this.destination = destination;
        this.durationInDays = durationInDays;
        this.basePrice = basePrice;
        this.client = client;
        this.transportation = transportation;
        this.accommodation = accommodation;
    }

    /** Copy constructor (new ID, not copied)
     Builds a valid Trip object (ID handled automatically) */
    public Trip(Trip other) {
        this.tripId = generateId();
        this.destination = other.destination;
        this.durationInDays = other.durationInDays;
        this.basePrice = other.basePrice;

        // Shallow copy of references
        this.client = other.client;
        this.transportation = other.transportation;
        this.accommodation = other.accommodation;
    }

    /** setters and getters */
    public String getTripId() {
        return tripId;
    }

    public String getDestination() {
        return destination;
    }


    public void setDestination(String destination) {
        this.destination = destination;
    }

    
    public int getDurationInDays() {
        return durationInDays;
    }

    
    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

  
    public double getBasePrice() {
        return basePrice;
    }


    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

  
    public Client getClient() {
        return client;
    }

 
    public void setClient(Client client) {
        this.client = client;
    }

    
    public Transportation getTransportation() {
        return transportation;
    }

   
    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    
    public Accommodation getAccommodation() {
        return accommodation;
    }

   
    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    
    /** Computes the full cost of the trip
 	 Transportation and Accommodation costs are resolved at runtime */
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

    /** Optional helper methods */
    public String getTripType() {
        return this.getClass().getSimpleName();
    }
    
    public double getDistance() {

        return 0.0;
    }


    /** Checks whether this trip belongs to a specific client
      	used when listing trips by client  */
    public boolean hasClientId(String clientId) {
        if (client == null || clientId == null)
        	return false;
        
        return client.getClientId().equals(clientId);
    }

    
    /** Iterates over the partially filled array
      to determine which trip has the highest total cost  */
    public static Trip findMostExpensiveTrip(Trip[] trips, int tripCount) {
        Trip best = null;
        double bestCost = -1.0;

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null)
            	continue;
            
            double cost = trips[i].calculateTotalCost();
            if (best == null || cost > bestCost) {
                best = trips[i];
                bestCost = cost;
            }
        }
        return best;
    }

    
    /** Provides a clean summary for display and debugging */
    @Override
    public String toString() {
        String clientId = (client == null) ? "None" : client.getClientId();
        String transportId = (transportation == null) ? "None" : transportation.getTransportId();
        String accomId = (accommodation == null) ? "None" : accommodation.getAccommodationId();

        return "Trip{" +
                "tripId='" + tripId + "'" +
                ", destination='" + destination + "'" +
                ", durationInDays=" + durationInDays +
                ", basePrice=" + basePrice +
                ", clientId=" + clientId +
                ", transportationId=" + transportId +
                ", accommodationId=" + accomId +
                ", totalCost=" + calculateTotalCost() +
                "}";
    }


    /** Checks logical equality based on meaningful attributes
     Logical equality ignoring auto-generated ID */
    @Override
    public boolean equals(Object oth) {
        if (oth == null || getClass() != oth.getClass())
            return false;

        Trip other = (Trip) oth;

        return durationInDays == other.durationInDays
            && basePrice == other.basePrice
            && destination.equals(other.destination);
    }
}