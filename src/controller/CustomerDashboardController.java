package controller;
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
import model.Customer;
import model.Order;
import model.ValidationException;
import restaurant.Restaurant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
public class CustomerDashboardController {
@FXML
private Label lblWelcome;
private Customer currentCustomer;

	public void initData(Customer customer) {
		this.currentCustomer = customer;
		lblWelcome.setText("Welcome, " + customer.getFirstName() + "!");
    }
	@FXML
	void handleViewDetails(ActionEvent event) {
		showAlert(Alert.AlertType.INFORMATION, "Personal Details", currentCustomer.toString());
    }
	@FXML
	void handleNewOrder(ActionEvent event) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/newOrder.fxml"));
	        Parent root = loader.load();
	        
	        NewOrderController controller = loader.getController();
	        controller.initData(currentCustomer); 
	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	void handleUpdateDetails(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateDetails.fxml"));
        Parent root = loader.load();
        
        UpdateDetailsController controller = loader.getController();
        controller.setCustomer(currentCustomer); 
        
        Stage stage = new Stage();
        stage.setTitle("Update Details");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    void handleViewMyOrders(ActionEvent event) {
        ArrayList<Order> orders = MainGUI.db.getOrdersByCustomerCode(currentCustomer.getCode());
        if (orders.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "My Orders", "You have no orders yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Order o : orders) {
                sb.append(o.toString()).append("\n");
            }
            showAlert(Alert.AlertType.INFORMATION, "My Orders", sb.toString());
        }
    }
    
    @FXML
    void handleDeposit(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Deposit Money");
        dialog.setHeaderText("Enter amount to deposit:");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    currentCustomer.setBalance(currentCustomer.getBalance() + amount);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Deposited. New balance: " + currentCustomer.getBalance());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Amount must be positive.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount entered.");
            } catch (ValidationException e) {
            	showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
			}
        });
    }
    @FXML
    void handleWithdraw(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw Money");
        dialog.setHeaderText("Enter amount to withdraw (Current Balance: " + currentCustomer.getBalance() + "):");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0 && amount <= currentCustomer.getBalance()) {
                    currentCustomer.setBalance(currentCustomer.getBalance() - amount);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawn. New balance: " + currentCustomer.getBalance());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount or insufficient funds.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount entered.");
            } catch (ValidationException e) {
            	showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
			}
        });
    }
    @FXML
    void handleShowBalance(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Current Balance", "Your balance is: " + currentCustomer.getBalance());
    }
    @FXML
    void handleShowRestaurants(ActionEvent event) {
        ArrayList<Restaurant> restaurants = MainGUI.db.getRestaurantsByCustomer(currentCustomer.getCode());
        if (restaurants.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "My Restaurants", "You haven't ordered from any restaurant yet.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Restaurant r : restaurants) {
            sb.append(r.getName()).append(" (").append(r.getCode()).append(")\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "My Restaurants", sb.toString());
    }
    @FXML
    void handleShowPremiumRestaurants(ActionEvent event) {
        ArrayList<Restaurant> premium = MainGUI.db.getPremiumRestaurantsForCustomer(currentCustomer);
        if (premium.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Premium Restaurants", "You haven't ordered from any premium restaurant yet.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Restaurant r : premium) {
            sb.append(r.getName()).append(" (").append(r.getCode()).append(")\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Premium Restaurants", sb.toString());
    }
    @FXML
    void handleSearchRestaurant(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Restaurant");
        dialog.setHeaderText("Enter Restaurant Code:");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(code -> {
            Restaurant r;
			try {
				r = MainGUI.db.findRestaurantByCode(code.trim());
				if (r != null) {
                showAlert(Alert.AlertType.INFORMATION, "Restaurant Found", r.toString());}
			} catch (ValidationException e) {
				showAlert(Alert.AlertType.ERROR, "Not Found", "No restaurant found with code: " + code);
            }
        });
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
        alert.setResizable(true);
        alert.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        alert.showAndWait();
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
}