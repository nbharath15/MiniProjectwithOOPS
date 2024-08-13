# Importing necessary modules
class Product:
    """Abstract class for Products"""
    def __init__(self, name, price, stock_quantity):
        """Parameterized constructor with attributes"""
        self._name = name  # underscore denotes private attribute in Python
        self._price = price
        self._stock_quantity = stock_quantity

    # Getter methods to access the encapsulated fields
    def get_name(self):
        return self._name

    def get_price(self):
        return self._price

    def get_stock_quantity(self):
        return self._stock_quantity

    # Method to reduce stock after purchase
    def reduce_stock(self, quantity):
        if self._stock_quantity >= quantity:
            self._stock_quantity -= quantity
        else:
            print(f"Not enough stock for {self._name}!")

    # String representation method
    def __str__(self):
        return f"{self._name} - ${self._price} (Stock: {self._stock_quantity})"

    # Abstract method for discount (to be implemented in subclasses)
    def get_discounted_price(self):
        raise NotImplementedError("Subclasses should implement this method.")


class Electronics(Product):
    """Concrete subclass for Electronics"""
    def __init__(self, name, price, stock_quantity):
        super().__init__(name, price, stock_quantity)

    def get_discounted_price(self):
        """Example discount logic for electronics"""
        return self.get_price() * 0.9  # 10% discount


class Clothing(Product):
    """Concrete subclass for Clothing"""
    def __init__(self, name, price, stock_quantity):
        super().__init__(name, price, stock_quantity)

    def get_discounted_price(self):
        """Example discount logic for clothing"""
        return self.get_price() * 0.8  # 20% discount


class ShoppingCart:
    """ShoppingCart class"""
    def __init__(self):
        self.items = {}  # Composition: ShoppingCart has a collection of Products

    def add_item(self, product, quantity):
        """Method to add items to the cart"""
        if quantity <= 0:
            print("Quantity must be positive.")
            return

        if product.get_stock_quantity() >= quantity:
            self.items[product] = self.items.get(product, 0) + quantity
            product.reduce_stock(quantity)
            print(f"Added {quantity} of {product.get_name()} to cart.")
        else:
            print(f"Quantity limit exceeded for {product.get_name()}. Available stock: {product.get_stock_quantity()}")

    def checkout(self):
        """Method to process and view the cart"""
        if not self.items:
            print("Your cart is empty.")
            return

        total = 0
        print("**************************************************")
        print("                   Checkout Summary               ")
        print("**************************************************")
        print(f"{'Product':<20} {'Quantity':<10} {'Price':<10} {'Subtotal':<10}")
        print("--------------------------------------------------")

        for product, quantity in self.items.items():
            discounted_price = product.get_discounted_price()
            subtotal = discounted_price * quantity
            total += subtotal
            print(f"{product.get_name():<20} {quantity:<10} ${discounted_price:<10.2f} ${subtotal:<10.2f}")

        print("--------------------------------------------------")
        print(f"Total Amount: ${total:<10.2f}")
        print("**************************************************")
        self.items.clear()
        print("Thank you for shopping with us!")
        print("**************************************************")


class Customer:
    """Customer class with ShoppingCart"""
    def __init__(self):
        self.cart = ShoppingCart()  # Composition: Customer has a cart

    def get_cart(self):
        """Provides access to the customer's shopping cart."""
        return self.cart


class Admin:
    """Admin class to manage products"""
    def __init__(self, products):
        self.products = products  # Association: Admin manages the collection of products

    def print_stock(self):
        """Method to print current stock levels"""
        print("**************************************************")
        print("               Current Stock Levels               ")
        print("**************************************************")
        for product in self.products.values():
            print(f"{product.get_name()} - {product.get_stock_quantity()} units - ${product.get_price()} (1)")
        print("**************************************************")


# Main function to execute the program
def main():
    # Create some products
    laptop = Electronics("Laptop", 100000.0, 10)
    phone = Electronics("Phone", 50000.0, 30)
    shoe = Clothing("Shoe", 1000.0, 30)
    watch = Clothing("Watch", 10000.0, 40)
    smart_tv = Electronics("SmartTV", 60000.0, 15)
    speakers = Electronics("Speakers", 15000.0, 15)
    ac = Electronics("AC", 35000.0, 8)
    washing_machine = Electronics("Washing Machine", 25000.0, 12)
    vacuum_cleaner = Electronics("Vacuum Cleaner", 8000.0, 12)
    earpods = Electronics("Earpods", 3000.0, 25)

    # Create a map of products (case-insensitive keys)
    products = {
        laptop.get_name().lower(): laptop,
        phone.get_name().lower(): phone,
        shoe.get_name().lower(): shoe,
        watch.get_name().lower(): watch,
        smart_tv.get_name().lower(): smart_tv,
        speakers.get_name().lower(): speakers,
        ac.get_name().lower(): ac,
        washing_machine.get_name().lower(): washing_machine,
        vacuum_cleaner.get_name().lower(): vacuum_cleaner,
        earpods.get_name().lower(): earpods,
    }

    # Create a customer and an admin
    customer = Customer()
    admin = Admin(products)

    # Print initial stock
    admin.print_stock()

    # Customer adds items to cart
    product_name = input("Enter the product name to add to the cart: ").strip().lower()
    product = products.get(product_name)

    if product:
        try:
            quantity = int(input("Enter quantity: "))
            customer.get_cart().add_item(product, quantity)
        except ValueError:
            print("Invalid input. Quantity should be an integer.")
    else:
        print("Invalid product name.")

    # Prompt user for their name
    user_name = input("Enter your name for the order: ")

    # Place an order
    print("**************************************************")
    print(f"         Order successfully placed by {user_name}!")
    print("**************************************************")
    customer.get_cart().checkout()

    # Print stock after transaction
    admin.print_stock()


if __name__ == "__main__":
    main()
