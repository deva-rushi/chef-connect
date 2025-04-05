package application.data;

import application.models.Chef;
import application.models.MenuItem;
import application.models.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ChefData {

    private static Map<Chef, List<MenuItem>> chefMenus = new ConcurrentHashMap<>();
    private static Map<String, Chef> chefs = new ConcurrentHashMap<>();
    private static Map<String, Queue<Order>> chefOrders = new ConcurrentHashMap<>();
    private static int nextItemID = 1;
    private static int nextOrderID = 1;

    public static synchronized void addMenuItem(Chef chef, MenuItem item) {
        item.setItemID(nextItemID++);
        chefMenus.computeIfAbsent(chef, k -> new CopyOnWriteArrayList<>()).add(item);
    }

    public static List<MenuItem> getMenuItemsByChef(Chef chef) {
        return chefMenus.getOrDefault(chef, Collections.emptyList());
    }

    public static synchronized void addOrder(Order order) {
        order.setOrderId(nextOrderID++);
        chefOrders.computeIfAbsent(order.getChefUsername(), k -> new ConcurrentLinkedQueue<>()).add(order);
    }

    public static Queue<Order> getOrdersByChef(String chefUsername) {
        return chefOrders.getOrDefault(chefUsername, new ConcurrentLinkedQueue<>());
    }

    public static synchronized void addChef(Chef chef) {
        chefs.put(chef.getUsername(), chef);
        chefOrders.put(chef.getUsername(), new ConcurrentLinkedQueue<>());
        System.out.println("Chef added: " + chef.getUsername() + ", Map size: " + chefs.size());
    }

    public static Chef getChef(String username) {
        System.out.println("ChefData.getChef() called with username: " + username);
        System.out.println("Current chefs map: " + chefs);
        return chefs.get(username);
    }

    public static Collection<Chef> getAllChefs() {
        return chefs.values();
    }

    public static List<Order> getOrdersByCustomer(String customerUsername) {
        return chefOrders.values().stream()
                .flatMap(Collection::stream)
                .filter(order -> order.getCustomerUsername().equals(customerUsername))
                .collect(Collectors.toList());
    }

    public static synchronized void updateChef(Chef chef) {
        chefs.put(chef.getUsername(), chef);
    }

    public static synchronized void updateOrder(Order order) {
        Queue<Order> orderQueue = chefOrders.get(order.getChefUsername());
        if (orderQueue != null) {
            orderQueue.removeIf(o -> o.getOrderId() == order.getOrderId());
            orderQueue.add(order);
        }
    }
}