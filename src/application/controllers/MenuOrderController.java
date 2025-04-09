package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import application.data.ChefData;
import application.models.Chef;
import application.models.MenuItem;
import application.models.Order;
import application.models.OrderItem;
import application.utils.SessionManager;

public class MenuOrderController {

    @FXML private Label chefNameLabel;
    @FXML private ListView<String> menuListView;
    @FXML private ListView<String> orderListView;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Chef chef;

    public void setChef(Chef chef) {
        this.chef = chef;
        this.chefNameLabel.setText("Menu of " + chef.getUsername());
        loadMenu();
    }

    private void loadMenu() {
        if (chef != null) {
            List<MenuItem> menuItems = ChefData.getMenuItemsByChef(chef);
            ObservableList<String> menuItemNames = FXCollections.observableArrayList(menuItems.stream()  //Stream is used to get an iterable(called stream) for the collection which you can apply map, filter etc. to. So kind of how in Python the map, filter etc functions are applied to iterables
                    .map(item -> item.getItemName() + " - $" + item.getPrice()) // Add price to display
                    .collect(Collectors.toList()));  //Collect the modified stream iterable and gather them into thelist
            menuListView.setItems(menuItemNames);
        }
    }

    @FXML
    private void handleAddToOrder(ActionEvent event) {
        String selectedItemName = menuListView.getSelectionModel().getSelectedItem();
        if (selectedItemName != null) {
            String itemName = selectedItemName.split(" - ")[0]; // Extract item name
            MenuItem selectedItem = ChefData.getMenuItemsByChef(chef).stream().filter(item -> item.getItemName().equals(itemName)).findFirst().orElse(null);
            if (selectedItem != null) {
                orderItems.add(new OrderItem(0, 0, selectedItem.getItemID(), 1));
                orderListView.getItems().add(selectedItem.getItemName());
            }
        }
    }

    @FXML
    private void handlePlaceOrder(ActionEvent event) throws Exception {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delivery Address");
        dialog.setHeaderText("Enter Delivery Address:");
        dialog.setContentText("Address:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String address = result.get();
            Order order = new Order(SessionManager.getUsername(), chef.getUsername(), LocalDateTime.now(), "Pending", orderItems, calculateTotalPrice(), address);
            ChefData.addOrder(order);

            // Order Confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Order placed successfully. Your Order ID is: " + order.getOrderId());
            alert.showAndWait();

            orderItems.clear();
            orderListView.getItems().clear();

            // Return to CustomerDashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/CustomerDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        }
    }

    private double calculateTotalPrice() {
        double total = 0.0;
        for (OrderItem orderItem : orderItems) {
            MenuItem menuItem = ChefData.getMenuItemsByChef(chef).stream().filter(item -> item.getItemID() == orderItem.getMenuItemId()).findFirst().orElse(null);
            if (menuItem != null) {
                total += menuItem.getPrice() * orderItem.getQuantity();
            }
        }
        return total;
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/SelectChefForOrder.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
    }
}