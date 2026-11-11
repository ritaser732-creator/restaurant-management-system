package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import model.Customer;
import model.ValidationException;

public class UpdateDetailsController {
    
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField zipCodeField;
    @FXML private Label messageLabel;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhoneNumber());
        streetField.setText(customer.getStreet());
        cityField.setText(customer.getCity());
        zipCodeField.setText(customer.getZipCode());
    }

    @FXML
    private void handleSave() {
        try { 
        	customer.setFirstName(firstNameField.getText());
        	customer.setLastName(lastNameField.getText());
        	customer.setEmail(emailField.getText());
        	customer.setPhoneNumber(phoneField.getText());
        	customer.setCity(cityField.getText());
        	customer.setStreet(streetField.getText());
        	customer.setZipCode(zipCodeField.getText());
        	messageLabel.setText("Данные успешно обновлены!");
		} catch (ValidationException e) {
			e.printStackTrace();
		}
        
    }
}