package contracts;

/** Common contract for models that can report a billable amount. */
public interface Billable {

    /** Returns the billable amount represented by the model. */
    double getBillableAmount();
}
