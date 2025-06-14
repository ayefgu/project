import java.util.List;

public class Order {
    private List<CartItem> items;
    private String phone;
    private String email;
    private String address;

    public Order(List<CartItem> items, String phone, String email, String address) {
        this.items = items;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order details:\n");
        for (CartItem item : items) {
            sb.append("- ").append(item).append("\n");
        }
        sb.append("Total: $").append(getTotalAmount()).append("\n");
        sb.append("Contact: ").append(phone).append(", ").append(email).append("\n");
        sb.append("Address: ").append(address).append("\n");
        return sb.toString();
    }
}
