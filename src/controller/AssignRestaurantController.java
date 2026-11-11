package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AssignRestaurantController {

    @FXML private Label lblAdminName;
    @FXML private TextField fldRestaurantCode;
    @FXML private Label lblError;
    
    private admin.RestAdmin selectedAdmin; 

    public void initData(admin.RestAdmin admin) {
        this.selectedAdmin = admin;
        lblAdminName.setText("Assigning for: " + admin.getUsername());
    }

    @FXML
    void handleAssign(ActionEvent event) {
        String restCode = fldRestaurantCode.getText().trim();
        
        if (restCode.isEmpty()) {
            lblError.setText("Please enter a restaurant code.");
            return;
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