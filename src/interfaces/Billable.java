// -----------------------------------------------------
// SmartTravel Manager
// Interface: Billable
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package interfaces;

/** Common contract for models that expose both base price and total cost. */
public interface Billable {

    /** Returns the model's base price before optional additions are applied. */
    double getBasePrice();

    /** Returns the current total cost represented by the model. */
    double getTotalCost();
}
