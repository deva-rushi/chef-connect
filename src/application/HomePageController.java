package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomePageController {
    @FXML private Label welcomeLabel;
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setWelcomeMessage(String role, String name) {
        welcomeLabel.setText("Welcome " + role + " " + name);
    }

    @FXML
    private void handleGoToThird(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ThirdScreen.fxml"));
        Parent root = loader.load();

        ThirdScreenController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(new Scene(root));
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(new Scene(root));
    }
}