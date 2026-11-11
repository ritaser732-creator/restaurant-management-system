package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import model.*;
import restaurant.Restaurant;
import application.MainGUI;
import java.time.LocalDate;

public class AddOrderController {
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Restaurant> restaurantComboBox;
    @FXML private TextField priceField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        customerComboBox.setItems(FXCollections.observableArrayList(MainGUI.db.getCustomers()));
        restaurantComboBox.setItems(FXCollections.observableArrayList(MainGUI.db.getRestaurants()));
    }

    @FXML
    private void handleAddOrder() {
        try {
            Order newOrder = new Order(customerComboBox.getValue().getCode(), 
                                     restaurantComboBox.getValue(), 
                                     LocalDate.now().toString(), 
                                     Double.parseDouble(priceField.getText()));
            MainGUI.db.addOrder(newOrder);
            statusLabel.setText("Order created!");
        } catch (Exception e) {
            statusLabel.setText("Error: check input fields.");
        }
    }
}