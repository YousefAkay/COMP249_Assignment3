// -----------------------------------------------------
// Assignment 3
// Class: DuplicateEmailException
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package exceptions;

/** Signals that a client email already exists in the system. */
public class DuplicateEmailException extends RuntimeException {

    /** Passes the duplicate-email message to the parent exception class. */
    public DuplicateEmailException(String message) {
        super(message);
    }
}
