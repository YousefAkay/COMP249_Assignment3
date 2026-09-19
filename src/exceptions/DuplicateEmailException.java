// -----------------------------------------------------
// SmartTravel Manager
// Class: DuplicateEmailException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals that a client email already exists in the system. */
public class DuplicateEmailException extends RuntimeException {

    /** Passes the duplicate-email message to the parent exception class. */
    public DuplicateEmailException(String message) {
        super(message);
    }
}
