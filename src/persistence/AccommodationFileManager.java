package persistence;

import exceptions.InvalidAccommodationDataException;
import travel.*;

import java.io.*;

// -----------------------------------------------------
// Assignment 2
// Class: Client
// Written by: Yousef Yousef (40299095) & Hamza Shadeed (4034172)
// -----------------------------------------------------

public class AccommodationFileManager {

    // Example formats:
    // HOTEL;A4001;Hilton Rome;Rome;280.00;4
    // HOSTEL;A4002;Rome Backpackers;Rome;55.00;6
    public static void saveAccommodations(Accommodation[] arr, int count, String filePath) throws IOException {
        ensureParentDir(filePath);
        PrintWriter out = new PrintWriter(new FileWriter(filePath));

        for (int i = 0; i < count; i++) {
            Accommodation a = arr[i];
            if (a == null) continue;

            if (a instanceof Hotel) {
                Hotel h = (Hotel) a;
                out.println("HOTEL;" + h.getAccommodationId() + ";" + h.getName() + ";" + h.getLocation() + ";" +
                        h.getPricePerNight() + ";" + h.getStars());
            } else if (a instanceof Hostel) {
                Hostel ho = (Hostel) a;
                out.println("HOSTEL;" + ho.getAccommodationId() + ";" + ho.getName() + ";" + ho.getLocation() + ";" +
                        ho.getPricePerNight() + ";" + ho.getSharedRoomCapacity());
            } else {
                ErrorLogger.log("ACCOM SAVE ERROR | Unknown accommodation subclass: " + a.getClass().getName());
            }
        }

        out.close();
    }

    public static int loadAccommodations(Accommodation[] arr, String filePath) throws IOException {
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
                    String[] p = line.split(";");
                    String type = p[0].trim().toUpperCase();

                    if ("HOTEL".equals(type)) {
                        if (p.length != 6) throw new InvalidAccommodationDataException("Bad HOTEL token count: " + raw);
                        String id = p[1].trim();
                        String name = p[2].trim();
                        String loc = p[3].trim();
                        double ppn = Double.parseDouble(p[4].trim());
                        int stars = Integer.parseInt(p[5].trim());
                        arr[count++] = new Hotel(id, name, loc, ppn, stars);

                    } else if ("HOSTEL".equals(type)) {
                        if (p.length != 6) throw new InvalidAccommodationDataException("Bad HOSTEL token count: " + raw);
                        String id = p[1].trim();
                        String name = p[2].trim();
                        String loc = p[3].trim();
                        double ppn = Double.parseDouble(p[4].trim());
                        int cap = Integer.parseInt(p[5].trim());
                        arr[count++] = new Hostel(id, name, loc, ppn, cap);

                    } else {
                        throw new InvalidAccommodationDataException("Unknown accommodation type prefix: " + type);
                    }
                } catch (Exception ex) {
                    ErrorLogger.log("ACCOM LOAD ERROR | " + ex.getMessage() + " | line=" + raw);
                }

                if (count >= arr.length) break;
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