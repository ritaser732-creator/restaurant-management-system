package restaurant;

import model.ValidationException;

public class FastFoodRestaurant extends Restaurant {
	private int averagePreparationTime;
	private double extraCharges;

	public FastFoodRestaurant(String name, String type, double rating, boolean isOpen, double deliveryFee,
			int averagePreparationTime, double extraCharges) {
		super(name, type, rating, isOpen, deliveryFee);
		this.averagePreparationTime = averagePreparationTime;
		this.extraCharges = extraCharges;
	}

	public int getAveragePreparationTime() {
		return averagePreparationTime;
	}

	public double getExtraCharges() {
		return extraCharges;
	}

	public void setAveragePreparationTime(int averagePreparationTime) throws ValidationException {
		if (averagePreparationTime < 0) {
			throw new ValidationException("Average preparation time cannot be negative.");
		
		}
		this.averagePreparationTime = averagePreparationTime;
	}

	public void setExtraCharges(double extraCharges) throws ValidationException {
		if (extraCharges < 0) {
			throw new ValidationException("Extra charges cannot be negative.");
		}
		this.extraCharges = extraCharges;
	}

	@Override
	public double calculateFinalPrice(double basicPrice) {
		return basicPrice + deliveryFee + extraCharges;
	}

	@Override
	public String toString() {
		return "FastFoodRestaurant [averagePreparationTime=" + averagePreparationTime + ", extraCharges=" + extraCharges
				+ ", " + super.toString() + "]";
	}
}
