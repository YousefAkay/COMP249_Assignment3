// -----------------------------------------------------
// SmartTravel Manager
// Class: InvalidTransportDataException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals invalid transportation data during creation, editing, or loading. */
public class InvalidTransportDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidTransportDataException(String message) {
        super(message);
    }
}
