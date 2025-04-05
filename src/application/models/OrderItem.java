//OrderItem.java
package application.models;

public class OrderItem {
    private int orderItemID;
    private int orderID;
    private int menuItemId;
    private int quantity;

    public OrderItem(int orderItemID, int orderID, int menuItemId, int quantity) {
        this.orderItemID = orderItemID;
        this.orderID = orderID;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}