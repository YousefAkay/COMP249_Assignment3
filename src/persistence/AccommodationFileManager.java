// -----------------------------------------------------
// Assignment 2
// Class: AccommodationFileManager
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package persistence;

import exceptions.InvalidAccommodationDataException;
import travel.Accommodation;
import travel.Hotel;
import travel.Hostel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Handles CSV saving and loading for accommodation records. */
public class AccommodationFileManager {

    /** Saves each accommodation object using the subclass prefix required by the assignment. */
    public static void saveAccommodations(Accommodation[] accommodations, int accommodationCount, String filePath)
            throws IOException {
        ensureParentDir(filePath);

        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        for (int accommodationIndex = 0; accommodationIndex < accommodationCount; accommodationIndex++) {
            Accommodation accommodation = accommodations[accommodationIndex];
            if (accommodation == null) continue;

            if (accommodation instanceof Hotel) {
                Hotel hotel = (Hotel) accommodation;
                outputWriter.println(
                        "HOTEL;" + hotel.getAccommodationId() + ";" + hotel.getName() + ";" +
                                hotel.getLocation() + ";" + hotel.getPricePerNight() + ";" + hotel.getStars()
                );

            } else if (accommodation instanceof Hostel) {
                Hostel hostel = (Hostel) accommodation;
                outputWriter.println(
                        "HOSTEL;" + hostel.getAccommodationId() + ";" + hostel.getName() + ";" +
                                hostel.getLocation() + ";" + hostel.getPricePerNight() + ";" +
                                hostel.getSharedRoomCapacity()
                );

            } else {
                ErrorLogger.log("ACCOM SAVE ERROR | Unknown accommodation subclass: " +
                        accommodation.getClass().getName());
            }
        }

        outputWriter.close();
    }

    /** Loads accommodation records line by line, logging invalid rows and continuing safely. */
    public static int loadAccommodations(Accommodation[] accommodations, String filePath) throws IOException {
        int loadedCount = 0;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (loadedCount >= accommodations.length) break;

                String rawLine = line;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] tokens = line.split(";");
                    String typePrefix = tokens[0].trim().toUpperCase();

                    if ("HOTEL".equals(typePrefix)) {
                        if (tokens.length != 6) {
                            throw new InvalidAccommodationDataException("Bad HOTEL token count: " + rawLine);
                        }

                        String accommodationId = tokens[1].trim();
                        String name = tokens[2].trim();
                        String location = tokens[3].trim();
                        double pricePerNight = Double.parseDouble(tokens[4].trim());
                        int stars = Integer.parseInt(tokens[5].trim());

                        accommodations[loadedCount++] =
                                new Hotel(accommodationId, name, location, pricePerNight, stars);

                    } else if ("HOSTEL".equals(typePrefix)) {
                        if (tokens.length != 6) {
                            throw new InvalidAccommodationDataException("Bad HOSTEL token count: " + rawLine);
                        }

                        String accommodationId = tokens[1].trim();
                        String name = tokens[2].trim();
                        String location = tokens[3].trim();
                        double pricePerNight = Double.parseDouble(tokens[4].trim());
                        int sharedRoomCapacity = Integer.parseInt(tokens[5].trim());

                        accommodations[loadedCount++] =
                                new Hostel(accommodationId, name, location, pricePerNight, sharedRoomCapacity);

                    } else {
                        throw new InvalidAccommodationDataException(
                                "Unknown accommodation type prefix: " + typePrefix
                        );
                    }

                } catch (Exception exception) {
                    ErrorLogger.log("ACCOM LOAD ERROR | " + exception.getMessage() + " | line=" + rawLine);
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