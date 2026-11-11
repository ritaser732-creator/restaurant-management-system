package model;

import java.util.Comparator;

public class OrderFinalPriceComparator implements Comparator<Order> {
	public int compare(Order o1, Order o2) {
		return Double.compare(o2.getFinalPrice(), o1.getFinalPrice() );
	}
}
