import java.util.HashMap;//to use its class and methods we import it
import java.util.Map;// it is like dictionary key-value pairs
import java.util.Scanner;// we used this to take user input

// Abstract class for Products
abstract class Product {
    private String name;//private is to hide it from other classes
    private double price;
    private int stockQuantity;

    public Product(String name, double price, int stockQuantity) { // parameterized constructor with attributes
        this.name = name;
        this.price = price;// this is a keyword which refers to the current object in a method or constructor
        this.stockQuantity = stockQuantity;
    }
// getter methods to access the encapsulated fields
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
// method to reduce Stock after purchase
    public void reduceStock(int quantity) {// non static method
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
        } else {
            System.out.println("Not enough stock for " + name + "!");
        }
    }
// toString() method is to return in string format as return type is string
    @Override
    public String toString() {
        return name + " - $" + price + " (Stock: " + stockQuantity + ")";
    }

    // Abstract method for discount (if applicable in subclasses)
    public abstract double getDiscountedPrice();
}

// Concrete subclass for Electronics
class Electronics extends Product { // electronics inheriting the product class behaviour and properties
    public Electronics(String name, double price, int stockQuantity) {//parameterized constructor
        super(name, price, stockQuantity); // used to call superclass methods, and to access the superclass constructor.
    }

    @Override
    public double getDiscountedPrice() { // abstraction & polymorphism method overriding
        // Example discount logic for electronics
        return getPrice() * 0.9; // 10% discount
    }
}

// Concrete subclass for Clothing
class Clothing extends Product { // hierarchial inheritance 
    public Clothing(String name, double price, int stockQuantity) {//parameterized constructor
        super(name, price, stockQuantity);// it refers to parent class objects
    }

    @Override
    public double getDiscountedPrice() {//abstraction & method overriding 
        // Example discount logic for clothing
        return getPrice() * 0.8; // 20% discount
    }
}

// Customer class with ShoppingCart
class Customer {
    private ShoppingCart cart; // composition: has a (relationship customer as a cart)

    public Customer() {//default constructor
        this.cart = new ShoppingCart();// Initializes a new ShoppingCart when a Customer is created
    }

    public ShoppingCart getCart() {// Provides access to the customer's shopping cart.
        return cart;
    }
}

// Admin class to manage products
class Admin {
    private Map<String, Product> products;// Association Admin manages the collection of products(1:1,1:m,m:m) relationships

    public Admin(Map<String, Product> products) { // parameterized constructor to map products
        this.products = products;// takes a map of products to initialize.
    }

    public void printStock() {// non static method
        System.out.println("**************************************************");
        System.out.println("               Current Stock Levels               ");
        System.out.println("**************************************************");
        for (Product product : products.values()) {/*  enhanced for loop (also known as a "for-each" loop) in Java.
                                                This construct simplifies iterating over collections, such as lists or maps */
            System.out.println(product.getName() + " - " + product.getStockQuantity() + " units" + " - " + product.getPrice()+ "$ (1)");
        }
        System.out.println("**************************************************");
    }
}

// ShoppingCart class
class ShoppingCart {
    private Map<Product, Integer> items;// Composition: ShoppingCart has a collection of Products


