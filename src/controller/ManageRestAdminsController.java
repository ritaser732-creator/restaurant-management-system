package controller;
import admin.Admin;
import admin.RestAdmin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ValidationException;
import application.MainGUI;
import java.io.IOException;
import java.util.List;
public class ManageRestAdminsController {
@FXML
private TextField fldSearchUsername;
@FXML
private TableView<RestAdmin> tblRestAdmins;
@FXML
private TableColumn<RestAdmin, String> colCode;
@FXML
private TableColumn<RestAdmin, String> colFirstName;
@FXML
private TableColumn<RestAdmin, String> colUsername;
@FXML
private TableColumn<RestAdmin, Integer> colRestCount;
private ObservableList<RestAdmin> restAdminsList;
@FXML
public void initialize() {

colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRestCount.setCellValueFactory(new PropertyValueFactory<>("restaurantCount"));
        loadAllRestAdmins();
    }
    private void loadAllRestAdmins() {
        restAdminsList = FXCollections.observableArrayList(MainGUI.db.getRestAdmins());
        tblRestAdmins.setItems(restAdminsList);
    }
    @FXML
    void handleSearchAdmin(ActionEvent event) {
        String username = fldSearchUsername.getText().trim();
        if (username.isEmpty()) return;
        Admin admin;
		try {
			admin = MainGUI.db.findAdminByUsername(username); 
			if (admin instanceof RestAdmin) {
            tblRestAdmins.setItems(FXCollections.observableArrayList((RestAdmin) admin));
        }
		} catch (ValidationException e) {
			showAlert(Alert.AlertType.INFORMATION, "Not Found", "No Restaurant Admin found with username: " + username);
            tblRestAdmins.setItems(FXCollections.observableArrayList());
        }
    }
    @FXML
    void handleClear(ActionEvent event) {
        fldSearchUsername.clear();
        loadAllRestAdmins();
    }
    @FXML
    void handleAddAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddRestAdmin.fxml")); // Убедитесь, что путь верный
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Add New Restaurant Admin");
            stage.setScene(new Scene(root));
            
            stage.initModality(Modality.APPLICATION_MODAL); 
            
            stage.showAndWait();
            
            refreshTable(); // если у вас есть такой метод
            
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }
    @FXML
    void handleAssignRestaurant(ActionEvent event) {
        RestAdmin selectedAdmin = tblRestAdmins.getSelectionModel().getSelectedItem();
        
        if (selectedAdmin == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an admin from the table first.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/assignerRestaurant.fxml"));
            Parent root = loader.load();
            
            AssignRestaurantController controller = loader.getController();
            controller.initData(selectedAdmin); 
            
            Stage stage = new Stage();
            stage.setTitle("Assign Restaurant to " + selectedAdmin.getUsername());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            refreshTable();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleUpdateStatus(ActionEvent event) {
        RestAdmin selected = tblRestAdmins.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an admin from the table first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateStatus.fxml"));
            Parent root = loader.load();

            UpdateStatusController controller = loader.getController();
            controller.initData(selected); 

            Stage stage = new Stage();
            stage.setTitle("Update Status for " + selected.getUsername());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load update status window.");
        }
    }
    @FXML
    void handleBack(ActionEvent event) {
        switchScreen(event, "/view/SystemAdminDashboard.fxml", "System Admin Dashboard");
    }
    private void switchScreen(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load screen.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void refreshTable() {
        try {
            
            loadAllRestAdmins();
            tblRestAdmins.refresh();

        } catch (Exception e) {
            System.err.println("Ошибка при обновлении таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}