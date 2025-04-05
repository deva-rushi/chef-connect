package application.models;

public class MenuItem {
    private int itemID;
    private String itemName;
    private String description;
    private double price;
    private String chefUsername;

    public MenuItem(int itemID, String itemName, String description, double price, String chefUsername) {
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (chefUsername == null || chefUsername.isEmpty()) {
            throw new IllegalArgumentException("Chef username cannot be null or empty.");
        }
        this.itemID = itemID;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.chefUsername = chefUsername;
    }

    public MenuItem(String itemName, String description, double price, String chefUsername) {
        if (itemName == null || itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (chefUsername == null || chefUsername.isEmpty()) {
            throw new IllegalArgumentException("Chef username cannot be null or empty.");
        }
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.chefUsername = chefUsername;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getChefUsername() {
        return chefUsername;
    }

    public void setChefUsername(String chefUsername) {
        this.chefUsername = chefUsername;
    }
}