package application.controllers;

import application.data.ChefData;
import application.data.UserDatabase;
import application.models.Chef;
import application.models.User;
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
       
        System.out.println("Signup started. Role: " + role); // Debug

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "All fields are required!");
            return;
        }

        if (UserDatabase.userExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "User already exists!");
            return;
        }

        UserDatabase.addUser(new User(username, password, role));

        if (role.equals("Chef")) {
            String cuisine = "Unspecified"; //Default cuisine.
            Chef newChef = new Chef(username, password, cuisine);
            System.out.println("Chef created: " + newChef.getUsername()); // Debug
            ChefData.addChef(newChef);
            System.out.println("Chef added successfully. ChefData map size: " + ChefData.getAllChefs().size()); // Debug
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root, 800, 600));
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