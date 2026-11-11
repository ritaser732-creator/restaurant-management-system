package controller;
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
import javafx.stage.Stage;
import application.MainGUI;
import model.Order;
import model.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
public class ManageOrdersController {
@FXML
private TextField fldOrderCode;
@FXML
private TextField fldCustomerCode;
    @FXML
    private TextField fldRestaurantCode;
    @FXML
    private TableView<Order> tblOrders;
    @FXML
    private TableColumn<Order, String> colCode;
    @FXML
    private TableColumn<Order, String> colCustomer;
    @FXML
    private TableColumn<Order, String> colRestaurant;
    @FXML
    private TableColumn<Order, String> colRider;
    @FXML
    private TableColumn<Order, String> colDate;
    @FXML
    private TableColumn<Order, String> colStatus;
    @FXML
    private TableColumn<Order, Double> colBasicPrice;
    @FXML
    private TableColumn<Order, Double> colFinalPrice;
    private ObservableList<Order> orderList;
    @FXML
    public void initialize() {
     
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerCode"));
        colRestaurant.setCellValueFactory(new PropertyValueFactory<>("restaurantCode"));
        colRider.setCellValueFactory(new PropertyValueFactory<>("riderCode"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderedDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colBasicPrice.setCellValueFactory(new PropertyValueFactory<>("basicPrice"));
        colFinalPrice.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
        loadAllOrders();
    }
    private void loadAllOrders() {
        orderList = FXCollections.observableArrayList(MainGUI.db.getOrders());
        tblOrders.setItems(orderList);
    }
    @FXML
    void handleSearchOrder(ActionEvent event) {
        String code = fldOrderCode.getText().trim();
        if (code.isEmpty()) return;
        Order o;
		try {
			o = MainGUI.db.findOrderByCode(code);
			if (o != null) {
            tblOrders.setItems(FXCollections.observableArrayList(o));
            }else {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No order found with code: " + code);
            tblOrders.setItems(FXCollections.observableArrayList());
            }
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
    }
    @FXML
    void handleSearchByCustomer(ActionEvent event) {
        String customerCode = fldCustomerCode.getText().trim();
        if (customerCode.isEmpty()) return;
        ArrayList<Order> filtered = MainGUI.db.getOrdersByCustomerCode(customerCode);
        tblOrders.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    void handleSearchByRestaurant(ActionEvent event) {
        String restaurantCode = fldRestaurantCode.getText().trim();
        if (restaurantCode.isEmpty()) return;
        ArrayList<Order> filtered = (ArrayList<Order>) MainGUI.db.getOrders().stream()
                .filter(o -> o.getRestaurantCode().equals(restaurantCode))
                .collect(Collectors.toList());
        tblOrders.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    void handleHighestPrice(ActionEvent event) {
        ArrayList<Order> allOrders = MainGUI.db.getOrders();
        if (allOrders.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Info", "No orders in the system.");
            return;
        }
        
        
        Order maxOrder = Collections.max(allOrders, Comparator.comparingDouble(Order::getFinalPrice));
        tblOrders.setItems(FXCollections.observableArrayList(maxOrder));
    }
    @FXML
    void handleClearFilters(ActionEvent event) {
        fldOrderCode.clear();
        fldCustomerCode.clear();
        fldRestaurantCode.clear();
        loadAllOrders();
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
}