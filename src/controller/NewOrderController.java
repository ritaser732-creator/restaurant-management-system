package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Customer;
import model.Order;
import restaurant.Restaurant;
import java.time.LocalDate;

import application.MainGUI;

public class NewOrderController {

    @FXML private ComboBox<Restaurant> restaurantComboBox;
    @FXML private TextField priceField;
    @FXML private Label statusLabel;

    private Customer currentCustomer;

    @FXML
    public void initialize() {
    	restaurantComboBox.setItems(FXCollections.observableArrayList(MainGUI.db.getRestaurants()));
    }

    public void initData(Customer customer) {
        this.currentCustomer = customer;
    }

    @FXML
    private void handlePlaceOrder() {
        Restaurant selectedRest = restaurantComboBox.getValue();
        String priceText = priceField.getText();

        if (selectedRest == null || priceText.isEmpty()) {
            statusLabel.setText("Error: Please fill all fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            String date = LocalDate.now().toString(); 
            Order newOrder = new Order(currentCustomer.getCode(), selectedRest, date, price);

             MainGUI.db.addOrder(newOrder);

            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Order placed successfully!");
            
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Invalid price format!");
        }
    }
}