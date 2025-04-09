package application.data;

import java.util.ArrayList;
import java.util.List;

import application.models.Chef;
import application.models.Customer;
import application.models.User;

public class UserDatabase {
    private static List<User> users = new ArrayList<>();  //Users are stored in ArrayList

    public static boolean userExists(String username) { //iterate through users array and check if user exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addUser(User user) {  //Add new user to ArrayList
        users.add(user);
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Chef getChef(String username) {  //Get chef with matching username
        for (User user : users) {
            if (user instanceof Chef && user.getUsername().equals(username)) {
                return (Chef) user;
            }
        }
        return null;
    }

    public static Customer getCustomer(String username) {  //Get customer with matching username
        for (User user : users) {
            if (user instanceof Customer && user.getUsername().equals(username)) {
                return (Customer) user;
            }
        }
        return null;
    }
}