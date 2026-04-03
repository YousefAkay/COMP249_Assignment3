// -----------------------------------------------------
// Assignment 3
// Interface: Identifiable
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package interfaces;

/** Common contract for entities that expose a stable identifier. */
public interface Identifiable {

    /** Returns the entity identifier without changing existing model-specific getter names. */
    String getId();
}
