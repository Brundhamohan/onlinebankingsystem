import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User user = null;

        while (true) {
            System.out.println("\n--- Online Banking System ---");
            System.out.println("1. Register\n2. Login\n3. Exit");
            String input = sc.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter 1, 2, or 3.");
                continue;
            }

            if (choice == 1) {
                System.out.print("Enter Username: ");
                String username = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
                if (BankSystem.register(username, password))
                    System.out.println("Registration successful!");
                else
                    System.out.println("Registration failed!");
            } else if (choice == 2) {
                System.out.print("Enter Username: ");
                String username = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
                user = BankSystem.login(username, password);
                if (user != null) {
                    System.out.println("Welcome, " + user.getUsername());
                    while (true) {
                        System.out.println("\n1. Deposit\n2. Withdraw\n3. Transfer\n4. Balance\n5. Transactions\n6. Logout");
                        String menuInput = sc.nextLine();
                        int c;
                        try {
                            c = Integer.parseInt(menuInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input! Enter a number 1-6.");
                            continue;
                        }

                        if (c == 1) {
                            System.out.print("Enter amount: ");
                            double amt = sc.nextDouble();
                            sc.nextLine();
                            BankSystem.deposit(user.getUserId(), amt);
                        } else if (c == 2) {
                            System.out.print("Enter amount: ");
                            double amt = sc.nextDouble();
                            sc.nextLine();
                            BankSystem.withdraw(user.getUserId(), amt);
                        } else if (c == 3) {
                            System.out.print("Enter recipient username: ");
                            String recipientUsername = sc.nextLine();
                            int toId = BankSystem.getUserIdByUsername(recipientUsername);
                            if (toId == -1) {
                                System.out.println("Recipient not found!");
                            } else {
                                System.out.print("Enter amount: ");
                                double amt = sc.nextDouble();
                                sc.nextLine();
                                BankSystem.transfer(user.getUserId(), toId, amt);
                            }
                        } else if (c == 4) {
                            BankSystem.checkBalance(user.getUserId());
                        } else if (c == 5) {
                            BankSystem.viewTransactions(user.getUserId());
                        } else if (c == 6) {
                            break;
                        }
                    }
                } else {
                    System.out.println("Invalid login!");
                }
            } else if (choice == 3) {
                break;
            }
        }
        sc.close();
    }
}