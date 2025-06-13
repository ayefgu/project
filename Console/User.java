package Console;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String password;
    private String phone;
    private String email;
    private String address;
    
    private List<CartItem> cart = new ArrayList<>();

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() { return id; }
    
    public String getPassword() { return password; }
    
    public String getPhone() { return phone; }
    
    public String getEmail() { return email; }
    
    public String getAddress() { return address; }
    
    public void setContactInfo(String phone, String email, String address) {
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cart.add(new CartItem(product, quantity));
    }

    public void removeFromCart(int productId) {
        cart.removeIf(item -> item.getProduct().getId() == productId);
    }

    public void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("🧺 장바구니가 비어 있습니다.");
            return;
        }

        System.out.println("🧺 현재 장바구니:");
        int index = 1;
        for (CartItem item : cart) {
            System.out.println(index++ + ". " + item);
        }
    }

    }