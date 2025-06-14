package Console;

import java.util.*;

public class MiniCraftConsoleApp {
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        initializeProducts();

        while (true) {
            if (loggedInUser == null) {
                System.out.println("===== MiniCraft =====");
                System.out.println("1. 회원가입");
                System.out.println("2. 로그인");
                System.out.println("0. 종료");
                System.out.print("선택: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        register();
                        break;
                    case "2":
                        login();
                        break;
                    case "0":
                        System.out.println("👋 종료합니다.");
                        return;
                    default:
                        System.out.println("❌ 잘못된 선택입니다.");
                }
            } else {
                System.out.println("===== 사용자 메뉴 =====");
                System.out.println("1. 상품 목록 보기");
                System.out.println("2. 장바구니 보기");
                System.out.println("3. 장바구니에 상품 추가");
                System.out.println("4. 장바구니에서 상품 제거");
                System.out.println("5. 🧾 주문하기");
                System.out.println("6. 📜 주문 내역 보기");
                System.out.println("0. 로그아웃");
                System.out.print("선택: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        viewProducts();
                        break;
                    case "2":
                        loggedInUser.viewCart();
                        break;
                    case "3":
                        addProductToCart();
                        break;
                    case "4":
                        removeProductFromCart();
                        break;
                    case "5":
                        checkout(loggedInUser);
                        break;
                    case "6":
                        viewOrderHistory(loggedInUser);
                        break;
                    case "0":
                        loggedInUser = null;
                        System.out.println("👋 로그아웃되었습니다.");
                        break;
                    default:
                        System.out.println("❌ 잘못된 선택입니다.");
                }
            }
        }
    }

    private static void register() {
        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        users.add(new User(id, password));
        System.out.println("✅ 회원가입 완료!");
    }

    private static void login() {
        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("✅ 로그인 성공!");
                return;
            }
        }
        System.out.println("❌ 로그인 실패. 아이디 또는 비밀번호가 틀렸습니다.");
    }

    private static void viewProducts() {
        System.out.println("📦 상품 목록:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void addProductToCart() {
        viewProducts();
        System.out.print("추가할 상품 ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        System.out.print("수량: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        for (Product product : products) {
            if (product.getId() == productId) {
                if (product.getStock() >= quantity) {
                    loggedInUser.addToCart(product, quantity);
                    System.out.println("🛒 장바구니에 추가되었습니다.");
                } else {
                    System.out.println("❌ 재고 부족.");
                }
                return;
            }
        }
        System.out.println("❌ 상품을 찾을 수 없습니다.");
    }

    private static void removeProductFromCart() {
        loggedInUser.viewCart();
        System.out.print("삭제할 상품 ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        loggedInUser.removeFromCart(productId);
        System.out.println("🗑️ 장바구니에서 삭제되었습니다.");
    }

    private static void checkout(User user) {
        List<CartItem> cart = user.getCart();
        if (cart.isEmpty()) {
            System.out.println("🧺 장바구니가 비어 있어요.");
            return;
        }

        if (user.getPhone() == null || user.getEmail() == null || user.getAddress() == null) {
            System.out.println("📞 연락처 정보를 입력해 주세요:");
            System.out.print("전화번호: ");
            String phone = scanner.nextLine();
            System.out.print("이메일: ");
            String email = scanner.nextLine();
            System.out.print("주소: ");
            String address = scanner.nextLine();
            user.setContactInfo(phone, email, address);
        }

        double totalAmount = 0;
        for (CartItem item : cart) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
            item.getProduct().decreaseStock(item.getQuantity());
        }

        Order order = new Order(user.getId(), new ArrayList<>(cart), user.getPhone(), user.getEmail(), user.getAddress(), totalAmount);
        user.addOrder(order);
        cart.clear();

        System.out.println("✅ 주문이 완료되었습니다!");
        System.out.println(order);
    }

    private static void viewOrderHistory(User user) {
        user.viewOrders();
    }

    private static void initializeProducts() {
        products.add(new Product(1, "수제 반지", 15000, 10));
        products.add(new Product(2, "핸드메이드 귀걸이", 12000, 20));
        products.add(new Product(3, "비즈 팔찌", 10000, 15));
    }
}
