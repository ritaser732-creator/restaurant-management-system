package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UpdateStatusController {

    @FXML private Label lblAdminName;
    @FXML private ComboBox<String> comboStatus;
    
    private admin.RestAdmin selectedAdmin;

    @FXML
    public void initialize() {
        comboStatus.setItems(FXCollections.observableArrayList("Active", "Inactive", "Suspended"));
    }

    public void initData(admin.RestAdmin admin) {
        this.selectedAdmin = admin;
        lblAdminName.setText("Admin: " + admin.getUsername());
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        String newStatus = comboStatus.getValue();
        if (newStatus != null) {
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