package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DeliveryDataBase;
import model.Rider; 
public class AddRiderController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    DeliveryDataBase db = new DeliveryDataBase();

    @FXML
    void handleSave() {
        if (txtId.getText().trim().isEmpty() || 
            txtName.getText().trim().isEmpty() || 
            txtPhone.getText().trim().isEmpty()) {
            
            showAlert("Warning", "All fields are required!");
            return;
        }

        try {
           
            Rider newRider = new Rider(
                txtId.getText().trim(), 
                txtName.getText().trim(), 
                txtPhone.getText().trim(),
                "Active" 
            );
            db.addRider(newRider);

          
                Stage stage = (Stage) txtId.getScene().getWindow();
                stage.close();
           
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
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