// -----------------------------------------------------
// SmartTravel Manager
// Class: Client
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package client;

import interfaces.CsvPersistable;
import interfaces.Identifiable;
import exceptions.InvalidClientDataException;

/** Core Client entity in the SmartTravel system */
public class Client implements Identifiable, CsvPersistable, Comparable<Client> {

    private static int nextId = 1001;

    private String clientId;
    private String firstName;
    private String lastName;
    private String email;
    private double amountSpent;

    public static void resetIdCounter() {
        nextId = 1001;
    }

    /** Generates the next sequential ID for client objects. */
    private static String generateId() {
        return "C" + nextId++;
    }

    /** Syncs the next ID after loading records so new IDs stay unique. */
    public static void syncNextIdFromLoadedId(String loadedId) {
        if (loadedId == null) return;
        if (!loadedId.startsWith("C")) return;

        try {
            int loadedNumber = Integer.parseInt(loadedId.substring(1));
            if (loadedNumber >= nextId) {
                nextId = loadedNumber + 1;
            }
        } catch (NumberFormatException ignoredException) {
        }
    }

    /** Builds a default client with placeholder values. */
    public Client() {
        this.clientId = generateId();
        this.firstName = "Unknown";
        this.lastName = "Unknown";
        this.email = "unknown@example.com";
        this.amountSpent = 0.0;
    }

    /** Builds a client from validated user input. */
    public Client(String firstName, String lastName, String email) throws InvalidClientDataException {
        this.clientId = generateId();
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.amountSpent = 0.0;
    }

    /** Builds a client from file data using an explicit ID and stored spending. */
    public Client(String clientId, String firstName, String lastName, String email, double amountSpent)
            throws InvalidClientDataException {
        setClientIdForLoad(clientId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setAmountSpent(amountSpent);
        syncNextIdFromLoadedId(clientId);
    }

    /** Creates a copy of the client data but assigns a fresh generated ID. */
    public Client(Client otherClient) {
        this.clientId = generateId();
        this.firstName = otherClient.firstName;
        this.lastName = otherClient.lastName;
        this.email = otherClient.email;
        this.amountSpent = otherClient.amountSpent;
    }

    /** Treats null or whitespace-only text as blank input. */
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Applies the shared validation rules used by first and last names. */
    private static void validateName(String value, String fieldName) throws InvalidClientDataException {
        if (isBlank(value)) {
            throw new InvalidClientDataException(fieldName + " cannot be empty.");
        }
        if (value.trim().length() > 50) {
            throw new InvalidClientDataException(fieldName + " must be <= 50 characters.");
        }
    }

    /** Applies the validation rules required for email addresses. */
    private static void validateEmail(String value) throws InvalidClientDataException {
        if (isBlank(value)) {
            throw new InvalidClientDataException("Email cannot be empty.");
        }

        String trimmedEmail = value.trim();

        if (trimmedEmail.length() > 100) {
            throw new InvalidClientDataException("Email must be <= 100 characters.");
        }
        if (trimmedEmail.contains(" ")) {
            throw new InvalidClientDataException("Email cannot contain spaces.");
        }
        if (!(trimmedEmail.contains("@") && trimmedEmail.contains("."))) {
            throw new InvalidClientDataException("Email must contain '@' and '.'.");
        }
    }

    /** Stores a loaded client ID directly and should only be used during file loading. */
    private void setClientIdForLoad(String clientId) throws InvalidClientDataException {
        if (isBlank(clientId) || !clientId.startsWith("C")) {
            throw new InvalidClientDataException("Invalid clientId: " + clientId);
        }
        this.clientId = clientId.trim();
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public String getId() {
        return getClientId();
    }

    public String getFirstName() {
        return firstName;
    }

    /** Validates and stores the client's first name. */
    public void setFirstName(String firstName) throws InvalidClientDataException {
        validateName(firstName, "First name");
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    /** Validates and stores the client's last name. */
    public void setLastName(String lastName) throws InvalidClientDataException {
        validateName(lastName, "Last name");
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    /** Validates and stores the client's email address. */
    public void setEmail(String email) throws InvalidClientDataException {
        validateEmail(email);
        this.email = email.trim();
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    /** Enforces that stored client spending can never go negative. */
    public void setAmountSpent(double amountSpent) throws InvalidClientDataException {
        if (amountSpent < 0) {
            throw new InvalidClientDataException("amountSpent cannot be negative.");
        }
        this.amountSpent = amountSpent;
    }

    /** Adds new trip spending to the running total while rejecting negative values. */
    public void addToAmountSpent(double amountToAdd) throws InvalidClientDataException {
        if (amountToAdd < 0) {
            throw new InvalidClientDataException("Cannot add negative spending.");
        }
        this.amountSpent += amountToAdd;
    }

    /** Checks logical equality using the client's meaningful identity fields. */
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (this.getClass() != otherObject.getClass()) return false;

        Client otherClient = (Client) otherObject;

        if (firstName == null) return otherClient.firstName == null;
        if (!firstName.equals(otherClient.firstName)) return false;

        if (lastName == null) return otherClient.lastName == null;
        if (!lastName.equals(otherClient.lastName)) return false;

        if (email == null) return otherClient.email == null;
        return email.equals(otherClient.email);
    }

    /** Returns a compact text summary for menus, debugging, and output. */
    @Override
    public String toString() {
        return "Client{clientId='" + clientId + "', firstName='" + firstName + "', lastName='" + lastName +
                "', email='" + email + "', amountSpent=" + String.format("%.2f", amountSpent) + "}";
    }

    /** Serializes the client using the current CSV format. */
    @Override
    public String toCsvRow() {
        return clientId + ";" + firstName + ";" + lastName + ";" + email;
    }

    /** Reconstructs one client from a CSV row. */
    public static Client fromCsvRow(String csvRow) throws InvalidClientDataException {
        if (csvRow == null) {
            throw new InvalidClientDataException("Client CSV row cannot be null.");
        }

        String[] tokens = csvRow.split(";", -1);
        if (tokens.length != 4) {
            throw new InvalidClientDataException("Bad client CSV token count: " + csvRow);
        }

        return new Client(
                tokens[0].trim(),
                tokens[1].trim(),
                tokens[2].trim(),
                tokens[3].trim(),
                0.0
        );
    }

    /** Applies the natural business ordering: amountSpent descending. */
    @Override
    public int compareTo(Client otherClient) {
        if (otherClient == null) {
            return -1;
        }

        int spendingComparison = Double.compare(otherClient.amountSpent, amountSpent);
        if (spendingComparison != 0) {
            return spendingComparison;
        }

        return clientId.compareTo(otherClient.clientId);
    }
}
