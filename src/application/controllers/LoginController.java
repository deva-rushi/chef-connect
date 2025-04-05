package application.controllers;

import application.data.UserDatabase;
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
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "All fields are required!");
            return;
        }

        // Check if user exists in database
        User user = UserDatabase.getUser(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found!");
            return;
        }

        // Validate password and role
        if (!user.getPassword().equals(password) || !user.getRole().equals(role)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role selection!");
            return;
        }

        // Set user session and navigate to respective dashboard
        SessionManager.setUser(username, role);

        FXMLLoader loader;
        Parent root;
        if ("Chef".equals(role)) {
            loader = new FXMLLoader(getClass().getResource("/application/views/ChefDashboard.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/application/views/CustomerDashboard.fxml"));
        }

        root = loader.load();
        primaryStage.setScene(new Scene(root, 800, 600));
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
}
