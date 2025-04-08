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
import javafx.scene.control.Alert;
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
    private Label overallRatingLabel;

    private Queue<Order> currentOrderQueue; // Store the order queue for easier access

    @FXML
    public void initialize() {
        String chefUsername = SessionManager.getUsername();
        ObservableList<String> orderStatuses = FXCollections.observableArrayList();
        currentOrderQueue = ChefData.getOrdersByChef(chefUsername);
        if (currentOrderQueue != null) {
            for (Order order : currentOrderQueue) {
                orderStatuses.add(formatOrderDisplay(order));
            }
        }
        ordersListView.setItems(orderStatuses);

        // Populate statusComboBox options
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Pending", "Shipped", "Completed");
        statusComboBox.setItems(statusOptions);
        statusComboBox.setVisible(false);
        statusComboBox.setManaged(false);
        updateStatusButton.setVisible(false);
        updateStatusButton.setManaged(false);

        // Display overall rating
        Chef chef = ChefData.getChef(chefUsername);
        if (chef != null && !chef.getRatings().isEmpty()) {
            double averageRating = chef.getRating();
            overallRatingLabel.setText("Overall Customer Rating: " + String.format("%.2f", averageRating));
            overallRatingLabel.setVisible(true);
            overallRatingLabel.setManaged(true);
        } else {
            overallRatingLabel.setText("Overall Rating: No ratings yet.");
            overallRatingLabel.setVisible(true);
            overallRatingLabel.setManaged(true);
        }

        // Add listener for order selection to enable update controls and set default status
        ordersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                statusComboBox.setVisible(true);
                statusComboBox.setManaged(true);
                updateStatusButton.setVisible(true);
                updateStatusButton.setManaged(true);

                // Set the default value of the ComboBox to the current status
                Order selectedOrder = getSelectedOrderFromDisplay(newSelection);
                if (selectedOrder != null) {
                    statusComboBox.setValue(selectedOrder.getStatus());
                }
            } else {
                statusComboBox.setVisible(false);
                statusComboBox.setManaged(false);
                updateStatusButton.setVisible(false);
                updateStatusButton.setManaged(false);
                statusComboBox.setValue(null); // Clear selection
                overallRatingLabel.setVisible(chef != null && !chef.getRatings().isEmpty());
                overallRatingLabel.setManaged(chef != null && !chef.getRatings().isEmpty());
            }
        });
    }

    private String formatOrderDisplay(Order order) {
        return "Order ID: " + order.getOrderId() + ", Customer: " + order.getCustomerUsername() + ", Status: " + order.getStatus();
    }

    private Order getSelectedOrderFromDisplay(String displayString) {
        if (displayString != null && currentOrderQueue != null) {
            try {
                int orderId = Integer.parseInt(displayString.split("Order ID: ")[1].split(",")[0].trim());
                for (Order order : currentOrderQueue) {
                    if (order.getOrderId() == orderId) {
                        return order;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        String selectedOrderString = ordersListView.getSelectionModel().getSelectedItem();
        String newStatus = statusComboBox.getValue();
        if (selectedOrderString != null && newStatus != null && currentOrderQueue != null) {
            Order selectedOrder = getSelectedOrderFromDisplay(selectedOrderString);
            if (selectedOrder != null) {
                String currentStatus = selectedOrder.getStatus();

                // Prevent invalid status transitions
                if (currentStatus.equals("Shipped") && newStatus.equals("Pending")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Status Update", "Cannot change a shipped order back to pending.");
                    return;
                }
                if (currentStatus.equals("Completed") && (newStatus.equals("Pending") || newStatus.equals("Shipped"))) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Status Update", "Cannot change a completed order back to pending or shipped.");
                    return;
                }

                try {
                    int orderId = selectedOrder.getOrderId();
                    for (Order order : currentOrderQueue) {
                        if (order.getOrderId() == orderId) {
                            order.setStatus(newStatus);
                            ChefData.updateOrder(order);
                            // Update the display in the ListView without a full refresh
                            int selectedIndex = ordersListView.getSelectionModel().getSelectedIndex();
                            if (selectedIndex != -1) {
                                ordersListView.getItems().set(selectedIndex, formatOrderDisplay(order));
                            }
                            // Show success alert
                            showAlert(Alert.AlertType.INFORMATION, "Status Updated", "Order ID " + orderId + " status updated to " + newStatus + ".");
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}