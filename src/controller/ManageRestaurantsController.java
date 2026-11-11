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
import model.ValidationException;
import application.MainGUI;
import restaurant.Restaurant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
public class ManageRestaurantsController {
    @FXML
    private TextField fldSearchCode;
    @FXML
    private TextField fldSearchType;
    @FXML
    private TableView<Restaurant> tblRestaurants;
    @FXML
    private TableColumn<Restaurant, String> colCode;
    @FXML
    private TableColumn<Restaurant, String> colName;
    @FXML
    private TableColumn<Restaurant, String> colType;
    @FXML
    private TableColumn<Restaurant, Double> colRating;
    @FXML
    private TableColumn<Restaurant, Boolean> colIsOpen;
    @FXML
    private TableColumn<Restaurant, Double> colDeliveryFee;
    private ObservableList<Restaurant> restaurantList;
    @FXML
    public void initialize() {
        
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colIsOpen.setCellValueFactory(new PropertyValueFactory<>("open")); 
        colDeliveryFee.setCellValueFactory(new PropertyValueFactory<>("deliveryFee"));
        loadAllRestaurants();
    }
    private void loadAllRestaurants() {
        restaurantList = FXCollections.observableArrayList(MainGUI.db.getRestaurants());
        tblRestaurants.setItems(restaurantList);
    }
    @FXML
    void handleSearchCode(ActionEvent event) {
        String code = fldSearchCode.getText().trim();
        if (code.isEmpty()) return;
        Restaurant r;
		try {
			r = MainGUI.db.findRestaurantByCode(code);
			if (r != null) {
            tblRestaurants.setItems(FXCollections.observableArrayList(r));
        }
		} catch (ValidationException e) {
			showAlert(Alert.AlertType.INFORMATION, "Not Found", "No restaurant found with code: " + code);
            tblRestaurants.setItems(FXCollections.observableArrayList());
        }
    }
    @FXML
    void handleSearchType(ActionEvent event) {
        String type = fldSearchType.getText().trim().toLowerCase();
        if (type.isEmpty()) return;
        ArrayList<Restaurant> filtered = (ArrayList<Restaurant>) MainGUI.db.getRestaurants().stream()
                .filter(r -> r.getType().toLowerCase().equals(type))
                .collect(Collectors.toList());
        
        tblRestaurants.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    void handleShowOpenOnly(ActionEvent event) {
        ArrayList<Restaurant> filtered = (ArrayList<Restaurant>) MainGUI.db.getRestaurants().stream()
                .filter(Restaurant::isOpen)
                .collect(Collectors.toList());
        
        tblRestaurants.setItems(FXCollections.observableArrayList(filtered));
    }
    @FXML
    void handleClearFilters(ActionEvent event) {
        fldSearchCode.clear();
        fldSearchType.clear();
        loadAllRestaurants();
    }
    @FXML
    void handleAddRestaurant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addRestaurant.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Restaurant");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateRating(ActionEvent event) {
        Restaurant selected = tblRestaurants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a restaurant first.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateRating.fxml"));
            Parent root = loader.load();
            UpdateRatingController controller = loader.getController();
            controller.initData(selected);
            Stage stage = new Stage();
            stage.setTitle("Update Rating for " + selected.getName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handleToggleStatus(ActionEvent event) {
        Restaurant selected = tblRestaurants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a restaurant from the table first.");
            return;
        }
        
        boolean currentStatus = selected.isOpen();
        selected.setOpen(!currentStatus);
        
        tblRestaurants.refresh(); 
        showAlert(Alert.AlertType.INFORMATION, "Status Updated", selected.getName() + " is now " + (selected.isOpen() ? "Open" : "Close") +"."); 
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
            
        	loadAllRestaurants();
        	tblRestaurants.refresh();

        } catch (Exception e) {
            System.err.println("Ошибка при обновлении таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
