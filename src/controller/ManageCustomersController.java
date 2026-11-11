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
import model.Customer;
import model.Order;
import model.ValidationException;
import restaurant.Restaurant;
import java.io.IOException;
import java.util.ArrayList;
public class ManageCustomersController {
@FXML
private TextField fldSearchCode;
@FXML
    private TableView<Customer> tblCustomers;
    @FXML
    private TableColumn<Customer, String> colCode;
    @FXML
    private TableColumn<Customer, String> colFirstName;
    @FXML
    private TableColumn<Customer, String> colLastName;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TableColumn<Customer, Double> colBalance;
    @FXML
    private TableColumn<Customer, String> colAddress;
    private ObservableList<Customer> customerList;
    @FXML
    public void initialize() {
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address")); 
        loadAllCustomers();
    }
    private void loadAllCustomers() {
       
        customerList = FXCollections.observableArrayList(MainGUI.db.getCustomers());
        tblCustomers.setItems(customerList);
    }
    @FXML
    void handleSearchCustomer(ActionEvent event) {
        String code = fldSearchCode.getText().trim();
        
        if (code.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a customer code.");
            return;
        }

        try {
            Customer c = MainGUI.db.findCustomerByCode(code);
            
            ObservableList<Customer> singleCustomer = FXCollections.observableArrayList(c);
            tblCustomers.setItems(singleCustomer);
            
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", e.getMessage());
            
            tblCustomers.setItems(FXCollections.observableArrayList());
        }
    }
    @FXML
    void handleClearSearch(ActionEvent event) {
        fldSearchCode.clear();
        loadAllCustomers();
    }
    @FXML
    void handleAddCustomer(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomerForm.fxml"));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Customer");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleUpdateCustomer(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Customer Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer fromthe table to edit");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateCustomerForm.fxml"));
            Parent root = loader.load();

            UpdateCustomerController controller = loader.getController();
            controller.initData(selectedCustomer); 
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Customer - " + selectedCustomer.getFirstName());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleShowOrders(ActionEvent event) {
        Customer selected = tblCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer from the table first.");
            return;
        }
        ArrayList<Order> orders = MainGUI.db.getOrdersByCustomerCode(selected.getCode());
        showAlert(Alert.AlertType.INFORMATION, "Customer Orders", "Orders count: " + orders.size());
    }
    @FXML
    void handleShowRestaurants(ActionEvent event) {
        Customer selected = tblCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer from the table first.");
            return;
        }
        ArrayList<Restaurant> restaurants = MainGUI.db.getRestaurantsByCustomer(selected.getCode());
        showAlert(Alert.AlertType.INFORMATION, "Restaurants", "Ordered from " + restaurants.size() + " unique restaurants.");
    }
    @FXML
    void handleShowPremiumRestaurants(ActionEvent event) {
        Customer selected = tblCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a customer from the table first.");
            return;
        }
        ArrayList<Restaurant> premium = MainGUI.db.getPremiumRestaurantsForCustomer(selected);
        showAlert(Alert.AlertType.INFORMATION, "Premium Restaurants", "Ordered from " + premium.size() + " premium restaurants.");
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