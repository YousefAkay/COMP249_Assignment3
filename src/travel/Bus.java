// -----------------------------------------------------
// Assignment 2
// Class: Bus
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Bus entity in the SmartTravel system */
public class Bus extends Transportation {

    private int numberOfStops;

    /** Builds a default bus with the assignment's standard base fare. */
    public Bus() {
        super();
        this.numberOfStops = 1;

        try {
            setBaseFare(60.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    /** Builds a bus using user input and the standard bus base fare. */
    public Bus(String companyName, String departureCity, String arrivalCity, int numberOfStops)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setNumberOfStops(numberOfStops);
        setBaseFare(60.0);
    }

    /** Builds a bus using a base fare that usually comes from file data. */
    public Bus(String companyName, String departureCity, String arrivalCity,
               double baseFare, int numberOfStops) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setNumberOfStops(numberOfStops);
    }

    /** Builds a bus from file data using an explicit transport ID. */
    public Bus(String transportId, String companyName, String departureCity,
               String arrivalCity, double baseFare, int numberOfStops)
            throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setNumberOfStops(numberOfStops);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Bus(Bus otherBus) {
        super(otherBus);
        this.numberOfStops = otherBus.numberOfStops;
    }

    public int getNumberOfStops() {
        return numberOfStops;
    }

    /** Enforces that a bus must have at least one stop. */
    public void setNumberOfStops(int numberOfStops) throws InvalidTransportDataException {
        if (numberOfStops < 1) {
            throw new InvalidTransportDataException("Bus numberOfStops must be >= 1.");
        }
        this.numberOfStops = numberOfStops;
    }

    /** Adds a fixed amount per stop to the stored base fare. */
    @Override
    public double calculateCost(int numberOfDays) {
        return getBaseFare() + (numberOfStops * 5.0);
    }

    /** Compares the meaningful state of two bus objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (!(otherObject instanceof Bus)) return false;

        Bus otherBus = (Bus) otherObject;
        return numberOfStops == otherBus.numberOfStops;
    }

    /** Returns a clean text summary for menus, testing, and logging using super. */
    @Override
    public String toString() {
        return super.toString().replace("Transportation", "Bus").replace("}", "") +
                ", numberOfStops=" + numberOfStops + "}";
    }

    /** Serializes the bus using the current A2-compatible CSV format. */
    @Override
    public String toCsvRow() {
        return "BUS;" + getTransportId() + ";" + getCompanyName() + ";" +
                getDepartureCity() + ";" + getArrivalCity() + ";" +
                getBaseFare() + ";" + numberOfStops;
    }

    @Override
    public String getType() {
        return "BUS";
    }
}
