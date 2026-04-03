// -----------------------------------------------------
// Assignment 3
// Class: InvalidTransportDataException
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package exceptions;

/** Signals invalid transportation data during creation, editing, or loading. */
public class InvalidTransportDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidTransportDataException(String message) {
        super(message);
    }
}
