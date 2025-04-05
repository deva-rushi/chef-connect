// CuisineChefSelectionController.java
package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import application.data.ChefData;
import application.models.Chef;

public class CuisineChefSelectionController {

    @FXML private ComboBox<String> cuisineComboBox;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ListView<Chef> chefListView;

    @FXML
    public void initialize() {
        ObservableList<String> cuisines = FXCollections.observableArrayList(ChefData.getAllChefs().stream().map(Chef::getCuisine).distinct().collect(Collectors.toList()));
        cuisineComboBox.setItems(cuisines);

        ObservableList<String> sortOptions = FXCollections.observableArrayList("Rating", "Name", "Cuisine");
        sortComboBox.setItems(sortOptions);

        cuisineComboBox.setOnAction(event -> updateChefList());
        sortComboBox.setOnAction(event -> updateChefList());

        chefListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Chef chef, boolean empty) {
                super.updateItem(chef, empty);
                if (empty || chef == null) {
                    setText(null);
                } else {
                    setText(chef.getUsername() + " (" + chef.getCuisine() + ")");
                }
            }
        });
    }

    private void updateChefList() {
        String selectedCuisine = cuisineComboBox.getValue();
        String selectedSort = sortComboBox.getValue();

        List<Chef> chefs = ChefData.getAllChefs().stream()
                .filter(chef -> selectedCuisine == null || chef.getCuisine().equals(selectedCuisine))
                .collect(Collectors.toList());

        if (selectedSort != null) {
            switch (selectedSort) {
                case "Rating":
                    chefs.sort(Comparator.comparingDouble(Chef::getRating).reversed());
                    break;
                case "Name":
                    chefs.sort(Comparator.comparing(Chef::getUsername));
                    break;
                case "Cuisine":
                    chefs.sort(Comparator.comparing(Chef::getCuisine));
                    break;
            }
        }

        ObservableList<Chef> chefNames = FXCollections.observableArrayList(chefs);
        chefListView.setItems(chefNames);
    }

    @FXML
    private void handleSelectChef(ActionEvent event) throws Exception {
        Chef selectedChef = chefListView.getSelectionModel().getSelectedItem();
        if (selectedChef != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/MenuOrder.fxml"));
            Parent root = loader.load();
            MenuOrderController controller = loader.getController();
            controller.setChef(selectedChef);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CustomerDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
    }
}