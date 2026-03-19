package persistence;

import client.Client;
import exceptions.InvalidClientDataException;

import java.io.*;

public class ClientFileManager {

    // Format: ClientID;firstName;lastName;email
    public static void saveClients(Client[] clients, int clientCount, String filePath) throws IOException {
        ensureParentDir(filePath);

        PrintWriter out = new PrintWriter(new FileWriter(filePath));
        for (int i = 0; i < clientCount; i++) {
            Client c = clients[i];
            if (c == null) continue;
            out.println(c.getClientId() + ";" + c.getFirstName() + ";" + c.getLastName() + ";" + c.getEmail());
        }
        out.close();
    }

    public static int loadClients(Client[] clients, String filePath) throws IOException {
        int count = 0;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                String raw = line;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] parts = line.split(";");
                    if (parts.length != 4) {
                        throw new InvalidClientDataException("Bad client CSV token count: " + raw);
                    }

                    String id = parts[0].trim();
                    String fn = parts[1].trim();
                    String ln = parts[2].trim();
                    String em = parts[3].trim();

                    for (int i = 0; i < count; i++) {
                        if (clients[i] != null && clients[i].getEmail().equalsIgnoreCase(em)) {
                            throw new InvalidClientDataException("Duplicate email in clients.csv: " + em);
                        }
                    }

                    Client c = new Client(id, fn, ln, em, 0.0);
                    clients[count++] = c;

                } catch (Exception ex) {
                    ErrorLogger.log("CLIENT LOAD ERROR | " + ex.getMessage() + " | line=" + raw);
                }

                if (count >= clients.length) break;
            }

        } finally {
            if (br != null) br.close();
        }

        return count;
    }

    private static void ensureParentDir(String filePath) {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }
}