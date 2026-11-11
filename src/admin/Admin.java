package admin;

import model.ValidationException;
import utils.StringValidationUtils;

public class Admin {
	protected final String firstName;
	protected String username;
	protected String password;
	protected final String code;
	protected static int adminCount = 0;

	public Admin(String firstName, String username, String password) {
		this.firstName = firstName;
		this.username = username;
		this.password = password;
		this.code = generateAdminCode();
	}
	
	/**
     * Generates a unique code for the admin based on the username and a random number.
     *
     * @return A unique code for the admin.
     */
	private String generateAdminCode() {
		adminCount++;
		return String.format("Ad%04d", adminCount);
	}

	// Getters

	public String getFirstName() {
		return firstName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getCode() {
		return code;
	}

	// Setters

	public void setUsername(String username) throws ValidationException {
		if (StringValidationUtils.isNullOrEmpty(username)) { // Check if the username is null or empty
			throw new ValidationException("Username cannot be null or empty.");
		
		}
		if (!StringValidationUtils.isStringOnlyAlphabetic(username)) // Check if the username contains only alphabetic
																		// characters
		{
			throw new ValidationException("Username must contain only alphabetic characters.");
			
		}

		this.username = username;
	}

	public void setPassword(String password) throws ValidationException {
		if (StringValidationUtils.isNullOrEmpty(password)) { // Check if the password is null or empty
			throw new ValidationException("Password cannot be null or empty.");
			
		}

		if (!StringValidationUtils.isStringOnlyAlphanumeric(password)) { // Check if the password contains only
																			// alphanumeric
																			// characters
			throw new ValidationException("Password must contain only alphanumeric characters.");
			
		}

		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [firstName=" + firstName + ", username=" + username + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Admin other = (Admin) obj;
		return username.equals(other.username);
	}
}
