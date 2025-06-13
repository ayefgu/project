package Console;

import java.util.List;

public class Order {
    private String userId;
    private List<CartItem> items;
    private String phone;
    private String email;
    private String address;
    private double totalAmount;

    public Order(String userId, List<CartItem> items, String phone, String email, String address, double totalAmount) {
        this.userId = userId;
        this.items = items;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "\n🧾 주문자: " + userId +
               "\n📞 연락처: " + phone +
               "\n📧 이메일: " + email +
               "\n🏠 주소: " + address +
               "\n🛒 상품 수량: " + items.size() +
               "\n💳 총 금액: " + totalAmount + "원";
    }
}
