package persistence;

import exceptions.InvalidTransportDataException;
import travel.*;

import java.io.*;

public class TransportationFileManager {

    // Supported formats:
    // FLIGHT;TR3001;Alitalia;JFK;FCO;850.00;23.0
    // TRAIN;TR3002;Shinkansen;Tokyo;Kyoto;250.00;HighSpeed
    // BUS;TR3003;Greyhound;NYC;Boston;75.00;3
    public static void saveTransportations(Transportation[] arr, int count, String filePath) throws IOException {
        ensureParentDir(filePath);

        PrintWriter out = new PrintWriter(new FileWriter(filePath));

        for (int i = 0; i < count; i++) {
            Transportation t = arr[i];
            if (t == null) continue;

            if (t instanceof Flight) {
                Flight f = (Flight) t;
                out.println("FLIGHT;" + f.getTransportId() + ";" + f.getCompanyName() + ";" +
                        f.getDepartureCity() + ";" + f.getArrivalCity() + ";" +
                        f.getBaseFare() + ";" + f.getLuggageAllowanceKg());

            } else if (t instanceof Train) {
                Train tr = (Train) t;
                out.println("TRAIN;" + tr.getTransportId() + ";" + tr.getCompanyName() + ";" +
                        tr.getDepartureCity() + ";" + tr.getArrivalCity() + ";" +
                        tr.getBaseFare() + ";" + tr.getTrainType());

            } else if (t instanceof Bus) {
                Bus b = (Bus) t;
                out.println("BUS;" + b.getTransportId() + ";" + b.getCompanyName() + ";" +
                        b.getDepartureCity() + ";" + b.getArrivalCity() + ";" +
                        b.getBaseFare() + ";" + b.getNumberOfStops());

            } else {
                ErrorLogger.log("TRANSPORT SAVE ERROR | Unknown transport subclass: " + t.getClass().getName());
            }
        }

        out.close();
    }

    public static int loadTransportations(Transportation[] arr, String filePath) throws IOException {
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

                    if ("FLIGHT".equals(type)) {
                        if (p.length != 8) {
                            throw new InvalidTransportDataException("Bad FLIGHT token count: " + raw);
                        }

                        String id = p[1].trim();
                        String company = p[2].trim();
                        String dep = p[3].trim();
                        String arrCity = p[4].trim();
                        double baseFare = Double.parseDouble(p[5].trim());
                        double luggage = Double.parseDouble(p[6].trim());

                        // NOTE: if your Flight constructor still expects airlineName too,
                        // then keep your existing flight model format instead.
                        // If your model is airline-based, do NOT use this branch blindly.
                        throw new InvalidTransportDataException(
                                "Flight CSV/model mismatch: check your Flight.java against assignment format."
                        );

                    } else if ("TRAIN".equals(type)) {
                        if (p.length != 7) {
                            throw new InvalidTransportDataException("Bad TRAIN token count: " + raw);
                        }

                        String id = p[1].trim();
                        String company = p[2].trim();
                        String dep = p[3].trim();
                        String arrCity = p[4].trim();
                        double baseFare = Double.parseDouble(p[5].trim());
                        String trainType = p[6].trim();

                        arr[count++] = new Train(id, company, dep, arrCity, trainType, baseFare);

                    } else if ("BUS".equals(type)) {
                        if (p.length != 7) {
                            throw new InvalidTransportDataException("Bad BUS token count: " + raw);
                        }

                        String id = p[1].trim();
                        String company = p[2].trim();
                        String dep = p[3].trim();
                        String arrCity = p[4].trim();
                        double baseFare = Double.parseDouble(p[5].trim());
                        int stops = Integer.parseInt(p[6].trim());

                        arr[count++] = new Bus(id, company, dep, arrCity, baseFare, stops);

                    } else {
                        throw new InvalidTransportDataException("Unknown transport type prefix: " + type);
                    }

                } catch (Exception ex) {
                    ErrorLogger.log("TRANSPORT LOAD ERROR | " + ex.getMessage() + " | line=" + raw);
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