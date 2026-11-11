package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import model.*;
import restaurant.Restaurant;
import admin.RestAdmin;
import application.MainGUI; 

public class BasicReportsController {
    @FXML private TableView<ReportData> reportTable;
    @FXML private TableColumn<ReportData, String> nameColumn;
    @FXML private TableColumn<ReportData, Integer> ordersColumn;
    @FXML private TableColumn<ReportData, Double> revenueColumn;

    private RestAdmin currentAdmin; 

    public void setAdmin(RestAdmin admin) {
        this.currentAdmin = admin;
        updateTable(); 
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ordersColumn.setCellValueFactory(new PropertyValueFactory<>("orderCount"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
    }

    private void updateTable() {
        if (currentAdmin == null) return;

        var data = FXCollections.<ReportData>observableArrayList();
        var managed = currentAdmin.getManagedRestaurants();
        
        if (managed != null) {
            for (Restaurant r : managed) {
                int count = 0;
                double rev = 0;
                for (Order o : MainGUI.db.getOrders()) {
                    if (o.getRestaurantCode() != null && o.getRestaurantCode().equals(r.getCode())) {
                        count++;
                        rev += o.getFinalPrice();
                    }
                }
                data.add(new ReportData(r.getName(), count, rev));
            }
        }
        reportTable.setItems(data);
    }

    @FXML 
    private void handleClose() {
        reportTable.getScene().getWindow().hide();
    }
}