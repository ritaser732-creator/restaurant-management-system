package controller;
import admin.RestAdmin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import application.MainGUI;
import model.Order;
import model.ValidationException;
import restaurant.Restaurant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
public class RestAdminDashboardController {
    @FXML
    private Label lblWelcome;
    private RestAdmin currentAdmin;
    
    public void initData(RestAdmin admin) {
        currentAdmin = admin;
        lblWelcome.setText("Welcome, " + admin.getFirstName() + "!");
    }
    @FXML
    void handleShowMyRestaurants(ActionEvent event) {
        ArrayList<Restaurant> myRestaurants = currentAdmin.getManagedRestaurants();
        if (myRestaurants.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "My Restaurants", "You are not managing any restaurants yet.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Restaurant r : myRestaurants) {
            sb.append(r.getName()).append(" (").append(r.getCode()).append(") - ").append(r.isOpen() ? "Open" : "Closed").append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "My Restaurants", sb.toString());
    }
    @FXML
    void handleAddCustomer(ActionEvent event) {
    	 openWindow("/view/addCustomer.fxml", "Add New Customer");
    }
    @FXML
    void handleAddOrder(ActionEvent event) {
    	 openWindow("/view/addOrder.fxml", "Add New Order");
    }
    @FXML
    void handleAssignRider(ActionEvent event) {
    	 openWindow("/view/assignRider.fxml", "Add New Customer");
    }
    @FXML
    void handleViewRestaurantOrders(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restaurant Orders");
        dialog.setHeaderText("Enter Restaurant Code to view its orders:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(code -> {
            if (currentAdmin.isResponsibleForRestaurant(code.trim())) {
                ArrayList<Order> allOrders = MainGUI.db.getOrders();
                StringBuilder sb = new StringBuilder();
                int count = 0;
                for (Order o : allOrders) {
                    if (o.getRestaurantCode().equals(code.trim())) {
                        sb.append(o.getCode()).append(" (Customer: ").append(o.getCustomerCode()).append(")\n");
                        count++;
                    }
                }
                if (count == 0) sb.append("No orders found for this restaurant.");
                showAlert(Alert.AlertType.INFORMATION, "Orders for " + code, sb.toString());
            } else {
                showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not manage the restaurant with code: " + code);
            }
        });
    }
    @FXML
    void handleShowOpenByCuisine(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Open Restaurants by Cuisine");
        dialog.setHeaderText("Enter Cuisine Type (e.g., fast, regular, premium):");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(type -> {
            ArrayList<Restaurant> openRest = MainGUI.db.getOpenRestaurantsByType(type.trim());
            if (openRest.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Result", "No open restaurants found for type: " + type);
            } else {
                StringBuilder sb = new StringBuilder();
                for (Restaurant r : openRest) {
                    sb.append(r.getName()).append(" - Rating: ").append(r.getRating()).append("\n");
                }
                showAlert(Alert.AlertType.INFORMATION, "Open Restaurants (" + type + ")", sb.toString());
            }
        });
    }
    @FXML
    void handleUpdateRating(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateRatingRest.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Update Rating");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleToggleStatus(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Toggle Restaurant Status");
        dialog.setHeaderText("Enter Restaurant Code to Open/Close:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(code -> {
            if (currentAdmin.isResponsibleForRestaurant(code.trim())) {
                Restaurant r;
				try {
					r = MainGUI.db.findRestaurantByCode(code.trim());
					if (r != null) {
                    r.setOpen(!r.isOpen());
                    showAlert(Alert.AlertType.INFORMATION, "Success", r.getName() + " is now " + (r.isOpen() ? "Open": "Closed")+".");  
					} 
				} catch (ValidationException e) {
					showAlert(Alert.AlertType.ERROR, "Access Denied", "You do not manage the restaurant with code: " + code);
            }
          }
       });
    }
    @FXML
    void handleBasicReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BasicReports.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            
            BasicReportsController controller = loader.getController();
            controller.setAdmin(this.currentAdmin); 
            
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
    @FXML
    void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/WelcomeScreen.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Food Delivery System - Welcome");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load welcome screen.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}