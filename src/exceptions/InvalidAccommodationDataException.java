// -----------------------------------------------------
// SmartTravel Manager
// Class: InvalidAccommodationDataException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals invalid accommodation data during creation, editing, or loading. */
public class InvalidAccommodationDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidAccommodationDataException(String message) {
        super(message);
    }
}
