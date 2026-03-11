// -----------------------------------------------------
// Assignment 2
// Class: Train
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Train entity in the SmartTravel system */
public class Train extends Transportation {

    private String trainType;
    private String seatClass;

    /** Default constructor: base fare = 120 */
    public Train() {
        super();
        this.trainType = "Standard";
        this.seatClass = "Economy";
        try {
            setBaseFare(120.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    /** constructor: base fare = 120 */
    public Train(String companyName, String departureCity, String arrivalCity, String trainType, String seatClass)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setTrainType(trainType);
        setSeatClass(seatClass);
        setBaseFare(120.0);
    }

    /** constructor (base fare comes from CSV) */
    public Train(String companyName, String departureCity, String arrivalCity, String trainType, String seatClass,
                 double baseFare) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
        setSeatClass(seatClass);
    }

    /** load-time constructor (explicit ID) */
    public Train(String transportId, String companyName, String departureCity, String arrivalCity,
                 String trainType, String seatClass, double baseFare) throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
        setSeatClass(seatClass);
    }

    /** Copy constructor */
    public Train(Train other) {
        super(other);
        this.trainType = other.trainType;
        this.seatClass = other.seatClass;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) throws InvalidTransportDataException {
        if (isBlank(trainType)) {
            throw new InvalidTransportDataException("trainType cannot be empty.");
        }
        this.trainType = trainType.trim();
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) throws InvalidTransportDataException {
        if (isBlank(seatClass)) {
            throw new InvalidTransportDataException("seatClass cannot be empty.");
        }
        this.seatClass = seatClass.trim();
    }

    /** now uses baseFare instead of hardcoded base */
    @Override
    public double calculateCost(int numberOfDays) {
        double base = getBaseFare();

        if (seatClass != null && seatClass.equalsIgnoreCase("first")) {
            base += 60.0;
        }
        if (trainType != null && trainType.equalsIgnoreCase("highspeed")) {
            base += 40.0;
        }
        return base;
    }

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Train{transportId='" + getTransportId() + "', companyName='" + getCompanyName() +
                "', departureCity='" + getDepartureCity() + "', arrivalCity='" + getArrivalCity() +
                "', trainType='" + trainType + "', seatClass='" + seatClass + "', baseFare=" + getBaseFare() + "}";
    }
}