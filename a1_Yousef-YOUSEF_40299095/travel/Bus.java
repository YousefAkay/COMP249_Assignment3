// -----------------------------------------------------
// Assignment 1
// Class: Bus
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Bus entity in the SmartTravel system */
public class Bus extends Transportation {

    private String busCompany;
    private int numberOfStops;

    // Default constructor
    /** Builds a valid Bus object (ID handled automatically) */
    public Bus() {
        super();
        this.busCompany = "Unknown";
        this.numberOfStops = 0;
    }

    /** Parameterized constructor
     Builds a valid Bus object (ID handled automatically) */
    public Bus(String companyName, String departureCity, String arrivalCity,
               String busCompany, int numberOfStops) {
        super(companyName, departureCity, arrivalCity);
        this.busCompany = busCompany;
        this.numberOfStops = numberOfStops;
    }

    // Copy constructor (new ID, not copied)
    /** Builds a valid Bus object (ID handled automatically) */
    public Bus(Bus other) {
        super(other.getCompanyName(), other.getDepartureCity(), other.getArrivalCity());
        this.busCompany = other.busCompany;
        this.numberOfStops = other.numberOfStops;
    }

    /** setters and getters */
    public String getBusCompany() {
        return busCompany;
    }

    
    public void setBusCompany(String busCompany) {
        this.busCompany = busCompany;
    }

  
    public int getNumberOfStops() {
        return numberOfStops;
    }

   
    public void setNumberOfStops(int numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    
    /** Calculates bus cost based on base fare and fee per stop */
    @Override
    public double calculateCost(int numberOfDays) {
        double base = 60.0;
        return base + (numberOfStops * 5.0);
    }

    
    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Bus{" +
                "transportId='" + getTransportId() + "'" +
                ", companyName='" + getCompanyName() + "'" +
                ", departureCity='" + getDepartureCity() + "'" +
                ", arrivalCity='" + getArrivalCity() + "'" +
                ", busCompany='" + busCompany + "'" +
                ", numberOfStops=" + numberOfStops +
                "}";
    }


    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth))
            return false;

        Bus other = (Bus) oth;

        if (this.busCompany == null) {
            if (other.busCompany != null)
                return false;
        } else if (!this.busCompany.equals(other.busCompany))
            return false;

        return this.numberOfStops == other.numberOfStops;
    }
}