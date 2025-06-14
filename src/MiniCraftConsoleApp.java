import java.util.*;

public class MiniCraftConsoleApp {
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);
    private static int nextProductId = 1;

    public static void main(String[] args) {
        System.out.println("MiniCraft 콘솔 앱에 오신 것을 환영합니다!");
        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. 가입하기 | 2. 로그인 | 0. 종료");
                System.out.print("선택하세요: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": register(); break;
                    case "2": login(); break;
                    case "0": System.exit(0); break;
                    default: System.out.println("잘못된 입력입니다.");
                }
            } else if (currentUser.isAdmin()) {
                adminMenu();
            } else {
                userMenu();
            }
        }
    }

    private static void register() {
        System.out.print("사용자 이름: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();
        System.out.print("관리자 계정입니까? (y/n): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("y");
        users.add(new User(username, password, isAdmin));
        System.out.println("가입이 완료되었습니다!");
    }

    private static void login() {
        System.out.print("사용자 이름: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                currentUser = user;
                System.out.println("로그인 성공!");
                return;
            }
        }
        System.out.println("잘못된 사용자 이름 또는 비밀번호입니다.");
    }

    private static void adminMenu() {
        System.out.println("\n[관리자 메뉴]");
        System.out.println("1. 상품 추가");
        System.out.println("2. 상품 삭제");
        System.out.println("3. 로그아웃");
        System.out.print("선택하세요: ");
        switch (scanner.nextLine()) {
            case "1": addProduct(); break;
            case "2": removeProduct(); break;
            case "3": currentUser = null; break;
            default: System.out.println("잘못된 입력입니다.");
        }
    }

    private static void userMenu() {
        System.out.println("\n[사용자 메뉴]");
        System.out.println("1. 상품 보기");
        System.out.println("2. 장바구니 담기");
        System.out.println("3. 장바구니 보기");
        System.out.println("4. 주문하기");
        System.out.println("5. 주문 내역 보기");
        System.out.println("6. 로그아웃");
        System.out.print("선택하세요: ");
        switch (scanner.nextLine()) {
            case "1": listProducts(); break;
            case "2": addToCart(); break;
            case "3": viewCart(); break;
            case "4": placeOrder(); break;
            case "5": viewOrders(); break;
            case "6": currentUser = null; break;
            default: System.out.println("잘못된 입력입니다.");
        }
    }

    private static void addProduct() {
        System.out.print("상품 이름: ");
        String name = scanner.nextLine();
        System.out.print("가격: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("설명: ");
        String description = scanner.nextLine();
        products.add(new Product(nextProductId++, name, price, description));
        System.out.println("상품이 추가되었습니다.");
    }

    private static void removeProduct() {
        listProducts();
        System.out.print("삭제할 상품 ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        products.removeIf(p -> p.getId() == id);
        System.out.println("상품이 삭제되었습니다.");
    }

    private static void listProducts() {
        if (products.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void addToCart() {
        listProducts();
        System.out.print("추가할 상품 ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("수량: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        for (Product p : products) {
            if (p.getId() == id) {
                currentUser.addToCart(p, quantity);
                System.out.println("장바구니에 추가되었습니다.");
                return;
            }
        }
        System.out.println("해당 ID의 상품이 없습니다.");
    }

    private static void viewCart() {
        List<CartItem> cart = currentUser.getCart();
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }
        for (CartItem item : cart) {
            System.out.println(item);
        }
    }

    private static void placeOrder() {
        if (currentUser.getCart().isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }
        System.out.print("전화번호: ");
        String phone = scanner.nextLine();
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        System.out.print("주소: ");
        String address = scanner.nextLine();
        Order order = new Order(new ArrayList<>(currentUser.getCart()), phone, email, address);
        currentUser.addOrder(order);
        currentUser.clearCart();
        System.out.println("주문이 완료되었습니다!");
        System.out.println(order);
    }

    private static void viewOrders() {
        List<Order> orders = currentUser.getOrders();
        if (orders.isEmpty()) {
            System.out.println("주문 내역이 없습니다.");
            return;
        }
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
