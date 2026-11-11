package model;

import restaurant.Restaurant;
import utils.StringValidationUtils;

public class Order  {
	private final String code;
	private String customerCode;
	private Restaurant restaurant;
	private String restaurantCode;
	private String riderCode;

	private String orderedDate;
	private String deliveredDate;

	private double basicPrice;
	private double finalPrice;
	private OrderStatus status;

	private static int count = 0;
	

	public Order(String customerCode, Restaurant restaurant, String riderCode, String orderedDate, double basicPrice) {
		this.code = generateOrderCode();
		this.customerCode = customerCode;
		this.restaurant = restaurant;
		this.restaurantCode = restaurant.getCode();
		this.riderCode = riderCode;
		this.orderedDate = orderedDate;
		this.basicPrice = basicPrice;
		this.finalPrice = calculateFinalPrice();
		this.status = OrderStatus.PENDING;
		this.deliveredDate = "0";
	}

	public Order(String customerCode, Restaurant restaurant, String orderedDate, double basicPrice) {
		this(customerCode, restaurant, null, orderedDate, basicPrice);
	}

	private String generateOrderCode() {
		count++;
		return String.format("O%04d", count);
	}
	
	/**
	 * Calculates the final price of the order based on the restaurant's pricing
	 * rules. If no restaurant is associated, it returns the basic price.
	 */
	private double calculateFinalPrice() {
		if (restaurant == null) {
			return basicPrice;
		}
		return restaurant.calculateFinalPrice(basicPrice);
	}

	public String getCode() {
		return code;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public String getRestaurantCode() {
		return restaurantCode;
	}

	public String getRiderCode() {
		return riderCode;
	}

	public String getOrderedDate() {
		return orderedDate;
	}

	public String getDeliverdDate() {
		return deliveredDate;
	}

	public String getDeliveredDate() {
		return deliveredDate;
	}

	public double getBasicPrice() {
		return basicPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setCustomerCode(String customerCode) throws ValidationException {
        if (StringValidationUtils.isNullOrEmpty(customerCode)) {
            throw new ValidationException("Customer code cannot be null or empty.");
        }
        this.customerCode = customerCode;
    }

	public void setRestaurant(Restaurant restaurant) throws ValidationException {
	    if (restaurant == null) {
	        throw new ValidationException("Restaurant cannot be null.");
	    }
	    
	    if (this.restaurant != null && this.restaurant.getCode().equals(restaurant.getCode())) {
	        throw new ValidationException("Restaurant " + restaurant.getName() + " is already set for this order.");
	    }
	    
	    this.restaurant = restaurant;
	    this.restaurantCode = restaurant.getCode();
	    this.finalPrice = calculateFinalPrice();
	}

	public void setRiderCode(String riderCode) {
		this.riderCode = riderCode;
	}

	public void setOrderedDate(String orderedDate) {
		if (StringValidationUtils.isValidOrderDate(orderedDate))
			this.orderedDate = orderedDate;
	}

	public void setDeliveredDate(String deliveredDate) throws ValidationException {
        if (!"0".equals(deliveredDate)) {
            if (!StringValidationUtils.isValidOrderDate(deliveredDate)) {
                throw new ValidationException("Invalid delivered date format.");
            }
            if (StringValidationUtils.isBefore(deliveredDate, orderedDate)) {
                throw new ValidationException("Delivered date cannot be before ordered date.");
            }
        }
        this.deliveredDate = deliveredDate;
    }

	public void setBasicPrice(double basicPrice) throws ValidationException {
        if (basicPrice < 0) {
            throw new ValidationException("Basic price cannot be negative.");
        }
        this.basicPrice = basicPrice;
        if (restaurant != null) {
            this.finalPrice = restaurant.calculateFinalPrice(basicPrice);
        }
    }

	

	public void setStatus(OrderStatus newStatus) throws ValidationException { 
	    
	    if (this.status == OrderStatus.DELIVERED && newStatus != OrderStatus.DELIVERED) {
	    	 throw new ValidationException("Cannot change status from delivered.");
	       
	    }
	    
	    if (this.status == OrderStatus.DELIVERING && newStatus == OrderStatus.PENDING) {
	    	throw new ValidationException("Cannot change status from delivering to pending.");
	      
	    }

	    if (this.status == newStatus) {
	    	throw new ValidationException("The order already has this status.");
	    }

	    this.status = newStatus;
	}

	@Override
	public String toString() {
		return "Order [code=" + code + ", customerCode=" + customerCode + ", restaurantCode=" + restaurantCode
				+ ", riderCode=" + riderCode + ", orderedDate=" + orderedDate + ", deliveredDate=" + deliveredDate
				+ ", basicPrice=" + basicPrice + ", finalPrice=" + finalPrice + ", status=" + status + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return code.equals(((Order) obj).code);
	}
	
}
