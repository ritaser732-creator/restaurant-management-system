package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ValidationException;
import javafx.collections.FXCollections;
import restaurant.Restaurant;
import application.MainGUI;

public class UpdateRatingRestController {
    @FXML private ComboBox<Restaurant> restaurantComboBox;
    @FXML private TextField ratingField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        restaurantComboBox.setItems(FXCollections.observableArrayList(MainGUI.db.getRestaurants()));
    }

    @FXML
    private void handleUpdate() {
        Restaurant selectedRest = restaurantComboBox.getValue();
        String ratingText = ratingField.getText();

        if (selectedRest == null || ratingText.isEmpty()) {
            statusLabel.setText("Please select a restaurant and enter a rating!");
            return;
        }

        try {
            double newRating = Double.parseDouble(ratingText);
            if (newRating < 1 || newRating > 5) {
                statusLabel.setText("Rating must be between 1 and 5!");
                return;}
                try {
    				selectedRest.setRating(newRating);
    				Stage stage = (Stage) ratingField.getScene().getWindow();
    				stage.close();
    			} catch (ValidationException e) {
    				
    				e.printStackTrace();
    			}
               
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid rating format!");
        }
    }
}