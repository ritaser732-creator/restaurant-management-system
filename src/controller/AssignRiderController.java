package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import model.*;
import application.MainGUI;

public class AssignRiderController {
    @FXML private TextField orderCodeField; 
    @FXML private ComboBox<Rider> riderComboBox;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        riderComboBox.setItems(FXCollections.observableArrayList(MainGUI.db.getRiders()));
    }

    @FXML
    private void handleAssign() {
        String orderCode = orderCodeField.getText().trim();
        Rider selectedRider = riderComboBox.getValue();
        
        if (orderCode.isEmpty() || selectedRider == null) {
            statusLabel.setText("Please enter order code and select a rider!");
            return;
        }

        Order order;
		try {
			order = MainGUI.db.findOrderByCode(orderCode);
			if (order != null) {
            try {
				MainGUI.db.assignRiderToOrder( selectedRider.getId(), order.getCode()); 
				statusLabel.setText("Rider " + selectedRider.getFirstName() + " assigned to " + orderCode);
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        } else {
            statusLabel.setText("Order not found!");
        }
		} catch (ValidationException e) {
			e.printStackTrace();
		} 

        
    }
}