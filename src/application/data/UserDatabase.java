package application.data;

import java.util.ArrayList;
import java.util.List;

import application.models.User;

public class UserDatabase {
    private static List<User> users = new ArrayList<>();

    public static boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addUser(User user) {
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
}