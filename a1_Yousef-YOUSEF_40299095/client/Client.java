// -----------------------------------------------------
// Assignment 1
// Class: Client
// Written by: Yousef Yousef (40299095)
// -----------------------------------------------------

package client;

/** Core Client entity in the SmartTravel system */
public class Client {
  
	private static int nextId = 1001;

    private String clientId;
    private String firstName;
    private String lastName;
    private String email;

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "C" + nextId++;
    }

    /** Default constructor
     Builds a valid Client object (ID handled automatically) */
    public Client() {
        this.clientId = generateId();
        this.firstName = "Unknown";
        this.lastName = "Unknown";
        this.email = "unknown@example.com";
    }

    /** Parameterized constructor (ID is auto-generated)
     Builds a valid Client object (ID handled automatically) */
    public Client(String firstName, String lastName, String email) {
        this.clientId = generateId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    /** Copy constructor (ID must be newly generated)
     Builds a valid Client object (ID handled automatically) */
    public Client(Client other) {
        this.clientId = generateId();
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
    }

    /** setters and getters */
    public String getClientId() {
        return clientId;
    }

    /** Id setter is unnecessary as ID is auto-generated */

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {


        this.firstName = firstName;
    }

    
    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
    public String getEmail() {
        return email;
    }

 
    public void setEmail(String email) {
        this.email = email;
    }



    /** Provides a clean summary for display  */
    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + "'" +
                ", firstName='" + firstName + "'" +
                ", lastName='" + lastName + "'" +
                ", email='" + email + "'" +
                "}";
    }

    
    /** Checks logical equality based on meaningful attributes */
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) 
        	return false;
        if (getClass() != otherObject.getClass()) 
        	return false;

        Client other = (Client) otherObject;

        return firstName.equals(other.firstName)
            && lastName.equals(other.lastName)
            && email.equals(other.email);
    }

}