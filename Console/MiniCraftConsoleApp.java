package Console;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MiniCraftConsoleApp {
    private static final List<Product> products = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static int productIdCounter = 1;

    public static void main(String[] args) {
        users.add(new User("admin", "1234")); // Админ по умолчанию

        while (true) {
            System.out.println("\n🎀 MiniCraft 쇼핑몰에 오신 것을 환영합니다!");
            if (currentUser == null) {
                System.out.println("1. 로그인 ");
                System.out.println("2. 회원가입 ");
                System.out.print("선택하세요 : ");
                int choice = getIntInput();

                switch (choice) {
                    case 1 -> loginUser();
                    case 2 -> registerUser();
                    default -> System.out.println("❗ 올바르지 않은 선택입니다.");
                }
            } else {
                System.out.println("\n🔐 로그인 사용자: " + currentUser.getId());
                if (currentUser.getId().equals("admin")) {
                    System.out.println("1. 상품 목록 보기");
                    System.out.println("2. 상품 추가");
                    System.out.println("3. 상품 삭제");
                    System.out.println("0. 로그아웃");
                    System.out.print("선택: ");
                    int choice = getIntInput();
                    switch (choice) {
                        case 1 -> viewProducts();
                        case 2 -> addProduct();
                        case 3 -> removeProduct();
                        case 0 -> {
                            currentUser = null;
                            System.out.println("🔓 로그아웃되었습니다.");
                        }
                        default -> System.out.println("❗ 잘못된 선택입니다.");
                    }
                } else {
                    System.out.println("1. 상품 목록 보기");
                    System.out.println("2. 장바구니 보기");
                    System.out.println("3. 장바구니에 상품 추가");
                    System.out.println("4. 장바구니에서 상품 삭제");
                    System.out.println("5. 상품 구매");
                    System.out.println("0. 로그아웃");
                    System.out.print("선택: ");
                    int choice = getIntInput();
                    switch (choice) {
                        case 1 -> viewProducts();
                        case 2 -> currentUser.viewCart();
                        case 3 -> addToCart();
                        case 4 -> removeFromCart();
                        case 5 -> purchaseItems();
                        case 0 -> {
                            currentUser = null;
                            System.out.println("🔓 로그아웃되었습니다.");
                        }
                        default -> System.out.println("❗ 잘못된 선택입니다.");
                    }
                }
            }
        }
    }

    private static void registerUser() {
        System.out.print("아이디 입력: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = scanner.nextLine();

        for (User u : users) {
            if (u.getId().equals(id)) {
                System.out.println("❗ 이미 존재하는 아이디입니다.");
                return;
            }
        }
        users.add(new User(id, pw));
        System.out.println("✅ 회원가입 완료!");
    }

    private static void loginUser() {
        System.out.print("아이디: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호: ");
        String pw = scanner.nextLine();

        for (User u : users) {
            if (u.getId().equals(id) && u.getPassword().equals(pw)) {
                currentUser = u;
                System.out.println("🔓 로그인 성공!");
                return;
            }
        }
        System.out.println("❌ 로그인 실패: 아이디 또는 비밀번호 오류");
    }

    private static void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("📦 등록된 상품이 없습니다.");
        } else {
            System.out.println("📦 상품 목록:");
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }

    private static void addProduct() {
        System.out.print("상품 이름: ");
        String name = scanner.nextLine();
        System.out.print("상품 가격: ");
        double price = getDoubleInput();
        System.out.print("상품 수량: ");
        int stock = getIntInput();

        products.add(new Product(productIdCounter++, name, price, stock));
        System.out.println("✅ 상품이 추가되었습니다.");
    }


    private static void removeProduct() {
        viewProducts();
        if (products.isEmpty()) return;

        System.out.print("삭제할 상품 ID: ");
        int id = getIntInput();

        boolean removed = products.removeIf(p -> p.getId() == id);
        if (removed) {
            System.out.println("🗑️ 상품이 삭제되었습니다.");
        } else {
            System.out.println("❌ 해당 ID의 상품을 찾을 수 없습니다.");
        }
    }

    private static void addToCart() {
        viewProducts();
        if (products.isEmpty()) return;

        System.out.print("추가할 상품 ID: ");
        int id = getIntInput();
        System.out.print("수량: ");
        int qty = getIntInput();

        for (Product p : products) {
            if (p.getId() == id) {
                currentUser.addToCart(p, qty);
                System.out.println("✅ 장바구니에 추가되었습니다.");
                return;
            }
        }
        System.out.println("❌ 상품 ID를 찾을 수 없습니다.");
    }

    private static void removeFromCart() {
        currentUser.viewCart();
        if (currentUser.getCart().isEmpty()) return;

        System.out.print("삭제할 상품 ID: ");
        int id = getIntInput();

        CartItem itemToRemove = null;
        for (CartItem item : currentUser.getCart()) {
            if (item.getProduct().getId() == id) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove == null) {
            System.out.println("❌ 장바구니에 해당 ID의 상품이 없습니다.");
            return;
        }

        System.out.print("삭제할 수량: ");
        int quantityToRemove = getIntInput();

        if (quantityToRemove <= 0) {
            System.out.println("❗ 1 이상의 수량을 입력해주세요.");
        } else if (quantityToRemove >= itemToRemove.getQuantity()) {
            currentUser.removeFromCart(id); // удаляем весь товар
            System.out.println("🗑️ 상품이 장바구니에서 완전히 삭제되었습니다.");
        } else {
            itemToRemove.setQuantity(itemToRemove.getQuantity() - quantityToRemove);
            System.out.println("✅ 장바구니에서 일부 수량이 삭제되었습니다.");
        }
    }


    private static void purchaseItems() {
        List<CartItem> cart = currentUser.getCart();

        if (cart.isEmpty()) {
            System.out.println("🧺 장바구니가 비어 있습니다. 먼저 상품을 추가하세요.");
            return;
        }

        double total = 0;
        System.out.println("🧾 구매 내역:");
        for (CartItem item : cart) {
            double itemTotal = item.getProduct().getPrice() * item.getQuantity();
            total += itemTotal;
            System.out.println("- " + item + " = " + itemTotal + "원");
        }

        System.out.println("💳 총 합계: " + total + "원");

        // Ввод контактной информации
        System.out.print("📱 전화번호 입력: ");
        String phone = scanner.nextLine().trim();

        System.out.print("📧 이메일 입력: ");
        String email = scanner.nextLine().trim();

        System.out.print("🏠 배송 주소 입력: ");
        String address = scanner.nextLine().trim();

        // Ввод банковской информации с проверкой
        String cardNumber;
        while (true) {
            System.out.print("💳 카드 번호 입력 (16자리): ");
            cardNumber = scanner.nextLine().trim();
            if (cardNumber.matches("\\d{16}")) break;
            System.out.println("❗ 카드 번호는 16자리 숫자여야 합니다.");
        }

        String cvc;
        while (true) {
            System.out.print("🔐 CVC 번호 입력 (3자리): ");
            cvc = scanner.nextLine().trim();
            if (cvc.matches("\\d{3}")) break;
            System.out.println("❗ CVC는 3자리 숫자여야 합니다.");
        }

        // Подтверждение покупки
        System.out.print("구매하시겠습니까? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            // Уменьшаем количество товаров на складе
            for (CartItem item : cart) {
                item.getProduct().decreaseStock(item.getQuantity());
            }

            cart.clear();
            System.out.println("✅ 구매가 완료되었습니다. 감사합니다!");
            System.out.println("📦 배송 정보: " + address + " | 📞 " + phone + " | 📧 " + email);
        } else {
            System.out.println("❌ 구매가 취소되었습니다.");
        }
    }



    private static int getIntInput() {
        while (true) {
            try {
                int num = scanner.nextInt();
                scanner.nextLine(); // buffer clear
                return num;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear wrong input
                System.out.print("❗ 숫자를 입력하세요: ");
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                double num = scanner.nextDouble();
                scanner.nextLine(); // buffer clear
                return num;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear wrong input
                System.out.print("❗ 숫자를 입력하세요: ");
            }
        }
    }
}
