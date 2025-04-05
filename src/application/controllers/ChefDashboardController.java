// ChefDashboardController.java (MODIFIED)
package application.controllers;

import application.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChefDashboardController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void handleAddMenuItem(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/AddMenuItem.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600)); // Set size
    }

    @FXML
    private void handleViewOrders(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/Orders.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600)); // Set size
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        SessionManager.clearSession();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        controller.setPrimaryStage(stage);
        stage.setScene(new Scene(root, 800, 600)); // Set size
    }
}