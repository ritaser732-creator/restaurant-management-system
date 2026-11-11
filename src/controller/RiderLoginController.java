package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.MainGUI;
import model.Rider;
import model.ValidationException;

import java.io.IOException;
public class RiderLoginController {
    @FXML
    private TextField fldRiderId;
    @FXML
    private Label lblError;
    @FXML
	private Button btnLogin;
	@FXML
	private Button btnBack;
	 @FXML
	    void handleLogin(ActionEvent event) {
	        String id = fldRiderId.getText().trim();
	        if (id.isEmpty()) {
	            lblError.setText("Please enter your Rider ID.");
	            return;
	        }
	        Rider rider;
			try {
				rider = MainGUI.db.findRiderById(id);
				if (rider == null) {
	            lblError.setText("Rider not found.");
	            return;
				}
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/riderDashboard.fxml"));
	                Parent root = loader.load();
	                
	            
	                RiderDashboardController controller = loader.getController();
	                controller.initData(rider);
	                
	                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                stage.setScene(new Scene(root));
	                stage.setTitle("Rider Dashboard");
	                stage.show();
	        
			} catch (ValidationException e1) {
				lblError.setText("");}
	         catch (IOException e) {
	                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard.");
	            }
	        }
	    
    @FXML
    void handleBack(ActionEvent event) {
        switchScreen(event, "/view/userTypeSelection.fxml", "Select User Type");
    }
    private void switchScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load screen: " + fxmlPath);
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}