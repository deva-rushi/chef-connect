package application.utils;

public class SessionManager {  //For managing the user session
    private static String username;
    private static String role;  //Whether chef or customer

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