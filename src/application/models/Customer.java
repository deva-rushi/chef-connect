package application.models;

public class Customer extends User {
    private String address;

    public Customer(String username, String password, String address) {
        super(username, password, "Customer");
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}