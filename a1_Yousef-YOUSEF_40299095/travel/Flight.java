// -----------------------------------------------------
// Assignment 1
// Class: Flight
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Flight entity in the SmartTravel system */
public class Flight extends Transportation {


    private String airlineName;
    private double luggageAllowanceKg;

    /** Default constructor
     Builds a valid Flight object (ID handled automatically) */
    public Flight() {
        super();
        this.airlineName = "Unknown";
        this.luggageAllowanceKg = 0.0;
    }

    /** Parameterized constructor
     Builds a valid Flight object (ID handled automatically) */
    public Flight(String companyName, String departureCity, String arrivalCity, String airlineName, double luggageAllowanceKg) {
        super(companyName, departureCity, arrivalCity);
        this.airlineName = airlineName;
        this.luggageAllowanceKg = luggageAllowanceKg;
    }

    /** Copy constructor (new ID, not copied)
     Builds a valid Flight object (ID handled automatically) */
    public Flight(Flight other) {
        super(other.getCompanyName(), other.getDepartureCity(), other.getArrivalCity());
        this.airlineName = other.airlineName;
        this.luggageAllowanceKg = other.luggageAllowanceKg;
    }

    /** setters and getters */
    public String getAirlineName() {
        return airlineName;
    }

  
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

   
    public double getLuggageAllowanceKg() {
        return luggageAllowanceKg;
    }

    
    public void setLuggageAllowanceKg(double luggageAllowanceKg) {
        this.luggageAllowanceKg = luggageAllowanceKg;
    }

    
    /** Calculates flight cost using a flat base fare plus excess luggage fees */
    @Override
    public double calculateCost(int numberOfDays) {
        double base = 350.0;                 // flat
        double baggageFee = 0.0;
        if (luggageAllowanceKg > 20.0) {
            baggageFee = (luggageAllowanceKg - 20.0) * 5.0;
        }
        return base + baggageFee;
    }

    
    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Flight{" +
                "transportId='" + getTransportId() + "'" +
                ", companyName='" + getCompanyName() + "'" +
                ", departureCity='" + getDepartureCity() + "'" +
                ", arrivalCity='" + getArrivalCity() + "'" +
                ", airlineName='" + airlineName + "'" +
                ", luggageAllowanceKg=" + luggageAllowanceKg +
                "}";
    }

 
    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth))
            return false;

        Flight other = (Flight) oth;

        if (this.airlineName == null) {
            if (other.airlineName != null)
                return false;
        } else if (!this.airlineName.equals(other.airlineName))
            return false;

        return this.luggageAllowanceKg == other.luggageAllowanceKg;
    }
}