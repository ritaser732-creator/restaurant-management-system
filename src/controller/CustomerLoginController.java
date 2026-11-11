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
import model.Customer;
import model.ValidationException;

import java.io.IOException;
public class CustomerLoginController {
    @FXML
    private TextField fldCustomerCode;
    @FXML
    private Label lblError;
    @FXML
    private Button btnLogin;
    @FXML
	private Button btnBack;
    @FXML
    void handleLogin(ActionEvent event) {
        String code = fldCustomerCode.getText().trim();
        
        if (code.isEmpty()) {
            lblError.setText("Please enter your customer code.");
            return;
        }
        
        try {
            Customer customer = MainGUI.db.findCustomerByCode(code);
            
            lblError.setText(""); 
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerDashboard.fxml"));
            Parent root = loader.load();
            
            CustomerDashboardController controller = loader.getController();
            controller.initData(customer);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Dashboard");
            stage.show();
            
        } catch (ValidationException e) {
            lblError.setText(e.getMessage());
            
        } catch (IOException e) {
            lblError.setText("Error loading dashboard screen.");
            e.printStackTrace();
        }
    }
    @FXML
    void handleBack(ActionEvent event) {
        switchScreen(event, "/view/UserTypeSelection.fxml", "Select User Type");
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