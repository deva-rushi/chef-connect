package application.utils;

import application.data.ChefData;
import application.data.UserDatabase;
import application.models.Chef;
import application.models.Customer;
import application.models.MenuItem;

public class AppInitializer {

    public static void initializeData() {
        // Create default chefs
        Chef chef1 = new Chef("chef1", "Password123!", "Italian");
        Chef chef2 = new Chef("chef2", "SecurePass456!", "Mexican");
        Chef chef3 = new Chef("chef3", "StrongPwd789!", "Indian");

        // Add chefs to the database
        UserDatabase.addUser(chef1);
        UserDatabase.addUser(chef2);
        UserDatabase.addUser(chef3);

        ChefData.addChef(chef1);
        ChefData.addChef(chef2);
        ChefData.addChef(chef3);

        // Create default menu items for chef1 (Italian)
        MenuItem pastaCarbonaraC1 = new MenuItem("Pasta Carbonara", "Classic Italian...", 15.99, "chef1");
        MenuItem margheritaPizzaC1 = new MenuItem("Margherita Pizza", "Simple and delicious...", 12.50, "chef1");
        MenuItem tiramisuC1 = new MenuItem("Tiramisu", "Italian dessert...", 7.00, "chef1");
        ChefData.addMenuItem(chef1, pastaCarbonaraC1);
        ChefData.addMenuItem(chef1, margheritaPizzaC1);
        ChefData.addMenuItem(chef1, tiramisuC1);

        // Create default menu items for chef2 (Mexican)
        MenuItem tacosAlPastorC2 = new MenuItem("Tacos al Pastor", "Marinated pork...", 13.75, "chef2");
        MenuItem enchiladasRojasC2 = new MenuItem("Enchiladas Rojas", "Corn tortillas...", 11.99, "chef2");
        MenuItem churrosConChocolateC2 = new MenuItem("Churros con Chocolate", "Fried dough...", 6.50, "chef2");
        ChefData.addMenuItem(chef2, tacosAlPastorC2);
        ChefData.addMenuItem(chef2, enchiladasRojasC2);
        ChefData.addMenuItem(chef2, churrosConChocolateC2);

        // Create default menu items for chef3 (Indian)
        MenuItem butterChickenC3 = new MenuItem("Butter Chicken", "Tender pieces...", 16.50, "chef3");
        MenuItem chickenBiryaniC3 = new MenuItem("Chicken Biryani", "Fragrant rice...", 14.25, "chef3");
        MenuItem gulabJamunC3 = new MenuItem("Gulab Jamun", "Deep-fried milk solids...", 5.75, "chef3");
        ChefData.addMenuItem(chef3, butterChickenC3);
        ChefData.addMenuItem(chef3, chickenBiryaniC3);
        ChefData.addMenuItem(chef3, gulabJamunC3);

        // Create default customers
        Customer customer1 = new Customer("customer1", "CustomerPass1!", "");
        Customer customer2 = new Customer("customer2", "CustomerPass2!", "");

        // Add customers to the database
        UserDatabase.addUser(customer1);
        UserDatabase.addUser(customer2);
    }
}