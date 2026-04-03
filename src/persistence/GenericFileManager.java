// -----------------------------------------------------
// Assignment 3
// Class: GenericFileManager
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package persistence;

import client.Client;
import interfaces.CsvPersistable;
import travel.Accommodation;
import travel.Transportation;
import travel.Trip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/** Generic CSV persistence helper for A3 entities that implement CsvPersistable. */
public class GenericFileManager {

    /** Loads CSV rows by dispatching to the model class's existing fromCsvRow factory. */
    public static <T extends CsvPersistable> List<T> load(String filePath, Class<T> clazz) throws IOException {
        List<T> loadedItems = new ArrayList<T>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String rawLine = line;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    loadedItems.add(parseCsvRow(line, clazz));
                } catch (Exception exception) {
                    ErrorLogger.log("GENERIC LOAD ERROR | " + exception.getMessage() + " | line=" + rawLine);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return loadedItems;
    }

    /** Saves all provided entities as one CSV row per line. */
    public static <T extends CsvPersistable> void save(List<T> items, String filePath) throws IOException {
        ensureParentDir(filePath);

        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));
        try {
            if (items == null) {
                return;
            }

            for (int index = 0; index < items.size(); index++) {
                T item = items.get(index);
                if (item != null) {
                    outputWriter.println(item.toCsvRow());
                }
            }
        } finally {
            outputWriter.close();
        }
    }

    /** Keeps the class-based CSV dispatch simple for TA demos and debugging. */
    private static <T extends CsvPersistable> T parseCsvRow(String csvRow, Class<T> clazz) throws Exception {
        if (clazz == Client.class) {
            return clazz.cast(Client.fromCsvRow(csvRow));
        }
        if (clazz == Trip.class) {
            return clazz.cast(Trip.fromCsvRow(csvRow));
        }
        if (clazz == Accommodation.class) {
            return clazz.cast(Accommodation.fromCsvRow(csvRow));
        }
        if (clazz == Transportation.class) {
            return clazz.cast(Transportation.fromCsvRow(csvRow));
        }

        throw new IllegalArgumentException("Unsupported CSV class: " + clazz.getName());
    }

    /** Creates the output directory path before saving files into it. */
    private static void ensureParentDir(String filePath) {
        File outputFile = new File(filePath);
        File parentDirectory = outputFile.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}
