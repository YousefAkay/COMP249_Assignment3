// -----------------------------------------------------
// Assignment 2
// Class: Bus
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Bus entity in the SmartTravel system */
public class Bus extends Transportation {

    private int numberOfStops;

    /** Default constructor: base fare = 60, stops = 1 */
    public Bus() {
        super();
        this.numberOfStops = 1;
        try {
            setBaseFare(60.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    /** constructor: base fare = 60 */
    public Bus(String companyName, String departureCity, String arrivalCity, int numberOfStops)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setNumberOfStops(numberOfStops);
        setBaseFare(60.0);
    }

    /** constructor (base fare comes from CSV) */
    public Bus(String companyName, String departureCity, String arrivalCity, double baseFare, int numberOfStops)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setNumberOfStops(numberOfStops);
    }

    /** load-time constructor (explicit ID) */
    public Bus(String transportId, String companyName, String departureCity, String arrivalCity,
               double baseFare, int numberOfStops) throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setNumberOfStops(numberOfStops);
    }

    /** Copy constructor */
    public Bus(Bus other) {
        super(other);
        this.numberOfStops = other.numberOfStops;
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(int numberOfStops) throws InvalidTransportDataException {
        // A2 rule: Bus requires >= 1 stop
        if (numberOfStops < 1) {
            throw new InvalidTransportDataException("Bus numberOfStops must be >= 1.");
        }
        this.numberOfStops = numberOfStops;
    }

    /** calculateCost uses baseFare now */
    @Override
    public double calculateCost(int numberOfDays) {
        return getBaseFare() + (numberOfStops * 5.0);
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Bus{transportId='" + getTransportId() + "', companyName='" + getCompanyName() +
                "', departureCity='" + getDepartureCity() + "', arrivalCity='" + getArrivalCity() +
                "', baseFare=" + getBaseFare() + ", numberOfStops=" + numberOfStops + "}";
    }
}