// -----------------------------------------------------
// Assignment 3
// Class: EntityNotFoundException
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package exceptions;

/** Signals that a requested ID could not be found in the current data. */
public class EntityNotFoundException extends Exception {

    /** Passes the not-found message to the parent exception class. */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
