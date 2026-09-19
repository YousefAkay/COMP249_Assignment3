// -----------------------------------------------------
// SmartTravel Manager
// Class: InvalidTripDataException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals invalid trip data during creation, editing, or loading. */
public class InvalidTripDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidTripDataException(String message) {
        super(message);
    }
}
