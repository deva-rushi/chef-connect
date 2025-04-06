package application.utils;
 
 import application.data.ChefData;
 import application.data.UserDatabase;
 import application.models.Chef;
 import application.models.Customer;
 
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
 
         // Create default customers
         Customer customer1 = new Customer("customer1", "CustomerPass1!", "");
         Customer customer2 = new Customer("customer2", "CustomerPass2!", "");
 
         // Add customers to the database
         UserDatabase.addUser(customer1);
         UserDatabase.addUser(customer2);
     }
 }