package application;

import admin.RestAdmin;
import model.*;
import restaurant.*;
import java.util.ArrayList;

public class DataInitializer {
    
    public static void initData(DeliveryDataBase db) {
        try {
            // 3 RestAdmins
            db.addRestAdmin(new RestAdmin("Dana", "resta", "Aa!123456")); 
            db.addRestAdmin(new RestAdmin("Yossi", "restb", "Bb@123456")); 
            db.addRestAdmin(new RestAdmin("Noa", "restc", "Cc#123456")); 

            // 10 Customers
            for (int i = 1; i <= 10; i++) {
                db.addCustomer(new Customer("C" + i, "User", "c" + i + "@mail.com", "05012345" + i, 100.0, "Street " + i, "City", "1234" + i));
            }

            // 10 Restaurants
            db.addRestaurant(new Restaurant("Home Food", "regular", 4.3, true, 12)); 
            db.addRestaurant(new Restaurant("Pasta Place", "regular", 4.0, true, 10)); 
            db.addRestaurant(new Restaurant("Grill Box", "regular", 4.1, true, 11)); 
            db.addRestaurant(new FastFoodRestaurant("BurgerX", "fast", 4.5, true, 8, 15, 3)); 
            db.addRestaurant(new FastFoodRestaurant("PizzaGo", "fast", 4.2, true, 7, 20, 4)); 
            db.addRestaurant(new FastFoodRestaurant("TacoNow", "fast", 4.0, true, 7, 10, 2)); 
            db.addRestaurant(new PremiumRestaurant("Chef Table", "premium", 4.9, true, 15, 120, 0.20)); 
            db.addRestaurant(new PremiumRestaurant("Sea Prime", "premium", 4.8, true, 14, 100, 0.18)); 
            db.addRestaurant(new PremiumRestaurant("Royal Asia", "premium", 4.7, true, 13, 110, 0.16)); 
            db.addRestaurant(new Restaurant("Healthy Bowl", "regular", 4.1, true, 9)); 

            // 5 Riders
            for (int i = 1; i <= 5; i++) {
                db.addRider(new Rider("Rider" + i, "Test", "05099999" + i, "car"));
            }

           
        } catch (ValidationException e) {
           
        }
    }
}