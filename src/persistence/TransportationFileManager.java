// -----------------------------------------------------
// Assignment 3
// Class: TransportationFileManager
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package persistence;

import exceptions.InvalidTransportDataException;
import travel.Bus;
import travel.Flight;
import travel.Train;
import travel.Transportation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Legacy fallback CSV helper retained for A2 compatibility; A3 primarily uses GenericFileManager. */
public class TransportationFileManager {

    /** Saves each transportation object using the subclass prefix required by the assignment. */
    public static void saveTransportations(Transportation[] transportations, int transportCount, String filePath)
            throws IOException {
        ensureParentDir(filePath);

        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        for (int transportationIndex = 0; transportationIndex < transportCount; transportationIndex++) {
            Transportation transportation = transportations[transportationIndex];
            if (transportation == null) continue;

            if (transportation instanceof Flight) {
                Flight flight = (Flight) transportation;
                outputWriter.println(
                        "FLIGHT;" + flight.getTransportId() + ";" + flight.getCompanyName() + ";" +
                                flight.getDepartureCity() + ";" + flight.getArrivalCity() + ";" +
                                flight.getAirlineName() + ";" + flight.getBaseFare() + ";" +
                                flight.getLuggageAllowanceKg()
                );

            } else if (transportation instanceof Train) {
                Train train = (Train) transportation;
                outputWriter.println(
                        "TRAIN;" + train.getTransportId() + ";" + train.getCompanyName() + ";" +
                                train.getDepartureCity() + ";" + train.getArrivalCity() + ";" +
                                train.getBaseFare() + ";" + train.getTrainType()
                );

            } else if (transportation instanceof Bus) {
                Bus bus = (Bus) transportation;
                outputWriter.println(
                        "BUS;" + bus.getTransportId() + ";" + bus.getCompanyName() + ";" +
                                bus.getDepartureCity() + ";" + bus.getArrivalCity() + ";" +
                                bus.getBaseFare() + ";" + bus.getNumberOfStops()
                );

            } else {
                ErrorLogger.log("TRANSPORT SAVE ERROR | Unknown transport subclass: " +
                        transportation.getClass().getName());
            }
        }

        outputWriter.close();
    }

    /** Loads transportation rows, builds the right subclass, and logs invalid lines. */
    public static int loadTransportations(Transportation[] transportations, String filePath) throws IOException {
        int loadedCount = 0;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (loadedCount >= transportations.length) break;

                String rawLine = line;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String[] tokens = line.split(";");
                    String typePrefix = tokens[0].trim().toUpperCase();

                    if ("FLIGHT".equals(typePrefix)) {
                        if (tokens.length != 8) {
                            throw new InvalidTransportDataException("Bad FLIGHT token count: " + rawLine);
                        }

                        String transportId = tokens[1].trim();
                        String companyName = tokens[2].trim();
                        String departureCity = tokens[3].trim();
                        String arrivalCity = tokens[4].trim();
                        String airlineName = tokens[5].trim();
                        double baseFare = Double.parseDouble(tokens[6].trim());
                        double luggageAllowanceKg = Double.parseDouble(tokens[7].trim());

                        transportations[loadedCount++] = new Flight(
                                transportId,
                                companyName,
                                departureCity,
                                arrivalCity,
                                airlineName,
                                baseFare,
                                luggageAllowanceKg
                        );

                    } else if ("TRAIN".equals(typePrefix)) {
                        if (tokens.length != 7) {
                            throw new InvalidTransportDataException("Bad TRAIN token count: " + rawLine);
                        }

                        String transportId = tokens[1].trim();
                        String companyName = tokens[2].trim();
                        String departureCity = tokens[3].trim();
                        String arrivalCity = tokens[4].trim();
                        double baseFare = Double.parseDouble(tokens[5].trim());
                        String trainType = tokens[6].trim();

                        transportations[loadedCount++] = new Train(
                                transportId,
                                companyName,
                                departureCity,
                                arrivalCity,
                                trainType,
                                baseFare
                        );

                    } else if ("BUS".equals(typePrefix)) {
                        if (tokens.length != 7) {
                            throw new InvalidTransportDataException("Bad BUS token count: " + rawLine);
                        }

                        String transportId = tokens[1].trim();
                        String companyName = tokens[2].trim();
                        String departureCity = tokens[3].trim();
                        String arrivalCity = tokens[4].trim();
                        double baseFare = Double.parseDouble(tokens[5].trim());
                        int numberOfStops = Integer.parseInt(tokens[6].trim());

                        transportations[loadedCount++] = new Bus(
                                transportId,
                                companyName,
                                departureCity,
                                arrivalCity,
                                baseFare,
                                numberOfStops
                        );

                    } else {
                        throw new InvalidTransportDataException(
                                "Unknown transport type prefix: " + typePrefix
                        );
                    }

                } catch (Exception exception) {
                    ErrorLogger.log("TRANSPORT LOAD ERROR | " + exception.getMessage() + " | line=" + rawLine);
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
