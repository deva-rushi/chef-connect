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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Queue;

public class OrdersController {

	@FXML
    private ListView<String> ordersListView;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button updateStatusButton;
    @FXML
    private Label ratingLabel;

    @FXML
    public void initialize() {
        String chefUsername = SessionManager.getUsername();
        ObservableList<String> orderStatuses = FXCollections.observableArrayList();
        Queue<Order> orderQueue = ChefData.getOrdersByChef(chefUsername);
        if (orderQueue != null) {
            for (Order order : orderQueue) {
                orderStatuses.add("Order ID: " + order.getOrderId() + ", Customer: " + order.getCustomerUsername() + ", Status: " + order.getStatus());
            }
        }
        ordersListView.setItems(orderStatuses);

        // Populate statusComboBox
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Pending", "Shipped", "Completed");
        statusComboBox.setItems(statusOptions);

        // Add listener for order selection
        ordersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                statusComboBox.setVisible(true);
                statusComboBox.setManaged(true);
                updateStatusButton.setVisible(true);
                updateStatusButton.setManaged(true);

                // Display rating
                String selectedOrderString = ordersListView.getSelectionModel().getSelectedItem();
                int orderId = Integer.parseInt(selectedOrderString.split("Order ID: ")[1].split(",")[0]);
                Order selectedOrder = null;
                for (Order order : orderQueue) {
                    if (order.getOrderId() == orderId) {
                        selectedOrder = order;
                        break;
                    }
                }

                if (selectedOrder != null && selectedOrder.getStatus().equals("Completed")) {
                    Chef chef = ChefData.getChef(chefUsername);
                    if (chef != null && chef.getRatings().size() > 0) {
                        ratingLabel.setText("Customer Rating: " + chef.getRating());
                        ratingLabel.setVisible(true);
                        ratingLabel.setManaged(true);
                    } else {
                        ratingLabel.setText("No ratings yet.");
                        ratingLabel.setVisible(true);
                        ratingLabel.setManaged(true);
                    }
                } else {
                    ratingLabel.setVisible(false);
                    ratingLabel.setManaged(false);
                }

            } else {
                statusComboBox.setVisible(false);
                statusComboBox.setManaged(false);
                updateStatusButton.setVisible(false);
                updateStatusButton.setManaged(false);
                ratingLabel.setVisible(false);
                ratingLabel.setManaged(false);
            }
        });
    }

    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        String selectedOrderString = ordersListView.getSelectionModel().getSelectedItem();
        if (selectedOrderString != null && statusComboBox.getValue() != null) {
            int orderId = Integer.parseInt(selectedOrderString.split("Order ID: ")[1].split(",")[0]);
            String newStatus = statusComboBox.getValue();
            Queue<Order> orderQueue = ChefData.getOrdersByChef(SessionManager.getUsername());
            if (orderQueue != null) {
                for (Order order : orderQueue) {
                    if (order.getOrderId() == orderId) {
                        order.setStatus(newStatus);
                        // Update the order in ChefData
                        ChefData.updateOrder(order);
                        // Refresh the ListView
                        initialize();
                        break;
                    }
                }
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/ChefDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
    }
}
