package persistence;

import exceptions.EntityNotFoundException;
import exceptions.InvalidTripDataException;
import service.SmartTravelService;
import travel.Trip;

import java.io.*;

public class TripFileManager {

    // Format:
    // TripID;ClientID;AccommodationID;TransportationID;Destination;DurationDays;BasePrice
    public static void saveTrips(Trip[] arr, int count, String filePath) throws IOException {
        ensureParentDir(filePath);
        PrintWriter out = new PrintWriter(new FileWriter(filePath));

        for (int i = 0; i < count; i++) {
            Trip t = arr[i];
            if (t == null) continue;

            String accomId = (t.getAccommodationId() == null) ? "" : t.getAccommodationId();
            String transId = (t.getTransportationId() == null) ? "" : t.getTransportationId();

            out.println(t.getTripId() + ";" + t.getClientId() + ";" + accomId + ";" + transId + ";" +
                    t.getDestination() + ";" + t.getDurationInDays() + ";" + t.getBasePrice());
        }

        out.close();
    }

    public static int loadTrips(Trip[] arr, String filePath, SmartTravelService svc) throws IOException {
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
                    if (p.length != 7) throw new InvalidTripDataException("Bad TRIP token count: " + raw);

                    String tripId = p[0].trim();
                    String clientId = p[1].trim();
                    String accomId = p[2].trim();
                    String transId = p[3].trim();
                    String dest = p[4].trim();
                    int days = Integer.parseInt(p[5].trim());
                    double base = Double.parseDouble(p[6].trim());

                    // At least one of accom/trans must be present
                    if ((accomId == null || accomId.isEmpty()) && (transId == null || transId.isEmpty())) {
                        throw new InvalidTripDataException("Trip must have accommodationId or transportationId: " + raw);
                    }

                    // Client must exist
                    if (!svc.clientExists(clientId)) {
                        throw new EntityNotFoundException("Trip references missing clientId: " + clientId);
                    }

                    // Let Trip validate its own rules too
                    Trip t = new Trip(tripId, clientId,
                            accomId.isEmpty() ? null : accomId,
                            transId.isEmpty() ? null : transId,
                            dest, days, base);

                    // Strict ID existence check (recommended by spec)
                    if (t.getAccommodationId() != null) svc.findAccommodationById(t.getAccommodationId());
                    if (t.getTransportationId() != null) svc.findTransportationById(t.getTransportationId());

                    // add via service to update amountSpent
                    svc.addTrip(t);

                } catch (Exception ex) {
                    ErrorLogger.log("TRIP LOAD ERROR | " + ex.getMessage() + " | line=" + raw);
                }

                if (count >= arr.length) break;
            }

            // service already inserted into its internal trip array,
            // so we return its tripCount for consistency:
            return svc.getTripCount();

        } finally {
            if (br != null) br.close();
        }
    }

    private static void ensureParentDir(String filePath) {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }
}