package controller;

import admin.RestAdmin;
import application.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ValidationException;

public class AddRestAdminController {

    @FXML private TextField fldFirstName;
    @FXML private TextField fldUsername;
    @FXML private PasswordField fldPassword;
    @FXML private Label lblError;

    @FXML
    void handleSave(ActionEvent event) {
        String firstName = fldFirstName.getText().trim();
        String username = fldUsername.getText().trim();
        String password = fldPassword.getText().trim();

        if (firstName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please fill in all fields.");
            return;
        }

        try {
			MainGUI.db.addRestAdmin(new RestAdmin(firstName, username, password));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
        
        closeWindow(event);
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}