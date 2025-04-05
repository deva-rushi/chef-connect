package application.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private int orderId;
    private String customerUsername;
    private String chefUsername;
    private LocalDateTime orderDateTime;
    private String status;
    private List<OrderItem> orderItems;
    private double totalPrice;
    private String deliveryAddress;

    public Order(int orderId, String customerUsername, String chefUsername, LocalDateTime orderDateTime, String status, List<OrderItem> orderItems, double totalPrice, String deliveryAddress) {
        this.orderId = orderId;
        this.customerUsername = customerUsername;
        this.chefUsername = chefUsername;
        this.orderDateTime = orderDateTime;
        setStatus(status); // Use setter for validation
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
    }

    public Order(String customerUsername, String chefUsername, LocalDateTime orderDateTime, String status, List<OrderItem> orderItems, double totalPrice, String deliveryAddress) {
        this.customerUsername = customerUsername;
        this.chefUsername = chefUsername;
        this.orderDateTime = orderDateTime;
        setStatus(status); // Use setter for validation
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getChefUsername() {
        return chefUsername;
    }

    public void setChefUsername(String chefUsername) {
        this.chefUsername = chefUsername;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("Pending") && !status.equals("Shipped") && !status.equals("Completed")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}