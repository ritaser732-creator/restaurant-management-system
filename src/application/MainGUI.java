package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DeliveryDataBase;
public class MainGUI extends Application {

public static DeliveryDataBase db = new DeliveryDataBase();
static {
	DataInitializer.initData(db);
}
@Override
public void start(Stage primaryStage) throws Exception {

Parent root = FXMLLoader.load(getClass().getResource("/view/welcomeScreen.fxml"));
primaryStage.setTitle("Food Delivery System - Welcome");
primaryStage.setScene(new Scene(root, 400, 400));
primaryStage.show();
    }
public static void main(String[] args) {
launch(args);
    }
}
/*
 * Admin code: Ad0001 
 * RestAdmin codes: Ad0002 - Ad0004 password "Aa!123456",Bb@123456, Cc#123456.
 * Customer codes: C0001 - C0010 
 * Rider codes: Ri0001 - Ri0005 
 * Restaurant codes: Re0001 - Re0010 
 * Orders codes: O0001 - O0003
 */