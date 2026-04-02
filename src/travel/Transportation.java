// -----------------------------------------------------
// Assignment 2
// Class: Transportation
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package travel;

import contracts.CsvPersistable;
import contracts.Identifiable;
import exceptions.InvalidTransportDataException;

/** Core Transportation entity in the SmartTravel system */
public abstract class Transportation implements Identifiable, CsvPersistable, Comparable<Transportation> {

	private static int nextId = 3001;

	private String transportId;
	private String companyName;
	private String departureCity;
	private String arrivalCity;
	private double baseFare;

	public static void resetIdCounter() {
		nextId = 3001;
	}

	/** Generates the next sequential ID for transportation objects. */
	private static String generateId() {
		return "TR" + nextId++;
	}

	/** Updates the next ID after loading records so new IDs do not collide. */
	public static void syncNextIdFromLoadedId(String loadedId) {
		if (loadedId == null) return;
		if (!loadedId.startsWith("TR")) return;

		try {
			int loadedNumber = Integer.parseInt(loadedId.substring(2));
			if (loadedNumber >= nextId) {
				nextId = loadedNumber + 1;
			}
		} catch (NumberFormatException ignore) {
		}
	}

	/** Builds a default transportation object with placeholder values. */
	public Transportation() {
		this.transportId = generateId();
		this.companyName = "Unknown";
		this.departureCity = "Unknown";
		this.arrivalCity = "Unknown";
		this.baseFare = 0.0;
	}

	/** Builds a transportation object from validated user input. */
	public Transportation(String companyName, String departureCity, String arrivalCity)
			throws InvalidTransportDataException {
		this.transportId = generateId();
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		this.baseFare = 0.0;
	}

	/** Builds a transportation object with an explicit base fare. */
	public Transportation(String companyName, String departureCity, String arrivalCity, double baseFare)
			throws InvalidTransportDataException {
		this.transportId = generateId();
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		setBaseFare(baseFare);
	}

	/** Builds a transportation object from file data using an explicit ID. */
	protected Transportation(String transportId, String companyName, String departureCity,
							 String arrivalCity, double baseFare) throws InvalidTransportDataException {
		setTransportIdForLoad(transportId);
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		setBaseFare(baseFare);
		syncNextIdFromLoadedId(transportId);
	}

	/** Creates a deep-style copy with a fresh generated ID. */
	public Transportation(Transportation otherTransportation) {
		this.transportId = generateId();
		this.companyName = otherTransportation.companyName;
		this.departureCity = otherTransportation.departureCity;
		this.arrivalCity = otherTransportation.arrivalCity;
		this.baseFare = otherTransportation.baseFare;
	}

	/** Treats null or whitespace-only text as blank input. */
	private static boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	/** Validates a loaded ID before storing it on the object. */
	private void setTransportIdForLoad(String transportId) throws InvalidTransportDataException {
		if (isBlank(transportId) || !transportId.startsWith("TR")) {
			throw new InvalidTransportDataException("Invalid transportId: " + transportId);
		}
		this.transportId = transportId.trim();
	}

	public String getTransportId() {
		return transportId;
	}

	@Override
	public String getId() {
		return getTransportId();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) throws InvalidTransportDataException {
		if (isBlank(companyName)) {
			throw new InvalidTransportDataException("companyName cannot be empty.");
		}
		this.companyName = companyName.trim();
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) throws InvalidTransportDataException {
		if (isBlank(departureCity)) {
			throw new InvalidTransportDataException("departureCity cannot be empty.");
		}
		this.departureCity = departureCity.trim();
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) throws InvalidTransportDataException {
		if (isBlank(arrivalCity)) {
			throw new InvalidTransportDataException("arrivalCity cannot be empty.");
		}
		this.arrivalCity = arrivalCity.trim();
	}

	public double getBaseFare() {
		return baseFare;
	}

	/** Enforces the common rule that transport fare cannot be negative. */
	public void setBaseFare(double baseFare) throws InvalidTransportDataException {
		if (baseFare < 0) {
			throw new InvalidTransportDataException("baseFare cannot be negative.");
		}
		this.baseFare = baseFare;
	}

