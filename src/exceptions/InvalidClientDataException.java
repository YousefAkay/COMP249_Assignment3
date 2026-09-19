// -----------------------------------------------------
// SmartTravel Manager
// Class: InvalidClientDataException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals invalid client data during creation, editing, or loading. */
public class InvalidClientDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidClientDataException(String message) {
        super(message);
    }
}
