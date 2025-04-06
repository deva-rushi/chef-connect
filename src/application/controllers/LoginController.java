package application.controllers;

import application.data.UserDatabase;
import application.models.Chef;
import application.models.Customer;
import application.models.User;
import application.utils.SessionManager;
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

public class LoginController {
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
    private void handleLogin(ActionEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "All fields are required!");
            return;
        }

        if (!isValidUsername(username)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username must be 3-20 characters long and contain only letters, numbers, or underscores.");
            return;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Password must be at least 8 characters long and contain at least one letter, one number, and one special character.");
            return;
        }

        User user = UserDatabase.getUser(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found!");
            return;
        }

        if (!user.getRole().equals(selectedRole)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect role selected!");
            return;
        }

        if (selectedRole.equals("Chef")) {
            Chef chef = UserDatabase.getChef(username);
            if (chef != null && chef.checkPassword(password)) {
                SessionManager.setUser(username, "Chef");
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Login completed successfully!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/ChefDashboard.fxml"));
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root, 800, 600));
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role selection!");
            }
        } else if (selectedRole.equals("Customer")) {
            Customer customer = UserDatabase.getCustomer(username);
            if (customer != null && customer.checkPassword(password)) {
                SessionManager.setUser(username, "Customer");
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Login completed successfully!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CustomerDashboard.fxml"));
                Parent root = loader.load();
                primaryStage.setScene(new Scene(root, 800, 600));
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role selection!");
            }
        }
    }

    /**
     * Utility method to show alert messages.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSignup(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/SignupScreen.fxml"));
        Parent root = loader.load();
        SignupController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }
}