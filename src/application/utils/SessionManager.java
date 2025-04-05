package application.utils;

public class SessionManager {
    private static String username;
    private static String role;

    public static void setUser(String user, String userRole) {
        username = user;
        role = userRole;
        System.out.println("Session set: username=" + username + ", role=" + role);
    }

    public static String getUsername() {
        System.out.println("Getting username: " + username);
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static void clearSession() {
        username = null;
        role = null;
    }
}