// -----------------------------------------------------
// SmartTravel Manager
// Class: TripChartGenerator
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package visualization;

import travel.Trip;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Generates simple chart images from the trip array without external libraries. */
public class TripChartGenerator {

    /** Creates all required chart images inside the output/charts directory. */
    public static void generateAllCharts(Trip[] trips, int tripCount) throws IOException {
        Trip[] safeTrips = (trips == null) ? new Trip[0] : trips;
        int safeTripCount = Math.min(Math.max(tripCount, 0), safeTrips.length);
        File outputDirectory = new File("output/charts");
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        generateTripCostBarChart(safeTrips, safeTripCount, "output/charts/trip_cost_bar_chart.png");
        generateTripDurationLineChart(safeTrips, safeTripCount, "output/charts/trip_duration_line_chart.png");
        generateTripsPerDestinationPieChart(safeTrips, safeTripCount, "output/charts/trips_per_destination_pie.png");
    }

    /** Draws a bar chart where each trip bar is scaled against the highest trip cost. */
    private static void generateTripCostBarChart(Trip[] trips, int tripCount, String filePath)
            throws IOException {
        BufferedImage image = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 800, 500);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Trip Cost Bar Chart", 330, 30);

        double maxTripCost = 1;
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] != null) {
                maxTripCost = Math.max(maxTripCost, trips[tripIndex].calculateTotalCost());
            }
        }

        int xPosition = 60;
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] == null) continue;

            int barHeight = (int) ((trips[tripIndex].calculateTotalCost() / maxTripCost) * 300);

            graphics.setColor(new Color(70, 130, 180));
            graphics.fillRect(xPosition, 400 - barHeight, 60, barHeight);

            graphics.setColor(Color.BLACK);
            graphics.drawRect(xPosition, 400 - barHeight, 60, barHeight);
            graphics.drawString(trips[tripIndex].getTripId(), xPosition, 420);

            xPosition += 100;
        }

        graphics.dispose();
        ImageIO.write(image, "png", new File(filePath));
    }

    /** Draws a line chart using trip duration values in the order trips appear in the array. */
    private static void generateTripDurationLineChart(Trip[] trips, int tripCount, String filePath)
            throws IOException {
        BufferedImage image = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 800, 500);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Trip Duration Line Chart", 320, 30);

        graphics.drawLine(60, 420, 740, 420);
        graphics.drawLine(60, 60, 60, 420);

        int previousX = -1;
        int previousY = -1;
        int xPosition = 100;

        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] == null) continue;

            int yPosition = 420 - (trips[tripIndex].getDurationInDays() * 15);

            graphics.setColor(Color.RED);
            graphics.fillOval(xPosition - 4, yPosition - 4, 8, 8);

            if (previousX != -1) {
                graphics.drawLine(previousX, previousY, xPosition, yPosition);
            }

            graphics.setColor(Color.BLACK);
            graphics.drawString(trips[tripIndex].getTripId(), xPosition - 10, 440);

            previousX = xPosition;
            previousY = yPosition;
            xPosition += 120;
        }

        graphics.dispose();
        ImageIO.write(image, "png", new File(filePath));
    }

    /** Draws a pie chart by grouping trips with the same destination name. */
    private static void generateTripsPerDestinationPieChart(Trip[] trips, int tripCount, String filePath)
            throws IOException {
        BufferedImage image = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 800, 500);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Trips Per Destination Pie Chart", 300, 30);

        String[] destinations = new String[tripCount];
        int[] destinationCounts = new int[tripCount];
        int uniqueDestinationCount = 0;

        /** Count how many trips belong to each destination without using collections. */
        for (int tripIndex = 0; tripIndex < tripCount; tripIndex++) {
            if (trips[tripIndex] == null) continue;

            String destination = trips[tripIndex].getDestination();
            int existingPosition = -1;

            for (int destinationIndex = 0; destinationIndex < uniqueDestinationCount; destinationIndex++) {
                if (destinations[destinationIndex].equalsIgnoreCase(destination)) {
                    existingPosition = destinationIndex;
                    break;
                }
            }

            if (existingPosition == -1) {
                destinations[uniqueDestinationCount] = destination;
                destinationCounts[uniqueDestinationCount] = 1;
                uniqueDestinationCount++;
            } else {
                destinationCounts[existingPosition]++;
            }
        }

        Color[] sliceColors = {
                Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN
        };

        int totalTripsCounted = 0;
        for (int destinationIndex = 0; destinationIndex < uniqueDestinationCount; destinationIndex++) {
            totalTripsCounted += destinationCounts[destinationIndex];
        }

        int startAngle = 0;
        for (int destinationIndex = 0; destinationIndex < uniqueDestinationCount; destinationIndex++) {
            int sliceAngle = (int) Math.round((destinationCounts[destinationIndex] * 360.0) / totalTripsCounted);

            graphics.setColor(sliceColors[destinationIndex % sliceColors.length]);
            graphics.fillArc(250, 100, 250, 250, startAngle, sliceAngle);

            graphics.setColor(Color.BLACK);
            graphics.drawString(
                    destinations[destinationIndex] + " (" + destinationCounts[destinationIndex] + ")",
                    550,
                    120 + destinationIndex * 25
            );

            startAngle += sliceAngle;
        }

        graphics.dispose();
        ImageIO.write(image, "png", new File(filePath));
    }
}
