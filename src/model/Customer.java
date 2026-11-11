package model;

import utils.StringValidationUtils;

public class Customer implements Comparable<Customer>  {
	private final String code;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private double balance;

	private String street;
	private String city;
	private String zipCode;

	private static int count = 0; // to count the number of customers created

	public Customer(String firstName, String lastName, String email, String phoneNumber, double balance, String street,
			String city, String zipCode) {
		this.code = generateCustomerCode();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.balance = balance;
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
	}

	/**
	 * Generates a unique customer code based on the number of customers created.
	 * The code is formatted as "C" followed by a zero-padded number (e.g., C0001,
	 * C0002).
	 * 
	 * @return
	 */
	private String generateCustomerCode() {
		count++;
		return String.format("C%04d", count); // Generates a code like C0001, C0002, etc.
	}

	// Getters

	public String getCode() {
		return code;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public double getBalance() {
		return balance;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getAddress() {
		return street + ", " + city + ", " + zipCode;
	}

	// Setters

	public void setFirstName(String firstName) throws ValidationException {
        if (!firstName.matches("[a-zA-Z0-9]+")) {
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

    public void setEmail(String email) throws ValidationException {
        if (!StringValidationUtils.isValidEmail(email)) {
            throw new ValidationException("Invalid email format.");
        }
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) throws ValidationException {
        if (!StringValidationUtils.isValidPhoneNumber(phoneNumber)) {
            throw new ValidationException("Invalid phone number format.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setBalance(double balance) throws ValidationException {
        if (balance < 0) {
            throw new ValidationException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public void setStreet(String street) throws ValidationException {
        if (!StringValidationUtils.isStringOnlyAlphanumericWithSpaces(street, 5)) {
            throw new ValidationException("Street must contain only alphanumeric characters and spaces.");
        }
        this.street = street;
    }

    public void setCity(String city) throws ValidationException {
        if (!StringValidationUtils.isStringOnlyAlphabeticWithSpaces(city, 5)) {
            throw new ValidationException("City must contain only alphabetic characters and spaces.");
        }
        this.city = city;
    }

    public void setZipCode(String zipCode) throws ValidationException {
        if (!StringValidationUtils.isStringOnlyNumeric(zipCode)) {
            throw new ValidationException("Zip code must contain only numeric characters.");
        }
        this.zipCode = zipCode;
    }

	@Override
	public String toString() {
		return "Customer [code=" + code + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", balance=" + balance + ", street=" + street + ", city=" + city
				+ ", zipCode=" + zipCode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return code.equals(other.code);
	}
	public int compareTo(Customer other) {
		return Double.compare(other.balance, this.balance);
	}
}
