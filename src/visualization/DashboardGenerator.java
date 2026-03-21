// -----------------------------------------------------
// Assignment 2
// Class: DashboardGenerator
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package visualization;

import client.Client;
import travel.Trip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Generates the dashboard HTML, CSS, and chart files for the project output. */
public class DashboardGenerator {

    /** Creates the dashboard folder structure, charts, stylesheet, and HTML page. */
    public static void generateDashboard(Client[] clients, int clientCount, Trip[] trips, int tripCount)
            throws IOException {
        ensureDir("output/dashboard");
        ensureDir("output/charts");

        TripChartGenerator.generateAllCharts(trips, tripCount);
        writeCss("output/dashboard/styles.css");
        writeHtml("output/dashboard/dashboard.html", clients, clientCount, trips, tripCount);
    }

    /** Writes a simple stylesheet used by the generated dashboard page. */
    private static void writeCss(String filePath) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        outputWriter.println("body{font-family:Arial,sans-serif;margin:24px;background:#f7f7f7;}");
        outputWriter.println("h1{margin-bottom:6px;}");
        outputWriter.println(".card{background:white;border:1px solid #ddd;padding:16px;border-radius:10px;margin:14px 0;}");
        outputWriter.println(".row{margin:8px 0;}");
        outputWriter.println(".muted{color:#666;font-size:12px;}");
        outputWriter.println("img{max-width:100%;border:1px solid #ccc;border-radius:8px;margin-top:10px;}");

        outputWriter.close();
    }

    /** Builds the dashboard page using current trip totals, client spending, and chart images. */
    private static void writeHtml(String filePath, Client[] clients, int clientCount, Trip[] trips, int tripCount)
            throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(filePath));

        double totalRevenue = 0.0;
        double highestTripCost = -1.0;
        String highestTripLabel = "N/A";

        /** Compute dashboard summary values from the trip array. */
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null) {
                double currentTripCost = trips[tripIndex].calculateTotalCost();
                totalRevenue += currentTripCost;

                if (currentTripCost > highestTripCost) {
                    highestTripCost = currentTripCost;
                    highestTripLabel =
                            trips[tripIndex].getTripId() + " (" + trips[tripIndex].getDestination() + ")";
                }
            }
        }

        double averageTripCost = (tripCount > 0) ? totalRevenue / tripCount : 0.0;

        outputWriter.println("<!DOCTYPE html><html><head><meta charset='utf-8'/>");
        outputWriter.println("<title>SmartTravel Dashboard</title>");
        outputWriter.println("<link rel='stylesheet' href='styles.css'/>");
        outputWriter.println("</head><body>");

        outputWriter.println("<h1>SmartTravel Dashboard</h1>");
        outputWriter.println("<div class='muted'>Generated automatically by the SmartTravel system</div>");

        outputWriter.println("<div class='card'>");
        outputWriter.println("<h2>Summary</h2>");
        outputWriter.println("<div class='row'>Clients: " + clientCount + "</div>");
        outputWriter.println("<div class='row'>Trips: " + tripCount + "</div>");
        outputWriter.println("<div class='row'>Total Revenue: $" + String.format("%.2f", totalRevenue) + "</div>");
        outputWriter.println("<div class='row'>Average Trip Cost: $" +
                String.format("%.2f", averageTripCost) + "</div>");
        outputWriter.println("<div class='row'>Most Expensive Trip: " + highestTripLabel + "</div>");
        outputWriter.println("</div>");

        outputWriter.println("<div class='card'>");
        outputWriter.println("<h2>Client Spending</h2>");
        for (int clientIndex = 0; clientIndex < clientCount; clientIndex++) {
            if (clients[clientIndex] == null) continue;

            outputWriter.println(
                    "<div class='row'>" + clients[clientIndex].getClientId() + " - " +
                            clients[clientIndex].getFirstName() + " " + clients[clientIndex].getLastName() +
                            " : $" + String.format("%.2f", clients[clientIndex].getAmountSpent()) + "</div>"
            );
        }
        outputWriter.println("</div>");

        outputWriter.println("<div class='card'><h2>Trip Cost Bar Chart</h2>");
        outputWriter.println("<img src='../charts/trip_cost_bar_chart.png' alt='Trip Cost Bar Chart'></div>");

        outputWriter.println("<div class='card'><h2>Trip Duration Line Chart</h2>");
        outputWriter.println("<img src='../charts/trip_duration_line_chart.png' alt='Trip Duration Line Chart'></div>");

        outputWriter.println("<div class='card'><h2>Trips Per Destination Pie Chart</h2>");
        outputWriter.println("<img src='../charts/trips_per_destination_pie.png' alt='Trips Per Destination Pie Chart'></div>");

        outputWriter.println("<div class='card'>");
        outputWriter.println("<h2>Trips Summary</h2>");
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] == null) continue;

            outputWriter.println(
                    "<div class='row'><b>" + trips[tripIndex].getTripId() + "</b> - " +
                            trips[tripIndex].getDestination() + " (" + trips[tripIndex].getDurationInDays() +
                            " days), total $" + String.format("%.2f", trips[tripIndex].calculateTotalCost()) +
                            "</div>"
            );
        }
        outputWriter.println("</div>");

        outputWriter.println("</body></html>");
        outputWriter.close();
    }

    /** Creates a directory if it does not already exist. */
    private static void ensureDir(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}