// -----------------------------------------------------
// SmartTravel Manager
// Interface: Identifiable
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package interfaces;

/** Common contract for entities that expose a stable identifier. */
public interface Identifiable {

    /** Returns the entity identifier without changing existing model-specific getter names. */
    String getId();
}
