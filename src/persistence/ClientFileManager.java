// -----------------------------------------------------
// SmartTravel Manager
// Class: ClientFileManager
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package persistence;

import client.Client;
import exceptions.InvalidClientDataException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Legacy array-based CSV helper retained for existing array-based callers; the service uses GenericFileManager via SmartTravelService. */
public class ClientFileManager {

    /** Saves each client as a semicolon-separated row in the older array-based persistence flow. */
    public static void saveClients(Client[] clients, int clientCount, String filePath) throws IOException {
        ensureParentDir(filePath);

        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            Client client = clients[clientIndex];
            if (client == null) continue;

            outputWriter.println(
                    client.getClientId() + ";" + client.getFirstName() + ";" +
                            client.getLastName() + ";" + client.getEmail()
            );
        }

        outputWriter.close();
    }

    /** Loads client rows for the legacy array-based path, rejecting duplicate emails within that in-memory load. */
    public static int loadClients(Client[] clients, String filePath) throws IOException {
        int loadedCount = 0;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (loadedCount >= clients.length) break;

                String rawLine = line;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] tokens = line.split(";", -1);
                    if (tokens.length != 4) {
                        throw new InvalidClientDataException("Bad client CSV token count: " + rawLine);
                    }

                    String clientId = tokens[0].trim();
                    String firstName = tokens[1].trim();
                    String lastName = tokens[2].trim();
                    String email = tokens[3].trim();

                    /* Prevent duplicate emails from being loaded into the in-memory array. */
                    for (int clientIndex = 0; clientIndex < loadedCount; clientIndex++) {
                        if (clients[clientIndex] != null &&
                                clients[clientIndex].getEmail().equalsIgnoreCase(email)) {
                            throw new InvalidClientDataException("Duplicate email in clients.csv: " + email);
                        }
                    }

                    clients[loadedCount++] = new Client(clientId, firstName, lastName, email, 0.0);

                } catch (Exception exception) {
                    ErrorLogger.log("CLIENT LOAD ERROR | " + exception.getMessage() + " | line=" + rawLine);
                }
            }

        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }

        return loadedCount;
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
