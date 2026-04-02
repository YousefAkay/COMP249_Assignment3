package contracts;

/** Common contract for models that can serialize themselves as one CSV row. */
public interface CsvPersistable {

    /** Returns a CSV row compatible with the current project persistence format. */
    String toCsvRow();
}
