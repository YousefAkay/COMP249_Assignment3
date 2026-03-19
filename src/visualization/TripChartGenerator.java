package visualization;

import travel.Trip;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TripChartGenerator {

    public static void generateAllCharts(Trip[] trips, int tripCount) throws IOException {
        File dir = new File("output/charts");
        if (!dir.exists()) dir.mkdirs();

        generateTripCostBarChart(trips, tripCount, "output/charts/trip_cost_bar_chart.png");
        generateTripDurationLineChart(trips, tripCount, "output/charts/trip_duration_line_chart.png");
        generateTripsPerDestinationPieChart(trips, tripCount, "output/charts/trips_per_destination_pie.png");
    }

    private static void generateTripCostBarChart(Trip[] trips, int tripCount, String path) throws IOException {
        BufferedImage img = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 500);
        g.setColor(Color.BLACK);
        g.drawString("Trip Cost Bar Chart", 330, 30);

        double max = 1;
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] != null) {
                max = Math.max(max, trips[i].calculateTotalCost());
            }
        }

        int x = 60;
        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null) continue;
            int height = (int) ((trips[i].calculateTotalCost() / max) * 300);
            g.setColor(new Color(70, 130, 180));
            g.fillRect(x, 400 - height, 60, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, 400 - height, 60, height);
            g.drawString(trips[i].getTripId(), x, 420);
            x += 100;
        }

        g.dispose();
        ImageIO.write(img, "png", new File(path));
    }

    private static void generateTripDurationLineChart(Trip[] trips, int tripCount, String path) throws IOException {
        BufferedImage img = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 500);
        g.setColor(Color.BLACK);
        g.drawString("Trip Duration Line Chart", 320, 30);

        g.drawLine(60, 420, 740, 420);
        g.drawLine(60, 60, 60, 420);

        int prevX = -1;
        int prevY = -1;
        int x = 100;

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null) continue;
            int y = 420 - (trips[i].getDurationInDays() * 15);
            g.setColor(Color.RED);
            g.fillOval(x - 4, y - 4, 8, 8);
            if (prevX != -1) {
                g.drawLine(prevX, prevY, x, y);
            }
            g.setColor(Color.BLACK);
            g.drawString(trips[i].getTripId(), x - 10, 440);
            prevX = x;
            prevY = y;
            x += 120;
        }

        g.dispose();
        ImageIO.write(img, "png", new File(path));
    }

    private static void generateTripsPerDestinationPieChart(Trip[] trips, int tripCount, String path) throws IOException {
        BufferedImage img = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 500);
        g.setColor(Color.BLACK);
        g.drawString("Trips Per Destination Pie Chart", 300, 30);

        String[] destinations = new String[tripCount];
        int[] counts = new int[tripCount];
        int unique = 0;

        for (int i = 0; i < tripCount; i++) {
            if (trips[i] == null) continue;
            String dest = trips[i].getDestination();
            int pos = -1;
            for (int j = 0; j < unique; j++) {
                if (destinations[j].equalsIgnoreCase(dest)) {
                    pos = j;
                    break;
                }
            }
            if (pos == -1) {
                destinations[unique] = dest;
                counts[unique] = 1;
                unique++;
            } else {
                counts[pos]++;
            }
        }

        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN};
        int total = 0;
        for (int i = 0; i < unique; i++) total += counts[i];

        int startAngle = 0;
        for (int i = 0; i < unique; i++) {
            int angle = (int) Math.round((counts[i] * 360.0) / total);
            g.setColor(colors[i % colors.length]);
            g.fillArc(250, 100, 250, 250, startAngle, angle);
            g.setColor(Color.BLACK);
            g.drawString(destinations[i] + " (" + counts[i] + ")", 550, 120 + i * 25);
            startAngle += angle;
        }

        g.dispose();
        ImageIO.write(img, "png", new File(path));
    }
}