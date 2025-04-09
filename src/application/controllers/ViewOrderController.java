package application.controllers;

import application.models.Chef;
import application.models.Order;
import application.data.ChefData;
import application.utils.SessionManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private ObservableList<Order> customerOrders;

    @FXML
    public void initialize() {
        loadOrders();
        ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);  // Populate combobox dropdownfor ratings
        ratingComboBox.setItems(ratings);

        // Add listener to handle rating controls visibility based on selection
        orderListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    Order selectedOrder = getSelectedOrder(newValue);
                    updateRatingControlsVisibility(selectedOrder);  //User is only allowed to rate completed orders
                } else {
                    setRatingControlsInvisible();
                }
            }
        });

        // Initially hide rating controls
        setRatingControlsInvisible();
    }

    private void loadOrders() {
        List<Order> orders = ChefData.getOrdersByCustomer(SessionManager.getUsername());
        customerOrders = FXCollections.observableArrayList(orders);
        ObservableList<String> orderDetails = FXCollections.observableArrayList(customerOrders.stream()
                .map(this::formatOrderDetails)
                .collect(Collectors.toList()));
        orderListView.setItems(orderDetails);
    }

    private void updateRatingControlsVisibility(Order selectedOrder) {
        if (selectedOrder != null && selectedOrder.getStatus().equalsIgnoreCase("completed")) {
            ratingComboBox.setVisible(true);
            ratingComboBox.setManaged(true);
            rateChefButton.setVisible(true);
            rateChefButton.setManaged(true);
        } else {
            setRatingControlsInvisible();
        }
    }

    private void setRatingControlsInvisible() {
        ratingComboBox.setVisible(false);
        ratingComboBox.setManaged(false);
        rateChefButton.setVisible(false);
        rateChefButton.setManaged(false);
        ratingComboBox.setValue(null); //Clear any previously set values
    }

    private Order getSelectedOrder(String orderString) {
        if (customerOrders != null && orderString != null) {
            String[] parts = orderString.split(", ");
            if (parts.length > 0 && parts[0].startsWith("Order ID: ")) {
                try {
                    int orderId = Integer.parseInt(parts[0].substring("Order ID: ".length()));  //First part of order item string contains order id.
                    return customerOrders.stream().filter(order -> order.getOrderId() == orderId).findFirst().orElse(null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String formatOrderDetails(Order order) {
        return "Order ID: " + order.getOrderId() +
               ", Chef: " + order.getChefUsername() +
               ", Status: " + order.getStatus() +
               ", Total: $" + order.getTotalPrice() +
               ", Delivery Address: " + order.getDeliveryAddress();  //Order item string
    }

    @FXML
    private void handleRateChef(ActionEvent event) {
        Order selectedOrder = getSelectedOrder(orderListView.getSelectionModel().getSelectedItem());
        Integer rating = ratingComboBox.getValue();

        if (selectedOrder != null && rating != null && selectedOrder.getStatus().equalsIgnoreCase("completed")) {
            Chef chef = ChefData.getChef(selectedOrder.getChefUsername());
            if (chef != null) {
                chef.addRating(rating);
                ChefData.updateChef(chef);  //Update rating for chef
                System.out.println("Chef rated successfully for Order ID: " + selectedOrder.getOrderId() + " with rating: " + rating);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rating Successful");
                alert.setHeaderText(null);
                alert.setContentText("You have rated " + chef.getUsername() + " with a rating of " + rating + "!");
                alert.showAndWait();
            }
        } else if (selectedOrder != null && !selectedOrder.getStatus().equalsIgnoreCase("completed")) {
            new Alert(Alert.AlertType.WARNING, "You can only rate completed orders.").showAndWait();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a completed order and a rating.").showAndWait();
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