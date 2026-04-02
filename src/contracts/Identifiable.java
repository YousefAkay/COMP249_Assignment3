package contracts;

/** Common contract for entities that expose a stable identifier. */
public interface Identifiable {

    /** Returns the entity identifier without changing existing A2 getter names. */
    String getId();
}
