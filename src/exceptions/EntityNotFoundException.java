// -----------------------------------------------------
// SmartTravel Manager
// Class: EntityNotFoundException
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package exceptions;

/** Signals that a requested ID could not be found in the current data. */
public class EntityNotFoundException extends Exception {

    /** Passes the not-found message to the parent exception class. */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