    public ShoppingCart() {
        this.items = new HashMap<>();/* is a line of code within the constructor that initializes
                           the items field of the ShoppingCart class*/
}
// method to add items ,renove items to the cart
    public void addItem(Product product, int quantity) { //non static methood with parameters
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (product.getStockQuantity() >= quantity) {
            items.put(product, items.getOrDefault(product, 0) + quantity);
            product.reduceStock(quantity);
            System.out.println("Added " + quantity + " of " + product.getName() + " to cart.");
        } else {
            System.out.println("Quantity limit exceeded for " + product.getName() + ". Available stock: " + product.getStockQuantity());
        }
    }
// method to process and view the cart 
    public void checkout() {// not static method
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = 0;
        System.out.println("**************************************************");
        System.out.println("                   Checkout Summary               ");
        System.out.println("**************************************************");
        /*A format string that specifies how the output should be formatted(%-20s %-10s %-10s %-10s\n)
        %-20s:(%: Marks the beginning of the format specifier.) Ensures the "Product" header is left-aligned(-) and takes up '20' character spaces
        "s" represents the string type.If "Product" is shorter than 20 characters, it will be padded with spaces on the right.*/
        System.out.printf("%-20s %-10s %-10s %-10s\n", "Product", "Quantity", "Price", "Subtotal");
        System.out.println("--------------------------------------------------");

        for (Map.Entry<Product, Integer> entry : items.entrySet()) {/* for each loop with Map.Entry: This is a nested interface within the Map interface in Java.
            This method returns a Set of Map.Entry<Product, Integer>. Each Map.Entry in this set represents a single key-value pair from the map*/
            Product product = entry.getKey();// returns key:items in cart
            int quantity = entry.getValue();// returns values:quantity of items
            double discountedPrice = product.getDiscountedPrice();// accessing with getter method
            double subtotal = discountedPrice * quantity;
            total += subtotal;
            //"printf" method is used to print formatted strings,It allows for precise control over the formatting of the output.
            System.out.printf("%-20s %-10d $%-10.2f $%-10.2f\n", product.getName(), quantity, discountedPrice, subtotal);
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total Amount: $%-10.2f\n", total);
        System.out.println("**************************************************");
        items.clear();
        System.out.println("Thank you for shopping with us!");
        System.out.println("**************************************************");
    }
}

// Main class to execute the program
public class Main {
    public static void main(String[] args) {// main method from where the execution of program starts.
        // Create some products with object creation for their respective classes
        Product laptop = new Electronics("Laptop", 100000.0, 10);
        Product phone = new Electronics("Phone", 50000.0, 30);
        Product shoe = new Clothing("Shoe", 1000.0, 30);
        Product watch = new Clothing("Watch", 10000.0, 40);
        Product smartTv = new Electronics("SmartTV", 60000.0, 15);
        Product speakers = new Electronics("Speakers", 15000.0, 15);
        Product ac = new Electronics("AC", 35000.0, 8);
        Product washingMachine = new Electronics("Washing Machine", 25000.0, 12);
        Product vacuumCleaner = new Electronics("Vacuum Cleaner", 8000.0, 12);
        Product earpods = new Electronics("Earpods", 3000.0, 25);

        // Create a map of products,this is used for accepting uupercase as well as lowercase
        Map<String, Product> products = new HashMap<>();// efficient access and case insensitive 
        products.put(laptop.getName().toLowerCase(), laptop);
        products.put(phone.getName().toLowerCase(), phone);
        products.put(shoe.getName().toLowerCase(), shoe);
        products.put(watch.getName().toLowerCase(), watch);
        products.put(smartTv.getName().toLowerCase(), smartTv);
        products.put(speakers.getName().toLowerCase(), speakers);
        products.put(ac.getName().toLowerCase(), ac);
        products.put(washingMachine.getName().toLowerCase(), washingMachine);
        products.put(vacuumCleaner.getName().toLowerCase(), vacuumCleaner);
        products.put(earpods.getName().toLowerCase(), earpods);

        // Create a customer and an admin
        Customer customer = new Customer();//object creation and constructor is called
        Admin admin = new Admin(products);

        // Print initial stock
        admin.printStock();

        // Create Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Customer adds items to cart
        System.out.println("Enter the product name to add to the cart :");
        String productName = scanner.nextLine();
        Product product = products.get(productName.toLowerCase());// retrieve the product from the map

        if (product != null) {
            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            customer.getCart().addItem(product, quantity);// cart creating for customer
        } else {
            System.out.println("Invalid product name.");
        }

        // Prompt user for their name
        System.out.println("Enter your name for the order:");
        String userName = scanner.nextLine();

        // Place an order
        System.out.println("**************************************************");
        System.out.println("         Order successfully placed by " + userName + "!");
        System.out.println("**************************************************");
        customer.getCart().checkout();// checkout summary 

        // Print stock after transaction
        admin.printStock();

        scanner.close();// Close the scanner to prevent resource leaks
    }
}
