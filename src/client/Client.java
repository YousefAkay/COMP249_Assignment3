// -----------------------------------------------------
// Assignment 2
// Class: Client
// Written by: Yousef Yousef (40299095) & Hamza Shadeed (4034172)
// -----------------------------------------------------

package client;


import exceptions.InvalidClientDataException;

/** Core Client entity in the SmartTravel system */
public class Client {

    private static int nextId = 1001;

    private String clientId;
    private String firstName;
    private String lastName;
    private String email;

    private double amountSpent;

    public static void resetIdCounter() {
        nextId = 1001;
    }

    /** Generates the next sequential ID for this category */
    private static String generateId() {
        return "C" + nextId++;
    }

    /** Syncs nextId so IDs don't collide after loading from CSV */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        // expected like "C1042"
        if (!loadedId.startsWith("C")) return;
        try {
            int n = Integer.parseInt(loadedId.substring(1));
            if (n >= nextId) nextId = n + 1;
        } catch (NumberFormatException ignore) {
        }
    }

    /** Default constructor */
    public Client() {
        this.clientId = generateId();
        this.firstName = "Unknown";
        this.lastName = "Unknown";
        this.email = "unknown@example.com";
        this.amountSpent = 0.0;
    }

    /**  parameterized constructor */
    public Client(String firstName, String lastName, String email) throws InvalidClientDataException {
        this.clientId = generateId();
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.amountSpent = 0.0;
    }

    /** load-time constructor: uses explicit ID from CSV */
    public Client(String clientId, String firstName, String lastName, String email, double amountSpent)
            throws InvalidClientDataException {
        setClientIdForLoad(clientId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAmountSpent(amountSpent);
        syncNextIdFromLoadedId(clientId);
    }

    /** Copy constructor */
    public Client(Client other) {
        this.clientId = generateId();
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.amountSpent = other.amountSpent;
    }


    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static void validateName(String value, String fieldName) throws InvalidClientDataException {
        if (isBlank(value)) {
            throw new InvalidClientDataException(fieldName + " cannot be empty.");
        }
        if (value.trim().length() > 50) {
            throw new InvalidClientDataException(fieldName + " must be <= 50 characters.");
        }
    }

    private static void validateEmail(String value) throws InvalidClientDataException {
        if (isBlank(value)) {
            throw new InvalidClientDataException("Email cannot be empty.");
        }
        String e = value.trim();
        if (e.length() > 100) {
            throw new InvalidClientDataException("Email must be <= 100 characters.");
        }
        if (e.contains(" ")) {
            throw new InvalidClientDataException("Email cannot contain spaces.");
        }
        if (!(e.contains("@") && e.contains("."))) {
            throw new InvalidClientDataException("Email must contain '@' and '.'.");
        }
    }


    /** Load-only setter: do NOT use in normal app flow */
    private void setClientIdForLoad(String clientId) throws InvalidClientDataException {
        if (isBlank(clientId) || !clientId.startsWith("C")) {
            throw new InvalidClientDataException("Invalid clientId: " + clientId);
        }
        this.clientId = clientId.trim();
    }

    public String getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws InvalidClientDataException {
        validateName(firstName, "First name");
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws InvalidClientDataException {
        validateName(lastName, "Last name");
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidClientDataException {
        validateEmail(email);
        this.email = email.trim();
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) throws InvalidClientDataException {
        if (amountSpent < 0) {
            throw new InvalidClientDataException("amountSpent cannot be negative.");
        }
        this.amountSpent = amountSpent;
    }

    public void addToAmountSpent(double delta) throws InvalidClientDataException {
        if (delta < 0) {
            throw new InvalidClientDataException("Cannot add negative spending.");
        }
        this.amountSpent += delta;
    }

    /** Checks logical equality based on meaningful attributes */
    public boolean equals(Object oth) {
        if (oth == null) return false;
        if (this.getClass() != oth.getClass()) return false;

        Client other = (Client) oth;

        if (firstName == null) return other.firstName == null;
        if (!firstName.equals(other.firstName)) return false;

        if (lastName == null) return other.lastName == null;
        if (!lastName.equals(other.lastName)) return false;

        if (email == null) return other.email == null;
        return email.equals(other.email);
    }


    /** cleanly displays all meaningful attributes */
    public String toString() {
        return "Client{clientId='" + clientId + "', firstName='" + firstName + "', lastName='" + lastName +
                "', email='" + email + "', amountSpent=" + amountSpent + "}";
    }
}