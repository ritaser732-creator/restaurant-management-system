package controller;

import application.MainGUI;
import model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCustomerController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtStreet;
    @FXML private TextField txtCity;
    @FXML private TextField txtZipCode;
    @FXML private TextField txtBalance;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    
    @FXML private Label lblError;

    @FXML
    void handleSave(ActionEvent event) {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhoneNumber.getText().trim();
        String street = txtStreet.getText().trim();
        String city = txtCity.getText().trim();
        String zip = txtZipCode.getText().trim();
        String balanceStr = txtBalance.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            lblError.setText("Please fill in all essential fields.");
            return;
        }

        if (balanceStr.isEmpty()) {
            balanceStr = "0.0";
        }

        try {
            double balance = Double.parseDouble(balanceStr);

            
            Customer newCustomer = new Customer(firstName, lastName, email, phone, balance, street, city, zip);

            MainGUI.db.addCustomer(newCustomer);

            utils.DataHandler.saveData(MainGUI.db);

            handleCancel(event);

        } catch (model.ValidationException e) {
            lblError.setText(e.getMessage());
        } catch (NumberFormatException e) {
            lblError.setText("Invalid balance value. Please enter a number.");
        } catch (Exception e) {
            lblError.setText("Error saving customer data.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/manageCustomers.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Customers");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}