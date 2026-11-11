package controller;

import model.Customer;
import application.MainGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UpdateCustomerController {
    
    private Customer currCustomer;

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

    public void initData(Customer customer) {
        this.currCustomer = customer;
        
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtEmail.setText(customer.getEmail());
        txtPhoneNumber.setText(customer.getPhoneNumber());
        
        txtStreet.setText(customer.getStreet());
        txtCity.setText(customer.getCity());
        txtZipCode.setText(customer.getZipCode());
        
        txtBalance.setText(String.valueOf(customer.getBalance()));
    }
    @FXML
    private Label lblError; 

    @FXML
    void handleSave(ActionEvent event) {
        if (currCustomer == null) return;

        
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhoneNumber.getText().trim();
        String street = txtStreet.getText().trim();
        String city = txtCity.getText().trim();
        String zip = txtZipCode.getText().trim();
        String balanceStr = txtBalance.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || balanceStr.isEmpty()) {
            lblError.setText("Please fill in essential fields.");
            return;
        }

        try {
            currCustomer.setFirstName(firstName);
            currCustomer.setLastName(lastName);
            currCustomer.setEmail(email);
            currCustomer.setPhoneNumber(phone);
            currCustomer.setStreet(street);
            currCustomer.setCity(city);
            currCustomer.setZipCode(zip);
            currCustomer.setBalance(Double.parseDouble(balanceStr));

            utils.DataHandler.saveData(MainGUI.db);

            handleCancel(event); 
            
        } catch (NumberFormatException e) {
            lblError.setText("Invalid balance value.");
        } catch (Exception e) {
            lblError.setText("Error saving data.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
       
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/manageCustomers.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Manage Customers");
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}