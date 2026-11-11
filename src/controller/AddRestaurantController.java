package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import restaurant.Restaurant;


public class AddRestaurantController {
    @FXML private TextField txtCode, txtName, txtType, txtRating, txtFee;

    @FXML
    void handleSave() {
        try {
            String code = txtCode.getText();
            String name = txtName.getText();
            String type = txtType.getText();
            double rating = Double.parseDouble(txtRating.getText());
            double fee = Double.parseDouble(txtFee.getText());

            if (code.isEmpty() || name.isEmpty()) {
                showAlert("Error", "All fields must be filled!");
                return;
            }

            Restaurant newRestaurant = new Restaurant( name, type, rating, true, fee);

            
            Stage stage = (Stage) txtCode.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Error", "Rating and Fee must be numbers!");
        } catch (Exception e) {
            showAlert("Error", "Failed to save restaurant: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}