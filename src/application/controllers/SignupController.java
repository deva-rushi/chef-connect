package application.controllers;

import application.data.ChefData;
import application.data.UserDatabase;
import application.models.Chef;
import application.models.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("Chef", "Customer");
    }

    @FXML
    private void handleSignup(ActionEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        System.out.println("Signup started. Role: " + role);

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "All fields are required!");
            return;
        }

        if (UserDatabase.userExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "User already exists!");
            return;
        }

        if (!isValidUsername(username)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Username must be 3-20 characters, alphanumeric with underscores.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Password must be at least 8 characters, containing letters, numbers, and symbols.");
            return;
        }

        if (role.equals("Chef")) {
            String cuisine = "Unspecified"; // Default cuisine.
            Chef newChef = new Chef(username, password, cuisine);
            UserDatabase.addUser(newChef);
            ChefData.addChef(newChef);
            System.out.println("Chef created: " + newChef.getUsername());
            System.out.println("Chef added successfully. ChefData map size: " + ChefData.getAllChefs().size());
        } else if (role.equals("Customer")) {
            Customer newCustomer = new Customer(username, password, "");
            UserDatabase.addUser(newCustomer);
            System.out.println("Customer created: " + newCustomer.getUsername());
        }

        showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "User created successfully!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);  //Load back login screen after signup
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws Exception {  //for back button to go back to login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";  //Regex to check for username
        return Pattern.matches(usernameRegex, username);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";  //Regex to match password. Password has to have at least 8 characters and have no whitespace, have at least one digit, uppercase letter, lowercase letter and one special character from @#$%^&+=!
        return Pattern.matches(passwordRegex, password);
    }
}