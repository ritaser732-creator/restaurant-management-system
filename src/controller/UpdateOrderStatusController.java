package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Order;
import model.OrderStatus;
import model.ValidationException; 

public class UpdateOrderStatusController {
    @FXML private ComboBox<OrderStatus> comboStatus; 
    @FXML private Label lblOrderId;
    private Order selectedOrder;

    @FXML
    public void initialize() {
       
        comboStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));
    }

    public void initData(Order order) {
        this.selectedOrder = order;
        lblOrderId.setText("Order: " + order.getCode());
        comboStatus.setValue(order.getStatus());
    }

    @FXML
    void handleUpdate() {
        OrderStatus newStatus = comboStatus.getValue();
        if (newStatus != null && selectedOrder != null) {
            try {
				selectedOrder.setStatus(newStatus);
				Stage stage = (Stage) lblOrderId.getScene().getWindow();
            stage.close();
			} catch (ValidationException e) {
				e.printStackTrace();
			} 
            
        }
    }
}