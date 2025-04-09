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

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import application.data.ChefData;
import application.models.Chef;

public class SelectChefForOrderController {

	@FXML
    private ListView<Chef> chefsListView;
    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    public void initialize() {
        if (sortComboBox == null) {
            System.err.println("sortComboBox is null!");
        }
        // Populate sort options
        sortComboBox.getItems().addAll("Rating (High to Low)", "Rating (Low to High)");
        sortComboBox.setValue("Rating (High to Low)"); // Default sorting for chefs

        loadChefs();

        chefsListView.setCellFactory(listView -> new ListCell<>() {  // Customize the display of Chef objects in the ListView
            @Override
            protected void updateItem(Chef chef, boolean empty) {  //Dynamically update list of chefs whenever data changes
                super.updateItem(chef, empty);
                if (empty || chef == null) {
                    setText(null);
                } else {
                    setText(chef.getUsername() + " (Rating: " + String.format("%.2f", chef.getRating()) + ")"); // Display the chef's username and rating
                }
            }
        });

        
        sortComboBox.setOnAction(this::handleSortChange);  // Add listener for sort selection
    }
    private void loadChefs() {  //Load chef data sorted according sort combo box value.
        List<Chef> sortedChefs = ChefData.getAllChefs().stream()
                .sorted(getComparator())
                .collect(Collectors.toList());

        ObservableList<Chef> chefs = FXCollections.observableArrayList(sortedChefs);
        chefsListView.setItems(chefs);
    }

    private Comparator<Chef> getComparator() {  //Use comparator to compare chef ratings for sorting
        if (sortComboBox.getValue().equals("Rating (High to Low)")) {
            return Comparator.comparingDouble(Chef::getRating).reversed();
        } else {
            return Comparator.comparingDouble(Chef::getRating);
        }
    }

    @FXML
    private void handleSortChange(ActionEvent event) {
        loadChefs();
    }

    @FXML
    private void handleSelectChef(ActionEvent event) {
        try {
            Chef selectedChef = chefsListView.getSelectionModel().getSelectedItem();
            if (selectedChef != null) {
                URL location = getClass().getResource("/application/views/MenuOrder.fxml");
                System.out.println("FXML location: " + location);

                if (location == null) {
                    System.err.println("MenuOrder.fxml not found!");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(location);
                Parent root = loader.load();
                MenuOrderController controller = loader.getController();
                controller.setChef(selectedChef);

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
            }
        } catch (IOException e) {
            e.printStackTrace();
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