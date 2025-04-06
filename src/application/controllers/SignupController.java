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

        if (!isValidUsername(username)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Username must be 3-20 characters long and contain only letters, numbers, or underscores.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "Password must be at least 8 characters long and contain at least one letter, one number, and one special character.");
            return;
        }

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "All fields are required!");
            return;
        }

        if (UserDatabase.userExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "User already exists!");
            return;
        }

        if (role.equals("Chef")) {
            String cuisine = "Unspecified";
            Chef newChef = new Chef(username, password, cuisine);
            UserDatabase.addUser(newChef);
            ChefData.addChef(newChef);
            System.out.println("Chef created: " + newChef.getUsername()); // Debug
            System.out.println("Chef added successfully. ChefData map size: " + ChefData.getAllChefs().size()); // Debug
        } else if (role.equals("Customer")) {
            Customer newCustomer = new Customer(username, password, "");
            UserDatabase.addUser(newCustomer);
            System.out.println("Customer created: " + newCustomer.getUsername());
        }

        showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Signup completed successfully!");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws Exception {
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
}