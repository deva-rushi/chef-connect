package application.controllers;

import application.models.Chef;
import application.models.Order;
import application.data.ChefData;
import application.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ViewOrderController {

    @FXML
    private ListView<String> orderListView;
    @FXML
    private ComboBox<Integer> ratingComboBox;
    @FXML
    private Button rateChefButton;

    @FXML
    public void initialize() {
        loadOrders();
        // Populate ratingComboBox
        ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ratingComboBox.setItems(ratings);
    }

    private void loadOrders() {
        List<Order> orders = ChefData.getOrdersByCustomer(SessionManager.getUsername());
        if (orders != null) {
            ObservableList<String> orderDetails = FXCollections.observableArrayList(orders.stream()
                    .map(this::formatOrderDetails)
                    .collect(Collectors.toList()));
            orderListView.setItems(orderDetails);

            // Make the rating input visible only for completed orders
            boolean completedOrderFound = orders.stream()
                    .anyMatch(order -> order.getStatus().equalsIgnoreCase("completed"));

            ratingComboBox.setVisible(completedOrderFound);
            ratingComboBox.setManaged(completedOrderFound);
            rateChefButton.setVisible(completedOrderFound);
            rateChefButton.setManaged(completedOrderFound);
        }
    }

    private String formatOrderDetails(Order order) {
        return "Order ID: " + order.getOrderId() +
                ", Chef: " + order.getChefUsername() +
                ", Status: " + order.getStatus() +
                ", Total: $" + order.getTotalPrice() +
                ", Delivery Address: " + order.getDeliveryAddress();
    }

    @FXML
    private void handleRateChef(ActionEvent event) {
        Order selectedOrder = orderListView.getSelectionModel().getSelectedItem() != null ?
                             ChefData.getOrdersByCustomer(SessionManager.getUsername()).stream()
                             .filter(order -> formatOrderDetails(order).equals(orderListView.getSelectionModel().getSelectedItem()))
                             .findFirst()
                             .orElse(null) : null;

        if (selectedOrder != null && ratingComboBox.getValue() != null) {
            int rating = ratingComboBox.getValue();
            Chef chef = ChefData.getChef(selectedOrder.getChefUsername());
            if (chef != null) {
                chef.addRating(rating);
                ChefData.updateChef(chef);
                // Optionally provide feedback to the user
                System.out.println("Chef rated successfully!");

                // Remove the rated order from the list and refresh the list
                List<Order> orders = ChefData.getOrdersByCustomer(SessionManager.getUsername());
                orders.removeIf(order -> order.getOrderId() == selectedOrder.getOrderId());
                ObservableList<String> orderDetails = FXCollections.observableArrayList(orders.stream()
                        .map(this::formatOrderDetails)
                        .collect(Collectors.toList()));
                orderListView.setItems(orderDetails);

                // Check if there are any completed orders remaining.
                boolean completedOrderFound = orders.stream()
                        .anyMatch(order -> order.getStatus().equalsIgnoreCase("completed"));

                ratingComboBox.setVisible(completedOrderFound);
                ratingComboBox.setManaged(completedOrderFound);
                rateChefButton.setVisible(completedOrderFound);
                rateChefButton.setManaged(completedOrderFound);

            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            URL location = getClass().getResource("/application/views/CustomerDashboard.fxml");
            if (location == null) {
                System.err.println("CustomerDashboard.fxml not found!");
                return;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}