// -----------------------------------------------------
// SmartTravel Manager
// Class: Flight
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Flight entity in the SmartTravel system */
public class Flight extends Transportation {

    private String airlineName;
    private double luggageAllowanceKg;

    /** Builds a default flight with the default base fare. */
    public Flight() {
        super();
        this.airlineName = "Unknown";
        this.luggageAllowanceKg = 0.0;

        try {
            setBaseFare(350.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    /** Builds a flight using user input and the standard flight base fare. */
    public Flight(String companyName, String departureCity, String arrivalCity,
                  String airlineName, double luggageAllowanceKg) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setAirlineName(airlineName);
        setLuggageAllowanceKg(luggageAllowanceKg);
        setBaseFare(350.0);
    }

    /** Builds a flight using a base fare that usually comes from file data. */
    public Flight(String companyName, String departureCity, String arrivalCity,
                  String airlineName, double baseFare, double luggageAllowanceKg)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setAirlineName(airlineName);
        setLuggageAllowanceKg(luggageAllowanceKg);
    }

    /** Builds a flight from file data using an explicit transport ID. */
    public Flight(String transportId, String companyName, String departureCity,
                  String arrivalCity, String airlineName, double baseFare, double luggageAllowanceKg)
            throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setAirlineName(airlineName);
        setLuggageAllowanceKg(luggageAllowanceKg);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Flight(Flight otherFlight) {
        super(otherFlight);
        this.airlineName = otherFlight.airlineName;
        this.luggageAllowanceKg = otherFlight.luggageAllowanceKg;
    }

    /** Treats null or whitespace-only text as blank input. */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) throws InvalidTransportDataException {
        if (isBlank(airlineName)) {
            throw new InvalidTransportDataException("airlineName cannot be empty.");
        }
        this.airlineName = airlineName.trim();
    }

    public double getLuggageAllowanceKg() {
        return luggageAllowanceKg;
    }

    /** Enforces the luggage rule that baggage allowance cannot be negative. */
    public void setLuggageAllowanceKg(double luggageAllowanceKg) throws InvalidTransportDataException {
        if (luggageAllowanceKg < 0) {
            throw new InvalidTransportDataException("luggageAllowanceKg cannot be negative.");
        }
        this.luggageAllowanceKg = luggageAllowanceKg;
    }

    /** Adds excess baggage fees only when the allowance goes past 20 kg. */
    @Override
    public double calculateCost(int numberOfDays) {
        double flightBaseFare = getBaseFare();
        double baggageFee = 0.0;

        if (luggageAllowanceKg > 20.0) {
            baggageFee = (luggageAllowanceKg - 20.0) * 10.0;
        }

        return flightBaseFare + baggageFee;
    }

    /** Compares the meaningful state of two flight objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (!(otherObject instanceof Flight)) return false;

        Flight otherFlight = (Flight) otherObject;

        if (airlineName == null) {
            if (otherFlight.airlineName != null) return false;
        } else if (!airlineName.equals(otherFlight.airlineName)) {
            return false;
        }

        return Double.compare(luggageAllowanceKg, otherFlight.luggageAllowanceKg) == 0;
    }

    /** Returns a clean text summary for menus, testing, and logging using super. */
    @Override
    public String toString() {
        return super.toString().replace("Transportation", "Flight").replace("}", "") +
                ", airlineName='" + airlineName + "', luggageAllowanceKg=" + luggageAllowanceKg + "}";
    }

    /** Serializes the flight using the current CSV format. */
    @Override
    public String toCsvRow() {
        return "FLIGHT;" + getTransportId() + ";" + getCompanyName() + ";" +
                getDepartureCity() + ";" + getArrivalCity() + ";" +
                airlineName + ";" + getBaseFare() + ";" + luggageAllowanceKg;
    }

    @Override
    public String getType() {
        return "FLIGHT";
    }
}