	/** Lets each subclass define its own transport pricing rule. */
	public abstract double calculateCost(int numberOfDays);

	/** Compares the meaningful state of two transportation objects. */
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null) return false;
		if (this.getClass() != otherObject.getClass()) return false;

		Transportation otherTransportation = (Transportation) otherObject;

		if (companyName == null) {
			if (otherTransportation.companyName != null) return false;
		} else if (!companyName.equals(otherTransportation.companyName)) {
			return false;
		}

		if (departureCity == null) {
			if (otherTransportation.departureCity != null) return false;
		} else if (!departureCity.equals(otherTransportation.departureCity)) {
			return false;
		}

		if (arrivalCity == null) {
			if (otherTransportation.arrivalCity != null) return false;
		} else if (!arrivalCity.equals(otherTransportation.arrivalCity)) {
			return false;
		}

		return Double.compare(baseFare, otherTransportation.baseFare) == 0;
	}

	/** Returns a clean text summary for menus, testing, and logging. */
	@Override
	public String toString() {
		return "Transportation{transportId='" + transportId + "', companyName='" + companyName +
				"', departureCity='" + departureCity + "', arrivalCity='" + arrivalCity +
				"', baseFare=" + baseFare + "}";
	}

	public abstract String getType();

	/** Reconstructs one transportation object by dispatching on the CSV type prefix. */
	public static Transportation fromCsvRow(String csvRow) throws InvalidTransportDataException {
		if (csvRow == null) {
			throw new InvalidTransportDataException("Transportation CSV row cannot be null.");
		}

		String[] tokens = csvRow.split(";");
		if (tokens.length < 2) {
			throw new InvalidTransportDataException("Bad transportation CSV row: " + csvRow);
		}

		String typePrefix = tokens[0].trim().toUpperCase();

		if ("FLIGHT".equals(typePrefix)) {
			if (tokens.length != 8) {
				throw new InvalidTransportDataException("Bad FLIGHT token count: " + csvRow);
			}

			return new Flight(
					tokens[1].trim(),
					tokens[2].trim(),
					tokens[3].trim(),
					tokens[4].trim(),
					tokens[5].trim(),
					Double.parseDouble(tokens[6].trim()),
					Double.parseDouble(tokens[7].trim())
			);
		}

		if ("TRAIN".equals(typePrefix)) {
			if (tokens.length != 7) {
				throw new InvalidTransportDataException("Bad TRAIN token count: " + csvRow);
			}

			String possibleNumericToken = tokens[5].trim();
			String possibleTrainTypeToken = tokens[6].trim();

			try {
				return new Train(
						tokens[1].trim(),
						tokens[2].trim(),
						tokens[3].trim(),
						tokens[4].trim(),
						possibleTrainTypeToken,
						Double.parseDouble(possibleNumericToken)
				);
			} catch (NumberFormatException exception) {
				return new Train(
						tokens[1].trim(),
						tokens[2].trim(),
						tokens[3].trim(),
						tokens[4].trim(),
						possibleNumericToken,
						Double.parseDouble(possibleTrainTypeToken)
				);
			}
		}

		if ("BUS".equals(typePrefix)) {
			if (tokens.length != 7) {
				throw new InvalidTransportDataException("Bad BUS token count: " + csvRow);
			}

			return new Bus(
					tokens[1].trim(),
					tokens[2].trim(),
					tokens[3].trim(),
					tokens[4].trim(),
					Double.parseDouble(tokens[5].trim()),
					Integer.parseInt(tokens[6].trim())
			);
		}

		throw new InvalidTransportDataException("Unknown transport type prefix: " + typePrefix);
	}

	/** Applies the A3 natural business ordering: baseFare descending. */
	@Override
	public int compareTo(Transportation otherTransportation) {
		if (otherTransportation == null) {
			return -1;
		}

		int fareComparison = Double.compare(otherTransportation.baseFare, baseFare);
		if (fareComparison != 0) {
			return fareComparison;
		}

		return transportId.compareTo(otherTransportation.transportId);
	}
}
