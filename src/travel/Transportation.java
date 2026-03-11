// -----------------------------------------------------
// Assignment 2
// Class: Transportation
// Written by: Yousef Yousef (40299095) & Hamza Shadeed (40
// -----------------------------------------------------

package travel;

import exceptions.InvalidTransportDataException;

/** Core Transportation entity in the SmartTravel system */
public abstract class Transportation {

	private static int nextId = 3001;

	private String transportId;
	private String companyName;
	private String departureCity;
	private String arrivalCity;

	// A2 addition: base fare to support CSV pricing
	private double baseFare;

	/** Generates the next sequential ID for this category */
	private static String generateId() {
		return "TR" + nextId++;
	}

	/** Sync nextId so IDs don't collide after CSV load */
	public static void syncNextIdFromLoadedId(String loadedId) {
		if (loadedId == null) return;
		if (!loadedId.startsWith("TR")) return;
		try {
			int n = Integer.parseInt(loadedId.substring(2));
			if (n >= nextId) nextId = n + 1;
		} catch (NumberFormatException ignore) {
		}
	}

	/** Default constructor */
	public Transportation() {
		this.transportId = generateId();
		this.companyName = "Unknown";
		this.departureCity = "Unknown";
		this.arrivalCity = "Unknown";
		this.baseFare = 0.0;
	}

	/** constructor */
	public Transportation(String companyName, String departureCity, String arrivalCity) throws InvalidTransportDataException {
		this.transportId = generateId();
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		this.baseFare = 0.0;
	}

	/** constructor: explicit baseFare */
	public Transportation(String companyName, String departureCity, String arrivalCity, double baseFare)
			throws InvalidTransportDataException {
		this.transportId = generateId();
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		setBaseFare(baseFare);
	}

	/** load-time constructor: explicit ID + baseFare */
	protected Transportation(String transportId, String companyName, String departureCity, String arrivalCity, double baseFare)
			throws InvalidTransportDataException {
		setTransportIdForLoad(transportId);
		setCompanyName(companyName);
		setDepartureCity(departureCity);
		setArrivalCity(arrivalCity);
		setBaseFare(baseFare);
		syncNextIdFromLoadedId(transportId);
	}

	/** Copy constructor: new ID generated */
	public Transportation(Transportation other) {
		this.transportId = generateId();
		this.companyName = other.companyName;
		this.departureCity = other.departureCity;
		this.arrivalCity = other.arrivalCity;
		this.baseFare = other.baseFare;
	}

	/** Validation helpers */
	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	private void setTransportIdForLoad(String transportId) throws InvalidTransportDataException {
		if (isBlank(transportId) || !transportId.startsWith("TR")) {
			throw new InvalidTransportDataException("Invalid transportId: " + transportId);
		}
		this.transportId = transportId.trim();
	}

	/** Getters and Setters */
	public String getTransportId() {
		return transportId;
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

	public void setBaseFare(double baseFare) throws InvalidTransportDataException {
		if (baseFare < 0) {
			throw new InvalidTransportDataException("baseFare cannot be negative.");
		}
		this.baseFare = baseFare;
	}

	/** subclasses decide how cost is computed */
	public abstract double calculateCost(int numberOfDays);

	/** Logical equality (meaningful attributes, not auto-ID) */
	public boolean equals(Object oth) {
		if (oth == null) return false;
		if (this.getClass() != oth.getClass()) return false;

		Transportation other = (Transportation) oth;

		if (companyName == null) {
			if (other.companyName != null) return false;
		} else if (!companyName.equals(other.companyName)) return false;

		if (departureCity == null) {
			if (other.departureCity != null) return false;
		} else if (!departureCity.equals(other.departureCity)) return false;

		if (arrivalCity == null) {
			if (other.arrivalCity != null) return false;
		} else if (!arrivalCity.equals(other.arrivalCity)) return false;

		return Double.compare(baseFare, other.baseFare) == 0;
	}

	/** Provides a clean summary for display */
	public String toString() {
		return "Transportation{transportId='" + transportId + "', companyName='" + companyName +
				"', departureCity='" + departureCity + "', arrivalCity='" + arrivalCity + "', baseFare=" + baseFare + "}";
	}
}