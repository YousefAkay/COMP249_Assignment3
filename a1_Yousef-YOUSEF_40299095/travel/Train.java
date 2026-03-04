// -----------------------------------------------------
// Assignment 1
// Class: Train
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Train entity in the SmartTravel system */
public class Train extends Transportation {

    private String trainType;
    private String seatClass;  

    /** Default constructor
     Builds a valid Train object (ID handled automatically) */
    public Train() {
        super();
        this.trainType = "Unknown";
        this.seatClass = "Unknown";
    }

    /** Parameterized constructor
    Builds a valid Train object (ID handled automatically) */
    public Train(String companyName, String departureCity, String arrivalCity, String trainType, String seatClass) {
        super(companyName, departureCity, arrivalCity);
        this.trainType = trainType;
        this.seatClass = seatClass;
    }

    /** Copy constructor (new ID, not copied)
    Builds a valid Train object (ID handled automatically) */
    public Train(Train other) {
        super(other.getCompanyName(), other.getDepartureCity(), other.getArrivalCity());
        this.trainType = other.trainType;
        this.seatClass = other.seatClass;
    }

    /** setters and getters */
    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
   
    
    /** Calculates train cost based on base fare with upgrades for seat class and train type */
    @Override
    public double calculateCost(int numberOfDays) {
        double base = 120.0;
        if (seatClass != null && seatClass.equalsIgnoreCase("first")) {
            base += 60.0;
        }
        if (trainType != null && trainType.equalsIgnoreCase("high-speed")) {
            base += 40.0;
        }
        return base;
    }

 
    /** Provides a clean summary for display  */
    @Override
    public String toString() {
        return "Train{" +
                "transportId='" + getTransportId() + "'" +
                ", companyName='" + getCompanyName() + "'" +
                ", departureCity='" + getDepartureCity() + "'" +
                ", arrivalCity='" + getArrivalCity() + "'" +
                ", trainType='" + trainType + "'" +
                ", seatClass='" + seatClass + "'" +
                "}";
    }


    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object oth) {
        if (!super.equals(oth)) 
        	return false;

        Train other = (Train) oth;

        if (this.trainType == null) {
            if (other.trainType != null)
            	return false;
        } else if (!this.trainType.equals(other.trainType))
        	return false;

        if (this.seatClass == null) {
            return other.seatClass == null;
        } else {
            return this.seatClass.equals(other.seatClass);
        }
    }
}