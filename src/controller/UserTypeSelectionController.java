package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
public class UserTypeSelectionController {
	@FXML
	private Button btnCustomer;
	@FXML
	private Button btnRider;
	@FXML
	private Button btnRestAdmin;
	@FXML
	private Button btnBack;
    @FXML
    void handleCustomerLogin(ActionEvent event) {
        
        switchScreen(event, "/view/CustomerLogin.fxml", "Customer Login");
    }
    @FXML
    void handleRiderLogin(ActionEvent event) {
        
        switchScreen(event, "/view/RiderLogin.fxml", "Rider Login");
    }
    @FXML
    void handleRestAdminLogin(ActionEvent event) {
        
        switchScreen(event, "/view/RestAdminLogin.fxml", "Restaurant Admin Login");
    }
    @FXML
    void handleBack(ActionEvent event) {
        
        switchScreen(event, "/view/WelcomeScreen.fxml", "Food Delivery System - Welcome");
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
            e.printStackTrace();
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