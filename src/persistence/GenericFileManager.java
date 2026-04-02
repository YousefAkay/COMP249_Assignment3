package persistence;

import contracts.CsvPersistable;

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

    /** Generic parser contract used to rebuild entities from one CSV row. */
    public interface CsvParser<T> {
        T parse(String csvRow) throws Exception;
    }

    /** Saves all provided entities as one CSV row per line. */
    public static <T extends CsvPersistable> void saveAll(List<T> items, String filePath) throws IOException {
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

    /** Loads CSV rows using the provided parser while logging bad lines and continuing. */
    public static <T> List<T> loadAll(String filePath, CsvParser<T> parser) throws IOException {
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
                    loadedItems.add(parser.parse(line));
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

    /** Creates the output directory path before saving files into it. */
    private static void ensureParentDir(String filePath) {
        File outputFile = new File(filePath);
        File parentDirectory = outputFile.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}
