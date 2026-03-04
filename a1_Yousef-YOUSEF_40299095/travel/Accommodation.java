// -----------------------------------------------------
// Assignment 1
// Class: Accommodation
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package travel;

/** Core Accommodation entity in the SmartTravel system */
public abstract class Accommodation {

    private static int nextId = 4001;

    private String accommodationId;
    private String name;
    private String location;
    private double pricePerNight;

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "A" + nextId++;
    }

    /** Default constructor
     Builds a valid Accommodation object (ID handled automatically) */
    public Accommodation() {
        this.accommodationId = generateId();
        this.name = "Unknown";
        this.location = "Unknown";
        this.pricePerNight = 0.0;
    }

    /** Parameterized constructor (ID auto-generated)
     Builds a valid Accommodation object (ID handled automatically) */
    public Accommodation(String name, String location, double pricePerNight) {
        this.accommodationId = generateId();
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
    }

    /** Copy constructor (new ID must be generated) */
    /** Builds a valid Accommodation object (ID handled automatically) */
    public Accommodation(Accommodation other) {
        this.accommodationId = generateId();
        this.name = other.name;
        this.location = other.location;
        this.pricePerNight = other.pricePerNight;
    }

    /** setters and getters */
    public String getAccommodationId() {
        return accommodationId;
    }

    /** ClientId setter is unnecessary as ID is auto-generated */
   
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

  
    public String getLocation() {
        return location;
    }

  
    public void setLocation(String location) {
        this.location = location;
    }

   
    public double getPricePerNight() {
        return pricePerNight;
    }

    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    
     /** Returns the concrete trip type based on the runtime class name */
    public String getTripType() {
        return this.getClass().getSimpleName();
    }

   
    /** Calculates the accommodation cost based on trip duration
   Declared abstract to enforce subclass-specific cost logic  */
    public abstract double calculateCost(int numberOfDays);

    
    /** Creates a deep copy of a single Accommodation object
    Preserves the runtime subclass type using copy constructors */
    public static Accommodation deepCopyOne(Accommodation original) {
        if (original == null) 
        	return null;

        if (original instanceof Hotel) 
        	return new Hotel((Hotel) original);
        if (original instanceof Hostel)
        	return new Hostel((Hostel) original);

        return null;
    }

    
    /** Creates a deep copy of a Accommodation array
    Ensures copied objects are independent from the original array */
    public static Accommodation[] copyAccommodationArray(Accommodation[] original) {
        if (original == null) 
        	return null;

        Accommodation[] copied = new Accommodation[original.length];
        for (int i = 0; i < original.length; i++) {
            copied[i] = deepCopyOne(original[i]);
        }
        return copied;
    }
    

    /** Provides a clean summary for display */
    @Override
    public String toString() {
        return "Accommodation{" +
                "accommodationId='" + accommodationId + "'" +
                ", name='" + name + "'" +
                ", location='" + location + "'" +
                ", pricePerNight=" + pricePerNight +
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

        Accommodation other = (Accommodation) otherObject;

        return name.equals(other.name)
            && location.equals(other.location)
            && pricePerNight == other.pricePerNight;
    }
}