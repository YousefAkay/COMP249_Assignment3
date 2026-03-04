// -----------------------------------------------------
// Assignment 1
// Class: Transportation
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Transportation entity in the SmartTravel system */
public abstract class Transportation {

	private static int nextId = 3001;

	    private String transportId;
	    private String companyName;
	    private String departureCity;
	    private String arrivalCity;

	    /** Generates the next sequential ID for this category */
	    private static String generateId() {
	        return "TR" + nextId++;
	    }

	    /** Default constructor
	     Builds a valid Transportation object (ID handled automatically) */
	    public Transportation() {
	        this.transportId = generateId();
	        this.companyName = "Unknown";
	        this.departureCity = "Unknown";
	        this.arrivalCity = "Unknown";
	    }

	    /** Parameterized constructor (ID auto-generated)
	     Builds a valid Transportation object (ID handled automatically). */
	    public Transportation(String companyName, String departureCity, String arrivalCity) {
	        this.transportId = generateId();
	        this.companyName = companyName;
	        this.departureCity = departureCity;
	        this.arrivalCity = arrivalCity;
	    }

	    /** Copy constructor (new ID must be generated, not copied) 
	     Builds a valid Transportation object (ID handled automatically) */
	    public Transportation(Transportation other) {
	        this.transportId = generateId();
	        this.companyName = other.companyName;
	        this.departureCity = other.departureCity;
	        this.arrivalCity = other.arrivalCity;
	    }

	    /** setters and getters */
	    public String getTransportId() {
	        return transportId;
	    }

	   /** Id setter is unnecessary as ID is auto-generated */
	   
	    public String getCompanyName() {
	        return companyName;
	    }

	 
	    public void setCompanyName(String companyName) {
	        this.companyName = companyName;
	    }

	  
	    public String getDepartureCity() {
	        return departureCity;
	    }

	
	    public void setDepartureCity(String departureCity) {
	        this.departureCity = departureCity;
	    }

	 
	    public String getArrivalCity() {
	        return arrivalCity;
	    }

	   
	    public void setArrivalCity(String arrivalCity) {
	        this.arrivalCity = arrivalCity;
	    }

	    /** Returns the concrete trip type based on the runtime class name */
	    public String getTripType() {
	        return this.getClass().getSimpleName();
	    }

	    
	    
	     /** Calculates the transportation cost based on trip duration
	     Declared abstract to enforce subclass-specific cost logic */
	    public abstract double calculateCost(int numberOfDays);

	    
	    /** Creates a deep copy of a single Transportation object
	      Preserves the runtime subclass type using copy constructors */
	    public static Transportation deepCopyOne(Transportation original) {
	        if (original == null) 
	        	return null;

	        if (original instanceof Flight) 
	        	return new Flight((Flight) original);
	        if (original instanceof Train)  
	        	return new Train((Train) original);
	        if (original instanceof Bus)   
	        	return new Bus((Bus) original);

	        return null;
	    }

	    
	    /** Creates a deep copy of a Transportation array
	      Ensures copied objects are independent from the original array */
	    public static Transportation[] copyTransportationArray(Transportation[] original) {
	        if (original == null) 
	        	return null;

	        Transportation[] copied = new Transportation[original.length];
	        for (int i = 0; i < original.length; i++) {
	            copied[i] = deepCopyOne(original[i]);
	        }
	        return copied;
	    }
	   
	    /** Provides a clean summary for display */
	    @Override
	    public String toString() {
	        return "Transportation{" +
	                "transportId='" + transportId + "'" +
	                ", companyName='" + companyName + "'" +
	                ", departureCity='" + departureCity + "'" +
	                ", arrivalCity='" + arrivalCity + "'" +
	                ", type='" + getTripType() + "'" +
	                "}";
	    }

	    /** Checks logical equality based on meaningful attributes */
	    @Override
	    public boolean equals(Object otherObject) {
	        if (otherObject == null) 
	        	return false;
	        if (getClass() != otherObject.getClass())
	        	return false;

	        Transportation other = (Transportation) otherObject;

	        return companyName.equals(other.companyName)
	            && departureCity.equals(other.departureCity)
	            && arrivalCity.equals(other.arrivalCity);
	    }
}