package controller;
import admin.Admin;
import admin.RestAdmin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ValidationException;
import application.MainGUI;
import java.io.IOException;
public class RestAdminLoginController {
    @FXML
    private TextField fldUsername;
    @FXML
    private PasswordField fldPassword;
    @FXML
    private Label lblError;
    @FXML
   	private Button btnLogin;
   	@FXML
   	private Button btnBack;
    @FXML
    void handleLogin(ActionEvent event) {
        String username = fldUsername.getText().trim();
        String password = fldPassword.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please fill in all fields.");
            return;
        }
        admin.Admin admin;
		try {
			admin = MainGUI.db.findAdminByUsername(username);
			if (admin == null || !(admin instanceof admin.RestAdmin) || !admin.getPassword().equals(password)) {
            lblError.setText("Invalid username or password.");
            return;
			}
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/restAdminDashboard.fxml"));
                Parent root = loader.load();
                
               
                RestAdminDashboardController controller = loader.getController();
                controller.initData((admin.RestAdmin) admin);
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Restaurant Admin Dashboard");
                stage.show();
			 
		} catch (ValidationException e) {
			lblError.setText(e.getMessage());}
           catch (IOException e1) {
        	   e1.printStackTrace();
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
            showAlert(Alert.AlertType.ERROR, "Error", "failed to load screen: " + fxmlPath);
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
