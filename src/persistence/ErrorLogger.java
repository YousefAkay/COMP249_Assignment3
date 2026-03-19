package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorLogger {

    private static final String DEFAULT_PATH = "output/logs/errors.txt";

    public static void log(String message) {
        log(DEFAULT_PATH, message);
    }

    public static void log(String filePath, String message) {
        if (message == null) {
            message = "null";
        }

        ensureParentDir(filePath);

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filePath, true));
            out.println(message);
        } catch (IOException ignore) {
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void ensureParentDir(String filePath) {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}