package application.controllers;

import application.data.ChefData;
import application.models.Chef;
import application.models.MenuItem;
import application.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMenuItemController {

    @FXML private TextField itemNameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;

    @FXML
    private void handleAddItem(ActionEvent event) {
        String itemName = itemNameField.getText();
        String description = descriptionField.getText();
        double price;

        try {
            price = Double.parseDouble(priceField.getText());
            String chefUsername = SessionManager.getUsername();
            System.out.println("AddMenuItemController: Chef username from session: " + chefUsername);
            Chef chef = ChefData.getChef(chefUsername);

            if (chef != null) {
                MenuItem newItem = new MenuItem(itemName, description, price, chefUsername);
                ChefData.addMenuItem(chef, newItem);

                itemNameField.clear();
                descriptionField.clear();
                priceField.clear();

                // Show a success alert
                showAlert(Alert.AlertType.INFORMATION, "Item Added", itemName + " has been added to your menu.");

            } else {
                showAlert(Alert.AlertType.ERROR, "Chef Not Found", "Chef with username " + chefUsername + " not found.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid number for the price.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/ChefDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}