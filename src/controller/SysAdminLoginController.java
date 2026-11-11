package controller;
import admin.Admin;
import admin.RestAdmin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ValidationException;
import application.MainGUI;
import java.io.IOException;
public class SysAdminLoginController {
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

    try {
        Admin admin = MainGUI.db.findAdminByUsername(username);

        if (!admin.getPassword().equals(password)) {
            lblError.setText("Invalid username or password.");
            return;
        }

        if (admin instanceof RestAdmin) {
            switchScreen(event, "/view/restAdminDashboard.fxml", "Restaurant Admin Dashboard");
        } else {
            switchScreen(event, "/view/systemAdminDashboard.fxml", "System Admin Dashboard");
        }

    } catch (ValidationException e) {
        lblError.setText("Invalid username or password.");
    }
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
            System.out.println("Error loading screen: " + fxmlPath);
            e.printStackTrace();
        }
    }
}