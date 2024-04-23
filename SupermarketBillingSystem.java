import java.io.*;
import java.util.*;

// Enum for product categories
enum Category 
{
    GROCERY,
    ELECTRONICS,
    CLOTHING,
    BEAUTY,
    HOME_APPLIANCES
}

// Item class to represent products in the supermarket
class Item implements Serializable 
{
    private String name;
    private double price;
    private Category category;
    private int quantityAvailable; // Quantity available in the supermarket

    public Item(String name, double price, Category category, int quantityAvailable) 
    {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantityAvailable = quantityAvailable;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice() 
    {
        return price;
    }

    public Category getCategory() 
    {
        return category;
    }

    public int getQuantityAvailable() 
    {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) 
    {
        this.quantityAvailable = quantityAvailable;
    }
}

// ShoppingCart class to manage items added to the cart
class ShoppingCart implements Serializable 
  {
    private HashMap<Item, Integer> items;

    public ShoppingCart() 
    {
        items = new HashMap<>();
    }

    public void addItem(Item item, int quantity) 
    {
        if (items.containsKey(item)) 
        {
            quantity += items.get(item);
        }
        items.put(item, quantity);
    }

    public void removeItem(Item item, int quantity) 
    {
        if (items.containsKey(item)) 
        {
            int currentQuantity = items.get(item);
            if (currentQuantity <= quantity) 
            {
                items.remove(item);
            } else 
            {
                items.put(item, currentQuantity - quantity);
            }
        }
    }

    public double calculateTotal()
    {
        double total = 0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) 
        {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            total += item.getPrice() * quantity;
        }
        return total;
    }

    public void displayReceipt(String customerName, String customerEmail) 
    {
        System.out.println("\n----- Receipt -----\n");
        System.out.println("Customer: " + customerName);
        System.out.println("Email: " + customerEmail);
        System.out.println("Date: " + new Date());
        System.out.println("\n--- Items ---");
        for (Map.Entry<Item, Integer> entry : items.entrySet()) 
        {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = item.getPrice() * quantity;
            System.out.println(item.getName() + " - Quantity: " + quantity + " - $" + itemTotal);
        }
        System.out.println("\nTotal: $" + calculateTotal());
        System.out.println("--- Thank you for shopping with us! ---");
    }

    public HashMap<Item, Integer> getItems() 
    {
        return items;
    }

    public void clearCart() 
    {
        items.clear();
    }
}

// Discount class to represent discounts
class Discount implements Serializable 
{
    private String name;
    private double amount;
    private boolean isPercentage;

    public Discount(String name, double amount, boolean isPercentage) 
  {
        this.name = name;
        this.amount = amount;
        this.isPercentage = isPercentage;
    }

    public double applyDiscount(double price) 
  {
        if (isPercentage) 
        {
            return price - (price * amount / 100);
        } else 
        {
            return price - amount;
        }
    }

    public String getName() 
    {
        return name;
    }
}

// User class for authentication and personalized experience
class User implements Serializable 
{
    private String name;
    private String email;
    private String password; // Hashed password for security
    private ShoppingCart cart;
    private ArrayList<Item> favoriteItems;
    private ArrayList<Item> purchasedItems;
    // Add more user-related fields as needed

    public User(String name, String email, String password) 
    {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cart = new ShoppingCart();
        this.favoriteItems = new ArrayList<>();
        this.purchasedItems = new ArrayList<>();
    }

    public String getName() 
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public ShoppingCart getCart()
    {
        return cart;
    }

    public ArrayList<Item> getFavoriteItems() 
    {
        return favoriteItems;
    }

    public ArrayList<Item> getPurchasedItems() 
    {
        return purchasedItems;
    }

    // Add methods for managing favorite items, purchase history, etc.
}

// Supermarket class to manage products, users, and orders
class Supermarket implements Serializable 
{
    private HashMap<String, Item> products;
    private ArrayList<Discount> discounts;
    private ArrayList<User> users;

