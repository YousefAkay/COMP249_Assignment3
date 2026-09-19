# SmartTravel Manager

A Java console application for managing travel clients, trips, transportation, and accommodations. It stores records in CSV files, calculates trip costs and client spending, and generates a local HTML dashboard with charts.

## Features

* Add, edit, list, and remove clients and travel records.
* Calculate trip totals using transportation and accommodation pricing.
* Load and save clients, trips, transportation, and accommodations as CSV files.
* Filter trips by destination or cost, rank clients by spending, and sort records.
* Track recently viewed trips.
* Generate an HTML dashboard with trip and spending summaries, plus three charts.

## Run locally

**Requirements:** JDK 17 or later. Run these commands from the repository root in Bash, macOS Terminal, or WSL:

```bash
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out driver.SmartTravelDriver
```

The application opens an interactive menu. Select **8** to load the sample records from `data/`, or **10** to run a demonstration. Select **9** to save the current records to `output/data/`. After loading or creating trips, select **11** to generate the dashboard, then open `output/dashboard/dashboard.html` in a browser.

CSV rows use semicolons as separators. Invalid rows are skipped and recorded in `output/logs/errors.txt`.

## Project structure

* `src/` — Java models, service logic, CSV persistence, console menus, and dashboard generation
* `data/` — sample input CSV files
* `output/data/` — saved CSV records
* `output/dashboard/` and `output/charts/` — generated dashboard files

## Contributors

Yousef Yousef and Hamza Shaheed.
