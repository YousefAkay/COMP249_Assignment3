// -----------------------------------------------------
// Assignment 2
// Class: ErrorLogger
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Writes load and save errors to the required output log file. */
public class ErrorLogger {

    private static final String DEFAULT_PATH = "output/logs/errors.txt";

    /** Logs a message to the assignment's default error log file. */
    public static void log(String message) {
        log(DEFAULT_PATH, message);
    }

    /** Appends a message to the requested log file without crashing the program. */
    public static void log(String filePath, String message) {
        if (message == null) {
            message = "null";
        }

        ensureParentDir(filePath);

        PrintWriter outputWriter = null;
        try {
            outputWriter = new PrintWriter(new FileWriter(filePath, true));
            outputWriter.println(message);
        } catch (IOException ignoredException) {
        } finally {
            if (outputWriter != null) {
                outputWriter.close();
            }
        }
    }

    /** Creates the log directory path before writing error messages into it. */
    private static void ensureParentDir(String filePath) {
        File outputFile = new File(filePath);
        File parentDirectory = outputFile.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}