    public Supermarket() 
  {
        products = new HashMap<>();
        discounts = new ArrayList<>();
        users = new ArrayList<>();
        // Initialize products and discounts
        initializeProducts();
        initializeDiscounts();
    }

    private void initializeProducts() 
    {
        products.put("Apple", new Item("Apple", 1.0, Category.GROCERY, 50));
        products.put("TV", new Item("TV", 500.0, Category.ELECTRONICS, 10));
        products.put("Shirt", new Item("Shirt", 20.0, Category.CLOTHING, 30));
        products.put("Shampoo", new Item("Shampoo", 5.0, Category.BEAUTY, 40));
        products.put("Microwave", new Item("Microwave", 100.0, Category.HOME_APPLIANCES, 15));
    }

    private void initializeDiscounts() 
    {
        discounts.add(new Discount("10% Off Groceries", 10.0, true));
        discounts.add(new Discount("$50 Off Electronics", 50.0, false));
        discounts.add(new Discount("Buy One Get One Free on Shirts", 100.0, false));
    }

    public void addUser(User user) 
    {
        users.add(user);
    }

    public User getUserByEmail(String email) 
    {
        for (User user : users) 
        {
            if (user.getEmail().equalsIgnoreCase(email)) 
            {
                return user;
            }
        }
        return null;
    }

    public void displayProductCategories() 
  {
        System.out.println("\n--- Product Categories ---");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) 
        {
            System.out.println((i + 1) + ". " + categories[i]);
        }
    }

    public Category getCategoryFromChoice(int choice) 
  {
        Category[] categories = Category.values();
        if (choice > 0 && choice <= categories.length) 
        {
            return categories[choice - 1];
        }
        return null;
    }

    public void displayProductsInCategory(Category category) 
    {
        System.out.println("\n--- " + category + " Products ---");
        int productNumber = 1;
        for (Item item : products.values()) 
        {
            if (item.getCategory() == category && item.getQuantityAvailable() > 0) 
            {
                System.out.println(productNumber + ". " + item.getName() + " - $" + item.getPrice() + " - Available: " + item.getQuantityAvailable());
                productNumber++;
            }
        }
    }

    public Item getProductFromChoice(Category category, int choice) 
  {
        int productNumber = 1;
        for (Item item : products.values()) 
        {
            if (item.getCategory() == category && item.getQuantityAvailable() > 0) {
                if (productNumber == choice) 
                {
                    return item;
                }
                productNumber++;
            }
        }
        return null;
    }

    public double calculateTotalDiscount(ShoppingCart cart) 
  {
        double totalDiscount = 0;
        for (Map.Entry<Item, Integer> entry : cart.getItems().entrySet()) 
        {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = item.getPrice() * quantity;
            totalDiscount += getDiscountAmount(item, itemTotal) * quantity;
        }
        return totalDiscount;
    }

    public double getDiscountAmount(Item item, double price) 
  {
        for (Discount discount : discounts) 
        {
            if (discount.getName().equals("10% Off Groceries") && item.getCategory() == Category.GROCERY) 
            {
                return discount.applyDiscount(price);
            } else if (discount.getName().equals("$50 Off Electronics") && item.getCategory() == Category.ELECTRONICS) 
            {
                return discount.applyDiscount(price);
            } else if (discount.getName().equals("Buy One Get One Free on Shirts") && item.getName().equals("Shirt")) 
            {
                return applyBOGODiscount(item, price);
            }
        }
        return 0;
    }

    public double applyBOGODiscount(Item item, double price) 
  {
        // For BOGO, return the price of one item
        return item.getPrice();
    }

    public void applyDiscount(ShoppingCart cart, Scanner scanner) 
  {
        if (!discounts.isEmpty()) 
        {
            System.out.println("\n--- Available Discounts ---");
            for (int i = 0; i < discounts.size(); i++) 
            {
                Discount discount = discounts.get(i);
                System.out.println((i + 1) + ". " + discount.getName());
            }
            System.out.print("Enter discount number to apply: ");
            int discountNumber = getIntegerInput(scanner);
            if (discountNumber > 0 && discountNumber <= discounts.size()) 
            {
                Discount selectedDiscount = discounts.get(discountNumber - 1);
                double totalDiscount = calculateTotalDiscount(cart);
                double discountedTotal = cart.calculateTotal() - totalDiscount;
                double newTotal = selectedDiscount.applyDiscount(discountedTotal);

                System.out.println("Discount applied: " + selectedDiscount.getName());
                System.out.println("New Total: $" + newTotal);
            } 
            else 
            {
                System.out.println("Invalid discount number.");
            }
        } 
        else 
        {
            System.out.println("No discounts available.");
        }
    }

    public void clearCart(ShoppingCart cart, ArrayList<Item> cartItems) 
  {
        for (Item item : cartItems) 
        {
            item.setQuantityAvailable(item.getQuantityAvailable() + cart.getItems().get(item));
        }
        cart.clearCart();
        cartItems.clear();
        System.out.println("Cart cleared.");
    }

    private int getIntegerInput(Scanner scanner) 
  {
        while (!scanner.hasNextInt()) 
        {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextInt();
    }

    // Add more methods for the supermarket management system as needed
}

