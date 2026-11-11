package admin;

import java.util.ArrayList;

import model.Customer;
import model.ValidationException;
import restaurant.Restaurant;

public class RestAdmin extends Admin implements Comparable<RestAdmin> {
	private ArrayList<Restaurant> managedRestaurants;
	private int restaurantCount;

	public RestAdmin(String firstName, String username, String password) {
		super(firstName, username, password);
		this.managedRestaurants = new ArrayList<>();
		this.restaurantCount = 0;
	}

	public ArrayList<Restaurant> getManagedRestaurants() {
		return managedRestaurants;
	}

	public int getRestaurantCount() {
		return restaurantCount;
	}

	public void addManagedRestaurant(Restaurant restaurant) throws ValidationException {
		if (restaurant == null) {
			throw new ValidationException("Restaurant cannot be null.");
			
		}
		for (Restaurant r: managedRestaurants) {
			if (r.getCode().equals(restaurant.getCode())) {
				throw new ValidationException("Restaurant with code " + restaurant.getCode() + " is already managed by this admin.");
				
			}
		}
		managedRestaurants.add(restaurant);
		restaurantCount++;
	}

	public boolean isResponsibleForRestaurant(String restaurantCode) {
		if (restaurantCode == null)
			return false;
		for (Restaurant r: managedRestaurants) {
			if (restaurantCode.equals(r.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "RestAdmin [managedRestaurants=" + restaurantCount + ", " + super.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RestAdmin other = (RestAdmin) obj;
		return this.getUsername().equals(other.getUsername());
	}
	public int compareTo(RestAdmin other) {
		return Integer.compare(other.getRestaurantCount(), this.getRestaurantCount());
	}
}
