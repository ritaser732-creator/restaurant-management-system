package restaurant;

import model.ValidationException;

public class PremiumRestaurant extends Restaurant {
	private double minimumOrderAmount;
	private double additionalCommissionRate; 

	public PremiumRestaurant(String name, String type, double rating, boolean isOpen, double deliveryFee,
			double minimumOrderAmount, double additionalCommissionRate) {
		super(name, type, rating, isOpen, deliveryFee);
		this.minimumOrderAmount = minimumOrderAmount;
		this.additionalCommissionRate = additionalCommissionRate;
	}

	public double getMinimumOrderAmount() {
		return minimumOrderAmount;
	}

	public double getAdditionalCommissionRate() {
		return additionalCommissionRate;
	}

	public void setMinimumOrderAmount(double minimumOrderAmount) throws ValidationException {
		if (minimumOrderAmount < 0) {
			throw new ValidationException("Minimum order amount cannot be negative.");
			
		}
		this.minimumOrderAmount = minimumOrderAmount;
	}

	public void setAdditionalCommissionRate(double additionalCommissionRate) throws ValidationException {
		if (additionalCommissionRate < 0 || additionalCommissionRate > 1) {
			throw new ValidationException("Additional commission rate must be between 0 and 1.");
			
		}
		this.additionalCommissionRate = additionalCommissionRate;
	}

	@Override
	public double calculateFinalPrice(double basicPrice) {
		double finalPrice = basicPrice + deliveryFee; 
		
		if (basicPrice < minimumOrderAmount) {
			finalPrice = minimumOrderAmount;
					
		}
		
		return finalPrice + (finalPrice * additionalCommissionRate);
	}

	@Override
	public String toString() {
		return "PremiumRestaurant [minimumOrderAmount=" + minimumOrderAmount + ", additionalCommissionRate="
				+ additionalCommissionRate + ", " + super.toString() + "]";
	}
}
