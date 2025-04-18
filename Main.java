import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.*;

public class Main {
    static Random rand = new Random();

    static class BalanceWrapper {
        double value;
        BalanceWrapper(double value) {
            this.value = value;
        }
    }

    public static String generateRandomString(int length) {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(charset.length());
            randomString.append(charset.charAt(index));
        }
        return randomString.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BalanceWrapper balance = new BalanceWrapper(1000.00);
        int savedPin = 1234;
        double sendMoneyFee = 5.0;
        double nonCkashFee = 10.0;
        double cashOutFee = 10.0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        while (true) {
            String dateStr = dateFormat.format(new Date());

            System.out.println("\n=== cKash ===");
            System.out.println("1. Send Money");
            System.out.println("2. Send Money to Non-cKash User");
            System.out.println("3. Mobile Recharge");
            System.out.println("4. Payment");
            System.out.println("5. Cash Out");
            System.out.println("6. Pay Bill");
            System.out.println("7. Add Money");
            System.out.println("8. Download cKash App");
            System.out.println("9. My cKash");
            System.out.println("10. Reset PIN");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear invalid input
                continue;
            }

            switch (choice) {
                case 0:
                    System.out.println("Thank you for using cKash!");
                    System.exit(0);
                    break;
                case 1:
                    sendMoney(scanner, balance, sendMoneyFee, savedPin, dateStr);
                    break;
                case 2:
                    sendToNonCkash(scanner, balance, nonCkashFee, savedPin, dateStr);
                    break;
                case 3:
                    mobileRecharge(scanner, balance, savedPin, dateStr);
                    break;
                case 4:
                    payment(scanner, balance, savedPin, dateStr);
                    break;
                case 5:
                    cashOut(scanner, balance, cashOutFee, savedPin, dateStr);
                    break;
                case 6:
                    payBill(scanner, balance, savedPin, dateStr);
                    break;
                case 7:
                    addMoney(scanner, balance, dateStr);
                    break;
                case 8:
                    downloadApp();
                    break;
                case 9:
                    showMyCkash(balance.value);
                    break;
                case 10:
                    savedPin = resetPin(scanner, savedPin);
                    break;
                default:
                    System.out.println("Invalid option. Please choose from the menu.");
            }
        }
    }

    private static void sendMoney(Scanner scanner, BalanceWrapper balance, double fee, int savedPin, String dateStr) {
        System.out.print("Enter Receiver cKash Account No: ");
        int number = scanner.nextInt();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Reference: ");
        String reference = scanner.next();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if ((amount + fee) <= balance.value) {
                balance.value -= (amount + fee);
                System.out.printf("Send Money Tk %.2f to %d Successful. Ref %s. Fee Tk %.2f. Balance Tk %.2f.%n",
                        amount, number, reference, fee, balance.value);
                writeToFile(dateStr + " - Send Money: Tk " + amount + " to " + number + ". Ref: " + reference + ". Fee: Tk " + fee + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void sendToNonCkash(Scanner scanner, BalanceWrapper balance, double fee, int savedPin, String dateStr) {
        System.out.print("Enter Receiver Phone Number: ");
        String number = scanner.next();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if ((amount + fee) <= balance.value) {
                balance.value -= (amount + fee);
                System.out.printf("Send Money Tk %.2f to Non-cKash User (%s) Successful. Fee Tk %.2f. Balance Tk %.2f.%n",
                        amount, number, fee, balance.value);
                writeToFile(dateStr + " - Send Money to Non-cKash: Tk " + amount + " to " + number + ". Fee: Tk " + fee + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void mobileRecharge(Scanner scanner, BalanceWrapper balance, int savedPin, String dateStr) {
        System.out.print("Enter Mobile Number: ");
        String number = scanner.next();
        System.out.print("Enter Recharge Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if (amount <= balance.value) {
                balance.value -= amount;
                System.out.printf("Mobile Recharge Tk %.2f to %s Successful. Balance Tk %.2f.%n",
                        amount, number, balance.value);
                writeToFile(dateStr + " - Mobile Recharge: Tk " + amount + " to " + number + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void payment(Scanner scanner, BalanceWrapper balance, int savedPin, String dateStr) {
        System.out.print("Enter Merchant ID: ");
        String id = scanner.next();
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if (amount <= balance.value) {
                balance.value -= amount;
                System.out.printf("Payment Tk %.2f to Merchant (%s) Successful. Balance Tk %.2f.%n",
                        amount, id, balance.value);
                writeToFile(dateStr + " - Payment: Tk " + amount + " to Merchant " + id + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void cashOut(Scanner scanner, BalanceWrapper balance, double fee, int savedPin, String dateStr) {
        System.out.print("Enter Agent Number: ");
        int agent = scanner.nextInt();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if ((amount + fee) <= balance.value) {
                balance.value -= (amount + fee);
                System.out.printf("Cash Out Tk %.2f to Agent (%d) Successful. Fee Tk %.2f. Balance Tk %.2f.%n",
                        amount, agent, fee, balance.value);
                writeToFile(dateStr + " - Cash Out: Tk " + amount + " from Agent " + agent + ". Fee: Tk " + fee + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void payBill(Scanner scanner, BalanceWrapper balance, int savedPin, String dateStr) {
        System.out.print("Enter Biller Name: ");
        String biller = scanner.next();
        System.out.print("Enter Bill Amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Menu PIN to confirm: ");
        int pin = scanner.nextInt();

        if (pin == savedPin) {
            if (amount <= balance.value) {
                balance.value -= amount;
                System.out.printf("Paid Tk %.2f to %s successfully. Balance: Tk %.2f%n", amount, biller, balance.value);
                writeToFile(dateStr + " - Bill Payment: Tk " + amount + " to " + biller + ". Balance: Tk " + balance.value);
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    private static void addMoney(Scanner scanner, BalanceWrapper balance, String dateStr) {
        System.out.print("Enter amount to add: ");
        double amount = scanner.nextDouble();
        balance.value += amount;
        System.out.printf("Successfully added Tk %.2f. New Balance: Tk %.2f%n", amount, balance.value);
        writeToFile(dateStr + " - Money Added: Tk " + amount + ". New Balance: Tk " + balance.value);
    }

    private static void downloadApp() {
        String code = generateRandomString(6);
        System.out.println("Download link: https://ckash.com/download?code=" + code);
    }

    private static void showMyCkash(double balance) {
        System.out.printf("Your current balance is Tk %.2f.%n", balance);
        System.out.println("Recent Transactions:");
        try {
            List<String> transactions = Files.readAllLines(Paths.get("statements.txt"));
            transactions.stream().skip(Math.max(0, transactions.size() - 5)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("No recent transactions found.");
        }
    }

    private static int resetPin(Scanner scanner, int savedPin) {
        System.out.print("Enter Current PIN: ");
        int currentPin = scanner.nextInt();
        if (currentPin == savedPin) {
            System.out.print("Enter New PIN: ");
            int newPin = scanner.nextInt();
            System.out.print("Confirm New PIN: ");
            int confirmPin = scanner.nextInt();
            if (newPin == confirmPin) {
                System.out.println("PIN successfully reset.");
                return newPin;
            } else {
                System.out.println("PINs do not match. Try again.");
            }
        } else {
            System.out.println("Incorrect Current PIN.");
        }
        return savedPin;
    }

    private static void writeToFile(String text) {
        try (PrintWriter file = new PrintWriter(new FileWriter("statements.txt", true))) {
            file.println(text);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
