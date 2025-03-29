package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ThirdScreenController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
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
