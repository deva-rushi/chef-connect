// BrowseChefsController.java
package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.stream.Collectors;

import application.data.ChefData;
import application.models.Chef;

public class BrowseChefsController {

    @FXML
    private ComboBox<String> cuisineComboBox;

    @FXML
    private ListView<Chef> chefsListView;

    @FXML
    public void initialize() {
        // Populate cuisine ComboBox (get unique cuisines from ChefData)
        ObservableList<String> cuisines = FXCollections.observableArrayList(
                ChefData.getAllChefs().stream()
                        .map(Chef::getCuisine)
                        .distinct()
                        .collect(Collectors.toList())
        );
        cuisineComboBox.setItems(cuisines);

        // Populate chefsListView (initially show all chefs)
        ObservableList<Chef> allChefs = FXCollections.observableArrayList(ChefData.getAllChefs());
        chefsListView.setItems(allChefs);

        // Add listener to cuisine ComboBox for filtering
        cuisineComboBox.setOnAction(event -> filterChefs());
    }

    private void filterChefs() {
        String selectedCuisine = cuisineComboBox.getValue();
        ObservableList<Chef> filteredChefs;

        if (selectedCuisine == null || selectedCuisine.isEmpty()) {
            filteredChefs = FXCollections.observableArrayList(ChefData.getAllChefs());
        } else {
            filteredChefs = FXCollections.observableArrayList(
                    ChefData.getAllChefs().stream()
                            .filter(chef -> chef.getCuisine().equals(selectedCuisine))
                            .collect(Collectors.toList())
            );
        }
        chefsListView.setItems(filteredChefs);
    }

    @FXML
    private void handleSelectChef(ActionEvent event) throws Exception {
        // Get selected chef and navigate to their menu screen
        Chef selectedChef = chefsListView.getSelectionModel().getSelectedItem();
        if (selectedChef != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/ChefMenu.fxml")); // Assuming ChefMenu.fxml
            Parent root = loader.load();
            // Pass chef data to ChefMenuController (not shown here)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        // Navigate back to CustomerDashboard
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CustomerDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}