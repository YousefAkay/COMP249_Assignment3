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

/** Primary Assignment 3 CSV persistence helper for entities that implement CsvPersistable.
 *  The current load path supports only the four root model classes: Client, Trip,
 *  Accommodation, and Transportation.
 */
public class GenericFileManager {

    /** Primary A3 load entry point: reads rows, delegates parsing to each model's fromCsvRow factory, and logs bad records.
     *  This method currently supports only Client.class, Trip.class, Accommodation.class,
     *  and Transportation.class.
     */
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
                    ErrorLogger.log(getLoadErrorPrefix(clazz) + " | " + exception.getMessage() + " | line=" + rawLine);
                }
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return loadedItems;
    }

    /** Primary A3 save entry point: writes each entity using its own toCsvRow implementation. */
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

    /** Centralizes the small class-to-factory dispatch so the service can keep one generic load path.
     *  Only the four root Assignment 3 model classes are recognized here.
     */
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

    /** Maps each supported root model class to the legacy load-error prefix used elsewhere in persistence. */
    private static <T extends CsvPersistable> String getLoadErrorPrefix(Class<T> clazz) {
        if (clazz == Client.class) {
            return "CLIENT LOAD ERROR";
        }
        if (clazz == Trip.class) {
            return "TRIP LOAD ERROR";
        }
        if (clazz == Accommodation.class) {
            return "ACCOM LOAD ERROR";
        }
        if (clazz == Transportation.class) {
            return "TRANSPORT LOAD ERROR";
        }

        return "GENERIC LOAD ERROR";
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
