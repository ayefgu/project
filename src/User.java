import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;
    private List<CartItem> cart;
    private List<Order> orders;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.cart = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                cart.remove(item);
                cart.add(new CartItem(product, item.getQuantity() + quantity));
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
    }

    public void clearCart() {
        cart.clear();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
