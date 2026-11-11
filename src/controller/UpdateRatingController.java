package controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ValidationException;
import restaurant.Restaurant;

public class UpdateRatingController {
    @FXML private TextField txtRating;
    @FXML private Label lblRestaurantName;
    private Restaurant selectedRestaurant;

    public void initData(Restaurant restaurant) {
        this.selectedRestaurant = restaurant;
        lblRestaurantName.setText("Updating: " + restaurant.getName());
        txtRating.setText(String.valueOf(restaurant.getRating()));
    }

    @FXML
    void handleUpdate() {
        try {
            double newRating = Double.parseDouble(txtRating.getText());
            if (newRating < 1 || newRating > 5) {
            	txtRating.setText("Rating must be between 1 and 5!");
                return;
            }
            try {
				selectedRestaurant.setRating(newRating);
				Stage stage = (Stage) txtRating.getScene().getWindow();
				stage.close();
			} catch (ValidationException e) {
				
				e.printStackTrace();
			}
            
        } catch (NumberFormatException e) {
            
        }
    }
}