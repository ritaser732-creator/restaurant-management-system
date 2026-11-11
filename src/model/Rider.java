package model;

import java.util.ArrayList;

import utils.StringValidationUtils;

public class Rider {
	private final String id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String vehicle;

	public static final String[] VALID_VEHICLES = { "car", "motorcycle", "bicycle" };
	private boolean isAvailable;
	private ArrayList<Order> assignedOrders;
	private static int count = 0;

	public Rider(String firstName, String lastName, String phoneNumber, String vehicle) {
		this.id = generateRiderId();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.vehicle = vehicle;
		this.isAvailable = true;
		this.assignedOrders = new ArrayList<>();
	}

	private String generateRiderId() {
		count++;
		return String.format("Ri%04d", count);
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getVehicle() {
		return vehicle;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public ArrayList<Order> getAssignedOrders() {
		return assignedOrders;
	}

	 public void setFirstName(String firstName) throws ValidationException {
	        if (!StringValidationUtils.isStringOnlyAlphabetic(firstName)) {
	            throw new ValidationException("First name must contain only alphabetic characters.");
	        }
	        this.firstName = firstName;
	    }

	 public void setLastName(String lastName) throws ValidationException {
	        if (!StringValidationUtils.isStringOnlyAlphabetic(lastName)) {
	            throw new ValidationException("Last name must contain only alphabetic characters.");
	        }
	        this.lastName = lastName;
	    }

	 public void setPhoneNumber(String phoneNumber) throws ValidationException {
	        if (!StringValidationUtils.isValidPhoneNumber(phoneNumber)) {
	            throw new ValidationException("Invalid phone number format.");
	        }
	        this.phoneNumber = phoneNumber;
	    }

	public void setVehicle(String vehicle) throws ValidationException {
        if (!StringValidationUtils.isStringOnlyAlphabetic(vehicle)) {
            throw new ValidationException("Vehicle must contain only alphabetic characters.");
        }
        String normalizedVehicle = vehicle.toLowerCase();
        boolean isValid = false;
        for (String v : VALID_VEHICLES) {
            if (v.equals(normalizedVehicle)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new ValidationException("Invalid vehicle type. Valid types are: car, motorcycle, bicycle.");
        }
        this.vehicle = normalizedVehicle;
    }

	public void addAssignedOrder(Order order) throws ValidationException {
        if (order == null) {
            throw new ValidationException("Order cannot be null.");
        }
        for (Order assignedOrder : assignedOrders) {
            if (assignedOrder.getCode().equals(order.getCode())) {
                throw new ValidationException("Order is already assigned to this rider.");
            }
        }
        assignedOrders.add(order);
        isAvailable = false;
    }

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "Rider [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", vehicle=" + vehicle + ", isAvailable=" + isAvailable + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Rider other = (Rider) obj;
		return id.equals(other.id);
	}
}
