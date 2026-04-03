// -----------------------------------------------------
// Assignment 3
// Interface: CsvPersistable
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package interfaces;

/** Common contract for models that can serialize themselves as one CSV row. */
public interface CsvPersistable {

    /** Returns a CSV row compatible with the current project persistence format. */
    String toCsvRow();
}
