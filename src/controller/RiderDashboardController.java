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
import model.Order;
import model.OrderStatus;
import model.Rider;
import model.ValidationException;
import utils.StringValidationUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
public class RiderDashboardController {
    @FXML
    private Label lblWelcome;
    private Rider currentRider;
    
    public void initData(Rider rider) {
        this.currentRider = rider;
        lblWelcome.setText("Welcome, Rider " + rider.getFirstName() + "!");
    }
    @FXML
    void handleViewAllAssigned(ActionEvent event) {
        ArrayList<Order> assigned = currentRider.getAssignedOrders();
        if (assigned.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Assigned Orders", "You have no assigned orders.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Order o : assigned) {
            sb.append(o.getCode()).append(" - Status: ").append(o.getStatus()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Assigned Orders", sb.toString());
    }
    @FXML
    void handleViewActiveOrder(ActionEvent event) {
        ArrayList<Order> activeOrders = MainGUI.db.getActiveOrdersForRider(currentRider.getId());
        if (activeOrders.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Active Orders", "You have no active orders currently delivering.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Order o : activeOrders) {
            sb.append(o.getCode()).append(" - Customer: ").append(o.getCustomerCode()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Active Orders", sb.toString());
    }
    @FXML
    void handleUpdateStatusToDelivering(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Status");
        dialog.setHeaderText("Enter Order Code to mark as 'delivering':");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(code -> {
            try {
                Order order = MainGUI.db.findOrderByCode(code.trim());
                
                if (currentRider.getId().equals(order.getRiderCode())) {
                    order.setStatus(OrderStatus.DELIVERING);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order " + order.getCode() + " is now marked as delivering.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Order not assigned to you.");
                }
            } catch (ValidationException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });
    }
    @FXML
    void handleUpdateStatusToDelivered(ActionEvent event) {
        TextInputDialog orderDialog = new TextInputDialog();
        orderDialog.setTitle("Update Status");
        orderDialog.setHeaderText("Enter Order Code to mark as 'delivered':");
        Optional<String> result = orderDialog.showAndWait();

        result.ifPresent(code -> {
            try {
                Order order = MainGUI.db.findOrderByCode(code.trim());
                
                if (currentRider.getId().equals(order.getRiderCode())) {
                    TextInputDialog dateDialog = new TextInputDialog();
                    dateDialog.setTitle("Delivery Date");
                    dateDialog.setHeaderText("Enter Delivered Date (DD-MM-YYYY):");
                    Optional<String> dateResult = dateDialog.showAndWait();

                    dateResult.ifPresent(date -> {
                        try {
                            order.setDeliveredDate(date);
                            order.setStatus(OrderStatus.DELIVERED);
                            currentRider.setAvailable(true);
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Order delivered successfully!");
                        } catch (ValidationException e) {
                            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                        }
                    });
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Order not assigned to you.");
                }
            } catch (ValidationException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        });
    }
    @FXML
    void handleViewHistory(ActionEvent event) {
        ArrayList<Order> history = new ArrayList<>();
        for (Order o : currentRider.getAssignedOrders()) {
            if ("delivered".equals(o.getStatus())) {
                history.add(o);
            }
        }
        
        if (history.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Delivery History", "No completed deliveries found.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        for (Order o : history) {
            sb.append(o.getCode()).append(" delivered on ").append(o.getDeliveredDate()).append("\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Delivery History", sb.toString());
    }
    @FXML
    void handleTotalDeliveries(ActionEvent event) {
        long count = currentRider.getAssignedOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .count();
                
        showAlert(Alert.AlertType.INFORMATION, "Total Deliveries", "You have successfully completed " + count + " deliveries.");
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
}