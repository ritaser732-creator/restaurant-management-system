package utils;
import admin.RestAdmin;
import javafx.scene.control.Alert;
import model.Customer;
import model.DeliveryDataBase;
import model.Order;
import model.Rider;
import model.ValidationException;
import restaurant.FastFoodRestaurant;
import restaurant.PremiumRestaurant;
import restaurant.Restaurant;
import java.io.*;
public class DataHandler {
    private static final String DIR = "src/textFiles/"; 
    public static void saveData(DeliveryDataBase db) throws IOException {
        saveCustomers(db);
        saveRestaurants(db);
        saveRiders(db);
        saveRestAdmins(db);
      
    }
    public static void loadData(DeliveryDataBase db) throws IOException {
        
        loadCustomers(db);
        loadRestaurants(db);
        loadRiders(db);
        loadRestAdmins(db);
    }
    private static void saveCustomers(DeliveryDataBase db) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIR + "customers.txt"))) {
            for (Customer c : db.getCustomers()) {
                String line = String.join(",", 
                        c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhoneNumber(),
                        String.valueOf(c.getBalance()), c.getStreet(), c.getCity(), c.getZipCode());
                bw.write(line);
                bw.newLine();
            }
        }
    }
    private static void loadCustomers(DeliveryDataBase db) throws IOException {
        File file = new File(DIR + "customers.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    Customer c = new Customer(parts[0], parts[1], parts[2], parts[3], 
                            Double.parseDouble(parts[4]), parts[5], parts[6], parts[7]);
                    try {
						db.addCustomer(c);
					} catch (ValidationException e) {
						showAlert(Alert.AlertType.ERROR, "Error", "");
                }
            }
        }
    }
  }
    private static void saveRestaurants(DeliveryDataBase db) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIR + "restaurants.txt"))) {
            for (Restaurant r : db.getRestaurants()) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.getType()).append(",")
                  .append(r.getName()).append(",")
                  .append(r.getRating()).append(",")
                  .append(r.isOpen()).append(",")
                  .append(r.getDeliveryFee());
                if (r instanceof FastFoodRestaurant) {
                    FastFoodRestaurant ffr = (FastFoodRestaurant) r;
                    sb.append(",").append(ffr.getAveragePreparationTime())
                      .append(",").append(ffr.getExtraCharges());
                } else if (r instanceof PremiumRestaurant) {
                    PremiumRestaurant pr = (PremiumRestaurant) r;
                    sb.append(",").append(pr.getMinimumOrderAmount())
                      .append(",").append(pr.getAdditionalCommissionRate());
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }
    private static void loadRestaurants(DeliveryDataBase db) throws IOException {
        File file = new File(DIR + "restaurants.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                String name = parts[1];
                double rating = Double.parseDouble(parts[2]);
                boolean isOpen = Boolean.parseBoolean(parts[3]);
                double fee = Double.parseDouble(parts[4]);
                Restaurant r = null;
                if (type.equalsIgnoreCase("fast") && parts.length == 7) {
                    r = new FastFoodRestaurant(name, type, rating, isOpen, fee, 
                            Integer.parseInt(parts[5]), Double.parseDouble(parts[6]));
                } else if (type.equalsIgnoreCase("premium") && parts.length == 7) {
                    r = new PremiumRestaurant(name, type, rating, isOpen, fee, 
                            Double.parseDouble(parts[5]), Double.parseDouble(parts[6]));
                } else if (parts.length >= 5) {
                    r = new Restaurant(name, type, rating, isOpen, fee);
                }
                if (r != null)
					try {
						db.addRestaurant(r);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
        }
    }
    private static void saveRiders(DeliveryDataBase db) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIR + "riders.txt"))) {
            for (Rider r : db.getRiders()) {
                String line = String.join(",", 
                        r.getFirstName(), r.getLastName(), r.getPhoneNumber(), r.getVehicle());
                bw.write(line);
                bw.newLine();
            }
        }
    }
    private static void loadRiders(DeliveryDataBase db) throws IOException {
        File file = new File(DIR + "riders.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Rider r = new Rider(parts[0], parts[1], parts[2], parts[3]);
                    try {
						db.addRider(r);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
    }
    private static void saveRestAdmins(DeliveryDataBase db) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIR + "restAdmins.txt"))) {
            for (RestAdmin ra : db.getRestAdmins()) {
                String line = String.join(",", 
                        ra.getFirstName(), ra.getUsername(), ra.getPassword());
                bw.write(line);
                bw.newLine();
            }
        }
    }
    private static void loadRestAdmins(DeliveryDataBase db) throws IOException {
        File file = new File(DIR + "restAdmins.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    RestAdmin ra = new RestAdmin(parts[0], parts[1], parts[2]);
                    try {
						db.addRestAdmin(ra);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
    }
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}