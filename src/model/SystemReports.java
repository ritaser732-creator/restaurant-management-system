package model;

import java.util.ArrayList;
import java.util.Collections;
import restaurant.FastFoodRestaurant;
import restaurant.Restaurant;

public class SystemReports {

    public ArrayList<Restaurant> getRestaurantsList(ArrayList<? extends Restaurant> list) {
        return new ArrayList<>(list);
    }

    public double commonFinalPrice(ArrayList<? extends Order> list) {
        double total = 0;
        for (Order o : list) {
            total += o.getFinalPrice();
        }
        return total;
    }

    public void addFastFood(ArrayList<? super FastFoodRestaurant> list, FastFoodRestaurant restaurant) {
        list.add(restaurant);
    }

    public <T extends Comparable<T>> T getLargestObject(ArrayList<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return Collections.max(collection);
    }
}