// Main class to run the supermarket billing system
public class SupermarketBillingSystem
{
    private static final double TAX_RATE = 0.1; // 10% tax rate
    private static Supermarket supermarket;
    private static User currentUser;
    private static ShoppingCart cart;
    private static ArrayList<Item> cartItems; // List to keep track of individual cart items

    public static void main(String[] args) 
  {
        supermarket = new Supermarket();
        cart = new ShoppingCart();
        cartItems = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        while (true) 
        {
            System.out.println("\n--- Supermarket Billing System ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(scanner);

            switch (choice) 
            {
                case 1:
                    loginUser(scanner);
                    break;
                case 2:
                    registerUser(scanner);
                    break;
                case 3:
                    // Continue as guest
                    currentUser = null;
                    break;
                case 4:
                    System.out.println("Thank you for using the Supermarket Billing System.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }

            while (currentUser != null) 
            {
                System.out.println("\n--- Welcome, " + currentUser.getName() + " ---");
                System.out.println("1. Add Item to Cart");
                System.out.println("2. Remove Item from Cart");
                System.out.println("3. Calculate Total");
                System.out.println("4. Display Receipt");
                System.out.println("5. Apply Discount");
                System.out.println("6. Clear Cart");
                System.out.println("7. Confirm Order");
                System.out.println("8. Logout");
                System.out.print("Enter your choice: ");

                int userChoice = getIntInput(scanner);

                switch (userChoice) 
                {
                    case 1:
                        supermarket.displayProductCategories();
                        System.out.print("Enter category number: ");
                        int categoryChoice = getIntInput(scanner);
                        Category selectedCategory = supermarket.getCategoryFromChoice(categoryChoice);
                        if (selectedCategory != null) 
                        {
                            supermarket.displayProductsInCategory(selectedCategory);
                            System.out.print("Enter product number: ");
                            int productChoice = getIntInput(scanner);
                            Item selectedProduct = supermarket.getProductFromChoice(selectedCategory, productChoice);
                            if (selectedProduct != null) 
                            {
                                System.out.print("Enter quantity: ");
                                int quantity = getIntInput(scanner);
                                if (selectedProduct.getQuantityAvailable() >= quantity)
                                {
                                    cart.addItem(selectedProduct, quantity);
                                    selectedProduct.setQuantityAvailable(selectedProduct.getQuantityAvailable() - quantity);
                                    cartItems.add(selectedProduct);
                                    System.out.println(quantity + " " + selectedProduct.getName() + "(s) added to cart.");
                                }
                                else 
                                {
                                    System.out.println("Insufficient quantity available.");
                                }
                            } 
                            else
                            {
                                System.out.println("Invalid product choice.");
                            }
                        } 
                        else 
                        {
                            System.out.println("Invalid category choice.");
                        }
                        break;
                    case 2:
                        if (!cartItems.isEmpty())
                        {
                            displayCartItems();
                            System.out.print("Enter item number to remove: ");
                            int itemNumber = getIntInput(scanner);
                            if (itemNumber > 0 && itemNumber <= cartItems.size()) 
                            {
                                Item itemToRemove = cartItems.get(itemNumber - 1);
                                System.out.print("Enter quantity to remove: ");
                                int quantityToRemove = getIntInput(scanner);
                                cart.removeItem(itemToRemove, quantityToRemove);
                                itemToRemove.setQuantityAvailable(itemToRemove.getQuantityAvailable() + quantityToRemove);
                                cartItems.remove(itemToRemove);
                                System.out.println(quantityToRemove + " " + itemToRemove.getName() + "(s) removed from cart.");
                            } 
                            else 
                            {
                                System.out.println("Invalid item number.");
                            }
                        } 
                        else 
                        {
                            System.out.println("Cart is empty.");
                        }
                        break;
                    case 3:
                        displayTotalWithDiscounts();
                        break;
                    case 4:
                        displayReceipt(scanner);
                        break;
                    case 5:
                        supermarket.applyDiscount(cart, scanner);
                        break;
                    case 6:
                        supermarket.clearCart(cart, cartItems);
                        break;
                    case 7:
                        confirmOrder(scanner);
                        break;
                    case 8:
                        System.out.println("Logged out.");
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        }
    }

    private static void loginUser(Scanner scanner) 
  {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your password: "); // Password should be hashed and validated
        String password = scanner.nextLine().trim();

        User user = supermarket.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) 
        {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
        } else 
        {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void registerUser(Scanner scanner) 
  {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your password: "); // Password should be hashed before storing
        String password = scanner.nextLine().trim();

        User newUser = new User(name, email, password);
        supermarket.addUser(newUser);
        currentUser = newUser;
        System.out.println("Registration successful. Welcome, " + currentUser.getName() + "!");
    }

    private static void displayCartItems() 
  {
        System.out.println("\n--- Cart Items ---");
        int itemNumber = 1;
        for (Item item : cartItems) {
            System.out.println(itemNumber + ". " + item.getName() + " - Quantity: " + cartItems.size());
            itemNumber++;
        }
    }

    private static void displayTotalWithDiscounts() 
   {
        double subtotal = cart.calculateTotal();
        double totalDiscount = supermarket.calculateTotalDiscount(cart);
        double discountedTotal = subtotal - totalDiscount;
        double tax = discountedTotal * TAX_RATE;
        double total = discountedTotal + tax;

        System.out.println("\n--- Total with Discounts ---");
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Total Discounts: $" + totalDiscount);
        System.out.println("Discounted Total: $" + discountedTotal);
        System.out.println("Tax: $" + tax);
        System.out.println("Total: $" + total);
    }

    private static void displayReceipt(Scanner scanner) 
    {
        if (currentUser == null) 
        {
            System.out.println("Please login or register to display the receipt.");
            return;
        }
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String customerEmail = scanner.nextLine();
        cart.displayReceipt(customerName, customerEmail);
    }

    private static void confirmOrder(Scanner scanner) 
    {
        if (currentUser == null) 
        {
            System.out.println("Please login or register to confirm the order.");
            return;
        }
        if (!cartItems.isEmpty()) 
        {
            System.out.println("\n--- Confirm Order ---");
            displayCartItems();
            System.out.println("Total: $" + cart.calculateTotal());
            System.out.print("Confirm order (yes/no): ");
            String confirm = scanner.nextLine().toLowerCase();
            if (confirm.equals("yes")) 
            {
                System.out.println("Order confirmed. Thank you!");
                supermarket.clearCart(cart, cartItems);
            } 
            else
            {
                System.out.println("Order cancelled.");
            }
        }
        else
        {
            System.out.println("Cart is empty.");
        }
    }

    private static int getIntInput(Scanner scanner)
  {
        while (!scanner.hasNextInt()) 
        {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextInt();
    }
}
                     
