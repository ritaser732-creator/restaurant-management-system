package model;

public class RestaurantIsNotManagedByThisAdmin extends Exception{
	public RestaurantIsNotManagedByThisAdmin(String messange) {
		super(messange);
	}
}
