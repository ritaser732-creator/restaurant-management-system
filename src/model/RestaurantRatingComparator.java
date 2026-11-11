package model;

import java.util.Comparator;

import restaurant.Restaurant;

public class RestaurantRatingComparator implements Comparator<Restaurant> {
	public int compare(Restaurant r1, Restaurant r2) {
		return Double.compare(r2.getRating(), r1.getRating());
	}

}
