package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePageScreen.fxml"));
        Parent root = loader.load();

        HomePageController homeController = loader.getController();
        homeController.setPrimaryStage(primaryStage);
        homeController.setWelcomeMessage(role, username);

        primaryStage.setScene(new Scene(root));
    }
}