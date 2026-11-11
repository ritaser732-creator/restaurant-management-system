package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import application.MainGUI;
import model.Customer;
import model.Order;
import model.Rider;
import restaurant.PremiumRestaurant;
import restaurant.Restaurant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class ReportsAndSortingController {
    @FXML
    private TextArea txtResults;
    
    private <T> void displayList(List<T> list, String title) {
        if (list.isEmpty()) {
            txtResults.setText(title + "\n\nNo records found.");
            return;
        }
        String result = list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        txtResults.setText(title + " (Total: " + list.size() + ")\n\n" + result);
    }
    @FXML
    void handleSortCustomersByBalance(ActionEvent event) {
        ArrayList<Customer> customers = new ArrayList<>(MainGUI.db.getCustomers());
        
        customers.sort(Customer::compareTo);
        displayList(customers, "--- Customers Sorted by Balance ---");
    }
    @FXML
    void handleSortCustomersByFirstName(ActionEvent event) {
        ArrayList<Customer> customers = new ArrayList<>(MainGUI.db.getCustomers());
        
        customers.sort(Comparator.comparing(Customer::getFirstName));
        displayList(customers, "--- Customers Sorted by First Name ---");
    }
    @FXML
    void handleSortRestaurantsByRating(ActionEvent event) {
        ArrayList<Restaurant> restaurants = new ArrayList<>(MainGUI.db.getRestaurants());
        
        restaurants.sort(new model.RestaurantRatingComparator());
        displayList(restaurants, "--- Restaurants Sorted by Rating ---");
    }
    @FXML
    void handleSortOrdersByPrice(ActionEvent event) {
        ArrayList<Order> orders = new ArrayList<>(MainGUI.db.getOrders());
        
        orders.sort(new model.OrderFinalPriceComparator());
        displayList(orders, "--- Orders Sorted by Final Price ---");
    }
    @FXML
    void handleSortRidersByDeliveries(ActionEvent event) {
        ArrayList<Rider> riders = new ArrayList<>(MainGUI.db.getRiders());
        
        riders.sort((r1, r2) -> {
            long count1 = r1.getAssignedOrders().stream().filter(o -> "delivered".equals(o.getStatus())).count();
            long count2 = r2.getAssignedOrders().stream().filter(o -> "delivered".equals(o.getStatus())).count();
            return Long.compare(count2, count1); 
        });
        displayList(riders, "--- Riders Sorted by Number of Deliveries ---");
    }
    @FXML
    void handleShowOpenRestaurants(ActionEvent event) {
        List<Restaurant> openRestaurants = MainGUI.db.getRestaurants().stream()
                .filter(Restaurant::isOpen)
                .collect(Collectors.toList());
        displayList(openRestaurants, "--- Open Restaurants ---");
    }
    @FXML
    void handleShowPremiumRestaurants(ActionEvent event) {
        List<Restaurant> premiumRestaurants = MainGUI.db.getRestaurants().stream()
                .filter(r -> r instanceof PremiumRestaurant)
                .collect(Collectors.toList());
        displayList(premiumRestaurants, "--- Premium Restaurants ---");
    }
    @FXML
    void handleShowAvailableRiders(ActionEvent event) {
        List<Rider> availableRiders = MainGUI.db.getRiders().stream()
                .filter(Rider::isAvailable)
                .collect(Collectors.toList());
        displayList(availableRiders, "--- Available Riders ---");
    }
    @FXML
    void handleShowTotalPayments(ActionEvent event) {
        double totalPayments = MainGUI.db.getOrders().stream()
                .mapToDouble(Order::getFinalPrice)
                .sum();
        txtResults.setText("--- Total Payments in the System ---\n\nTotal Revenue: " + String.format("%.2f", totalPayments));
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to load screen.");
            alert.showAndWait();
        }
    }
}