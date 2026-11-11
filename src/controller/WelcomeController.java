package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomeController {

    @FXML private Button btnSysAdminLogin;
    @FXML private Button btnUserLogin;
    @FXML private Button btnExit;

    @FXML
    void handleSysAdminLogin(ActionEvent event) {
        switchScreen(event, "/view/SysAdminLogin.fxml", "System Admin Login");
    }

    @FXML
    void handleUserLogin(ActionEvent event) {
        switchScreen(event, "/view/UserTypeSelection.fxml", "User Type Selection");
    }

    @FXML
    void handleExit(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    private void switchScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}