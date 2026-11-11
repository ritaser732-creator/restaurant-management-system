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
public class SystemAdminDashboardController {
	@FXML
	private Button btnManageCustomers;
	@FXML
	private Button btnManageRestaurants;
	@FXML
	private Button btnManageOrders;
	@FXML
	private Button btnManageRiders;
	@FXML
	private Button btnManageRestAdmins;
	@FXML
	private Button btnReports;
	@FXML
	private Button btnSaveData;
	@FXML
	private Button btnLoadData;
	@FXML
	private Button btnLogout;
	
@FXML
void goToManageCustomers(ActionEvent event) {
switchScreen(event, "/view/manageCustomers.fxml", "Manage Customers");
}
@FXML
void goToManageRestaurants(ActionEvent event) {
switchScreen(event, "/view/manageRestaurants.fxml", "Manage Restaurants");
    }
@FXML
void goToManageOrders(ActionEvent event) {
    switchScreen(event, "/view/manageOrders.fxml", "Manage Orders");
}
@FXML
void goToManageRiders(ActionEvent event) {
switchScreen(event, "/view/manageRiders.fxml", "Manage Riders");
    }
@FXML
void goToManageRestAdmins(ActionEvent event) {
    switchScreen(event, "/view/manageRestAdmins.fxml", "Manage Rest Admins");
}

@FXML
void goToReports(ActionEvent event) {
switchScreen(event, "/view/reportsAndSorting.fxml", "Reports and Sorting");
    }
@FXML
void handleSaveData(ActionEvent event) {
try {
utils.DataHandler.saveData(application.MainGUI.db);
showAlert(Alert.AlertType.INFORMATION, "Success", "All data has been successfully saved to text files.");
} catch (Exception e) {
showAlert(Alert.AlertType.ERROR, "Save Error", "An error occurred while saving data: " + e.getMessage());
e.printStackTrace();
        }
    }
@FXML
void handleLoadData(ActionEvent event) {
try {

application.MainGUI.db = new model.DeliveryDataBase();
utils.DataHandler.loadData(application.MainGUI.db);
showAlert(Alert.AlertType.INFORMATION, "Success", "All data has been successfully loaded from text files.");
} catch (Exception e) {
showAlert(Alert.AlertType.ERROR, "Load Error", "An error occurred while loading data: " + e.getMessage());
e.printStackTrace();
        }
    }
@FXML
void handleLogout(ActionEvent event) {
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