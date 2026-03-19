package visualization;

import client.Client;
import travel.Trip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DashboardGenerator {

    public static void generateDashboard(Client[] clients, int clientCount, Trip[] trips, int tripCount) throws IOException {
        ensureDir("output/dashboard");
        ensureDir("output/charts");

        TripChartGenerator.generateAllCharts(trips, tripCount);
        writeCss("output/dashboard/styles.css");
        writeHtml("output/dashboard/dashboard.html", clients, clientCount, trips, tripCount);
    }

    private static void writeCss(String path) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(path));
        out.println("body{font-family:Arial,sans-serif;margin:24px;background:#f7f7f7;}");
        out.println("h1{margin-bottom:6px;}");
        out.println(".card{background:white;border:1px solid #ddd;padding:16px;border-radius:10px;margin:14px 0;}");
        out.println(".row{margin:8px 0;}");
        out.println(".muted{color:#666;font-size:12px;}");
        out.println("img{max-width:100%;border:1px solid #ccc;border-radius:8px;margin-top:10px;}");
        out.close();
    }

    private static void writeHtml(String path, Client[] clients, int clientCount, Trip[] trips, int tripCount) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(path));

        double totalRevenue = 0.0;
        double maxTripCost = -1.0;
        String maxTripLabel = "N/A";

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null) {
                double cost = trips[i].calculateTotalCost();
                totalRevenue += cost;
                if (cost > maxTripCost) {
                    maxTripCost = cost;
                    maxTripLabel = trips[i].getTripId() + " (" + trips[i].getDestination() + ")";
                }
            }
        }

        double averageTripCost = (tripCount > 0) ? totalRevenue / tripCount : 0.0;

        out.println("<!DOCTYPE html><html><head><meta charset='utf-8'/>");
        out.println("<title>SmartTravel Dashboard</title>");
        out.println("<link rel='stylesheet' href='styles.css'/>");
        out.println("</head><body>");

        out.println("<h1>SmartTravel Dashboard</h1>");
        out.println("<div class='muted'>Generated automatically by the SmartTravel system</div>");

        out.println("<div class='card'>");
        out.println("<h2>Summary</h2>");
        out.println("<div class='row'>Clients: " + clientCount + "</div>");
        out.println("<div class='row'>Trips: " + tripCount + "</div>");
        out.println("<div class='row'>Total Revenue: $" + String.format("%.2f", totalRevenue) + "</div>");
        out.println("<div class='row'>Average Trip Cost: $" + String.format("%.2f", averageTripCost) + "</div>");
        out.println("<div class='row'>Most Expensive Trip: " + maxTripLabel + "</div>");
        out.println("</div>");

        out.println("<div class='card'>");
        out.println("<h2>Client Spending</h2>");
        for (int i = 0; i < clientCount; i++) {
            if (clients[i] == null) continue;
            out.println("<div class='row'>" + clients[i].getClientId() + " - " +
                    clients[i].getFirstName() + " " + clients[i].getLastName() +
                    " : $" + String.format("%.2f", clients[i].getAmountSpent()) + "</div>");
        }
        out.println("</div>");

        out.println("<div class='card'><h2>Trip Cost Bar Chart</h2>");
        out.println("<img src='../charts/trip_cost_bar_chart.png' alt='Trip Cost Bar Chart'></div>");

        out.println("<div class='card'><h2>Trip Duration Line Chart</h2>");
        out.println("<img src='../charts/trip_duration_line_chart.png' alt='Trip Duration Line Chart'></div>");

        out.println("<div class='card'><h2>Trips Per Destination Pie Chart</h2>");
        out.println("<img src='../charts/trips_per_destination_pie.png' alt='Trips Per Destination Pie Chart'></div>");

        out.println("<div class='card'>");
        out.println("<h2>Trips Summary</h2>");
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null) continue;
            out.println("<div class='row'><b>" + trips[i].getTripId() + "</b> - " +
                    trips[i].getDestination() + " (" + trips[i].getDurationInDays() +
                    " days), total $" + String.format("%.2f", trips[i].calculateTotalCost()) + "</div>");
        }
        out.println("</div>");

        out.println("</body></html>");
        out.close();
    }

    private static void ensureDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) f.mkdirs();
    }
}