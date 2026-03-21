// -----------------------------------------------------
// Assignment 2
// Class: TripFileManager
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package persistence;

import exceptions.EntityNotFoundException;
import exceptions.InvalidTripDataException;
import service.SmartTravelService;
import travel.Trip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Handles CSV saving and loading for trip records. */
public class TripFileManager {

    /** Saves each trip while keeping optional accommodation and transportation IDs blank when absent. */
    public static void saveTrips(Trip[] trips, int tripCount, String filePath) throws IOException {
        ensureParentDir(filePath);

        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            Trip trip = trips[tripIndex];
            if (trip == null) continue;

            String accommodationId = (trip.getAccommodationId() == null) ? "" : trip.getAccommodationId();
            String transportationId = (trip.getTransportationId() == null) ? "" : trip.getTransportationId();

            outputWriter.println(
                    trip.getTripId() + ";" + trip.getClientId() + ";" + accommodationId + ";" +
                            transportationId + ";" + trip.getDestination() + ";" +
                            trip.getDurationInDays() + ";" + trip.getBasePrice()
            );
        }

        outputWriter.close();
    }

    /** Loads trips only after checking that referenced clients and bookings already exist. */
    public static int loadTrips(Trip[] trips, String filePath, SmartTravelService smartTravelService)
            throws IOException {
        int loadedCount = 0;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (loadedCount >= trips.length) break;

                String rawLine = line;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] tokens = line.split(";");
                    if (tokens.length != 7) {
                        throw new InvalidTripDataException("Bad TRIP token count: " + rawLine);
                    }

                    String tripId = tokens[0].trim();
                    String clientId = tokens[1].trim();
                    String accommodationId = tokens[2].trim();
                    String transportationId = tokens[3].trim();
                    String destination = tokens[4].trim();
                    int durationInDays = Integer.parseInt(tokens[5].trim());
                    double basePrice = Double.parseDouble(tokens[6].trim());

                    if (accommodationId.isEmpty()) accommodationId = null;
                    if (transportationId.isEmpty()) transportationId = null;

                    /** Enforce the A2 rule that a trip needs at least one booking component. */
                    if (accommodationId == null && transportationId == null) {
                        throw new InvalidTripDataException(
                                "Trip must have accommodationId or transportationId: " + rawLine
                        );
                    }

                    /** Verify that all referenced IDs are already present in memory. */
                    if (!smartTravelService.clientExists(clientId)) {
                        throw new EntityNotFoundException("Trip references missing clientId: " + clientId);
                    }

                    if (accommodationId != null) {
                        smartTravelService.findAccommodationById(accommodationId);
                    }
                    if (transportationId != null) {
                        smartTravelService.findTransportationById(transportationId);
                    }

                    Trip trip = new Trip(
                            tripId,
                            clientId,
                            accommodationId,
                            transportationId,
                            destination,
                            durationInDays,
                            basePrice
                    );

                    smartTravelService.addTrip(trip);
                    loadedCount++;

                } catch (Exception exception) {
                    ErrorLogger.log("TRIP LOAD ERROR | " + exception.getMessage() + " | line=" + rawLine);
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