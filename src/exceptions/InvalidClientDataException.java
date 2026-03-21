// -----------------------------------------------------
// Assignment 2
// Class: InvalidClientDataException
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package exceptions;

/** Signals invalid client data during creation, editing, or loading. */
public class InvalidClientDataException extends Exception {

    /** Passes the validation message to the parent exception class. */
    public InvalidClientDataException(String message) {
        super(message);
    }
}