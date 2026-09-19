// -----------------------------------------------------
// SmartTravel Manager
// Class: Train
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Train entity in the SmartTravel system */
public class Train extends Transportation {

    private String trainType;

    /** Builds a default train with the default base fare. */
    public Train() {
        super();
        this.trainType = "Standard";

        try {
            setBaseFare(120.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    /** Builds a train using user input and the standard train base fare. */
    public Train(String companyName, String departureCity, String arrivalCity, String trainType)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setTrainType(trainType);
        setBaseFare(120.0);
    }

    /** Builds a train using a base fare that usually comes from file data. */
    public Train(String companyName, String departureCity, String arrivalCity,
                 String trainType, double baseFare) throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
    }

    /** Builds a train from file data using an explicit transport ID. */
    public Train(String transportId, String companyName, String departureCity,
                 String arrivalCity, String trainType, double baseFare)
            throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
    }

    /** Creates a deep-style copy with a fresh generated ID. */
    public Train(Train otherTrain) {
        super(otherTrain);
        this.trainType = otherTrain.trainType;
    }

    /** Treats null or whitespace-only text as blank input. */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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

    /** Adds a surcharge only when the train type is high-speed. */
    @Override
    public double calculateCost(int numberOfDays) {
        double trainCost = getBaseFare();

        if (trainType != null && trainType.equalsIgnoreCase("highspeed")) {
            trainCost += 40.0;
        }

        return trainCost;
    }

    /** Compares the meaningful state of two train objects. */
    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) return false;
        if (!(otherObject instanceof Train)) return false;

        Train otherTrain = (Train) otherObject;

        if (trainType == null) return otherTrain.trainType == null;
        return trainType.equals(otherTrain.trainType);
    }

    /** Returns a clean text summary for menus, testing, and logging using super. */
    @Override
    public String toString() {
        return super.toString().replace("Transportation", "Train").replace("}", "") +
                ", trainType='" + trainType + "'}";
    }

    /** Serializes the train using the current CSV format. */
    @Override
    public String toCsvRow() {
        return "TRAIN;" + getTransportId() + ";" + getCompanyName() + ";" +
                getDepartureCity() + ";" + getArrivalCity() + ";" +
                getBaseFare() + ";" + trainType;
    }

    @Override
    public String getType() {
        return "TRAIN";
    }
}
