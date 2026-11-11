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
import javafx.stage.Modality;
import javafx.stage.Stage;
import application.MainGUI;
import model.Order;
import model.Rider;
import model.ValidationException;

import java.io.IOException;
import java.util.ArrayList;
public class ManageRidersController {
    @FXML
    private TextField fldSearchId;
    @FXML
    private TableView<Rider> tblRiders;
    @FXML
    private TableColumn<Rider, String> colId;
    @FXML
    private TableColumn<Rider, String> colFirstName;
    @FXML
    private TableColumn<Rider, String> colLastName;
    @FXML
    private TableColumn<Rider, String> colPhone;
    @FXML
    private TableColumn<Rider, String> colVehicle;
    @FXML
    private TableColumn<Rider, Boolean> colAvailable;
    private ObservableList<Rider> riderList;
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available")); 
        loadAllRiders();
    }
    private void loadAllRiders() {
        riderList = FXCollections.observableArrayList(MainGUI.db.getRiders());
        tblRiders.setItems(riderList);
    }
    @FXML
    void handleSearchRider(ActionEvent event) {
        String id = fldSearchId.getText().trim();
        if (id.isEmpty()) return;
        Rider r;
		try {
			r = MainGUI.db.findRiderById(id);
			if (r != null) {
            tblRiders.setItems(FXCollections.observableArrayList(r));
			}
		} catch (ValidationException e) {
			showAlert(Alert.AlertType.INFORMATION, "Not Found", "No rider found with ID: " + id);
            tblRiders.setItems(FXCollections.observableArrayList());
        }
    }
    @FXML
    void handleTopRider(ActionEvent event) {
        Rider topRider = MainGUI.db.getRiderWithMostDeliveries();
        if (topRider != null) {
            tblRiders.setItems(FXCollections.observableArrayList(topRider));
            showAlert(Alert.AlertType.INFORMATION, "Top Rider", 
                    "The rider with the most deliveries is " + topRider.getFirstName() + " (" + topRider.getId() + ")");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Info", "No deliveries have been completed yet.");
        }
    }
    @FXML
    void handleClear(ActionEvent event) {
        fldSearchId.clear();
        loadAllRiders();
    }
    @FXML
    void handleAddRider(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addRider.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Rider");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshTable(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateOrderStatus(ActionEvent event) {
        Rider selectedRider = tblRiders.getSelectionModel().getSelectedItem();
        if (selectedRider == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a rider first.");
            return;
        }

        ArrayList<Order> orders = selectedRider.getAssignedOrders();
        
        if (orders == null || orders.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "This rider has no assigned orders.");
            return;
        }

        Order orderToUpdate = orders.get(0); 

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateOrderStatus.fxml"));
            Parent root = loader.load();
            UpdateOrderStatusController controller = loader.getController();
            controller.initData(orderToUpdate);
            
            Stage stage = new Stage();
            stage.setTitle("Update Status for Order " + orderToUpdate.getCode());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleShowOrders(ActionEvent event) {
        Rider selected = tblRiders.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a rider from the table first.");
            return;
        }
        ArrayList<Order> orders = selected.getAssignedOrders();
        showAlert(Alert.AlertType.INFORMATION, "Rider Orders", "Rider " + selected.getId() + " has " + orders.size() + "assigned orders.");
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
            
        	loadAllRiders();
        	tblRiders.refresh();

        } catch (Exception e) {
            System.err.println("Ошибка при обновлении таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}