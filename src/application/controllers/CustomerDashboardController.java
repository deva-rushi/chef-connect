package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import application.utils.SessionManager;

public class CustomerDashboardController {

    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    
    @FXML
    private void handlePlaceOrder(ActionEvent event) {
        try {
            URL location = getClass().getResource("/application/views/SelectChefForOrder.fxml");
            if (location == null) {
                showAlert("Error", "SelectChefForOrder.fxml not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            showAlert("Error", "Failed to load SelectChefForOrder.fxml.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewOrder(ActionEvent event) {
        try {
            URL location = getClass().getResource("/application/views/ViewOrder.fxml");
            if (location == null) {
                showAlert("Error", "ViewOrder.fxml not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (IOException e) {
            showAlert("Error", "Failed to load ViewOrder.fxml.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
    	SessionManager.clearSession();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/LoginScreen.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        controller.setPrimaryStage(stage);
        stage.setScene(new Scene(root, 800, 600));
//        SessionManager.clearSession();
//        try {
//            URL location = getClass().getResource("LoginScreen.fxml");
//            if (location == null) {
//                showAlert("Error", "LoginScreen.fxml not found.");
//                return;
//            }
//
//            FXMLLoader loader = new FXMLLoader(location);
//            Parent root = loader.load();
//            LoginController controller = loader.getController();
//            controller.setPrimaryStage(primaryStage);
//            primaryStage.setScene(new Scene(root, 800, 600));
//        } catch (IOException e) {
//            showAlert("Error", "Failed to load LoginScreen.fxml.");
//            e.printStackTrace();
//        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}