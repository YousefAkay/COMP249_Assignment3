// -----------------------------------------------------
// Assignment 3
// Interface: Billable
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package interfaces;

/** Common contract for models that expose both base price and total cost. */
public interface Billable {

    /** Returns the model's base price before optional additions are applied. */
    double getBasePrice();

    /** Returns the current total cost represented by the model. */
    double getTotalCost();
}
