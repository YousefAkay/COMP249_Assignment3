// -----------------------------------------------------
// Assignment 2
// Class: Train
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

public class Train extends Transportation {

    private String trainType;

    public Train() {
        super();
        this.trainType = "Standard";
        try {
            setBaseFare(120.0);
        } catch (InvalidTransportDataException ignore) {
        }
    }

    public Train(String companyName, String departureCity, String arrivalCity, String trainType)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity);
        setTrainType(trainType);
        setBaseFare(120.0);
    }

    public Train(String companyName, String departureCity, String arrivalCity, String trainType, double baseFare)
            throws InvalidTransportDataException {
        super(companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
    }

    public Train(String transportId, String companyName, String departureCity, String arrivalCity,
                 String trainType, double baseFare) throws InvalidTransportDataException {
        super(transportId, companyName, departureCity, arrivalCity, baseFare);
        setTrainType(trainType);
    }

    public Train(Train other) {
        super(other);
        this.trainType = other.trainType;
    }

    public static void resetIdCounter() {
        syncNextIdFromLoadedId("TR3000");
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

    @Override
    public double calculateCost(int numberOfDays) {
        double base = getBaseFare();

        if (trainType != null && trainType.equalsIgnoreCase("highspeed")) {
            base += 40.0;
        }

        return base;
    }

    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth)) return false;
        if (!(oth instanceof Train)) return false;

        Train other = (Train) oth;

        if (trainType == null) return other.trainType == null;
        return trainType.equals(other.trainType);
    }

    @Override
    public String toString() {
        return "Train{transportId='" + getTransportId() + "', companyName='" + getCompanyName() +
                "', departureCity='" + getDepartureCity() + "', arrivalCity='" + getArrivalCity() +
                "', trainType='" + trainType + "', baseFare=" + getBaseFare() + "}";
    }

    /** EQUALS */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Train other = (Train) obj;

        if (trainType == null && other.trainType != null) return false;
        if (trainType != null && !trainType.equals(other.trainType)) return false;

        if (seatClass == null && other.seatClass != null) return false;
        if (seatClass != null && !seatClass.equals(other.seatClass)) return false;

        if (Double.compare(getBaseFare(), other.getBaseFare()) != 0) return false;

        return true;
    }

    @Override
    public String getType() { return "Train"; }
}