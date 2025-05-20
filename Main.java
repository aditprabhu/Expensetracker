package ex;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = new ExpenseTracker();

        // Prompt to load data from an additional file at startup
        System.out.print("Do you want to load transactions from an external file? (yes/no): ");
        String loadFromFileChoice = scanner.nextLine().trim().toLowerCase();
        if (loadFromFileChoice.equals("yes")) {
            System.out.print("Enter the path to the input file (e.g., transactions_initial.txt): ");
            String filePath = scanner.nextLine();
            tracker.loadDataFromFile(filePath);
        }

        int choice;
        do {
            System.out.println("\n--- Expense Tracker Menu ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addTransaction(scanner, tracker, "INCOME");
                        break;
                    case 2:
                        addTransaction(scanner, tracker, "EXPENSE");
                        break;
                    case 3:
                        viewMonthlySummary(scanner, tracker);
                        break;
                    case 4:
                        System.out.println("Exiting Expense Tracker. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                choice = 0; // Set to 0 to re-enter loop
            }
        } while (choice != 4);

        scanner.close();
    }

    private static void addTransaction(Scanner scanner, ExpenseTracker tracker, String type) {
        System.out.println("\n--- Add " + type + " ---");
        LocalDate date;
        while (true) {
            System.out.print("Enter date (YYYY-MM-DD, leave blank for today): ");
            String dateStr = scanner.nextLine();
            if (dateStr.isEmpty()) {
                date = LocalDate.now();
                break;
            }
            try {
                date = LocalDate.parse(dateStr);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        String category = "";
        if (type.equals("INCOME")) {
            while (true) {
                System.out.print("Enter category (Salary, Business, Other): ");
                category = scanner.nextLine().trim();
                if (category.equalsIgnoreCase("Salary") || category.equalsIgnoreCase("Business") || category.equalsIgnoreCase("Other")) {
                    break;
                } else {
                    System.out.println("Invalid income category. Please choose from Salary, Business, Other.");
                }
            }
        } else { // EXPENSE
            while (true) {
                System.out.print("Enter category (Food, Rent, Travel, Utilities, Entertainment, Other): ");
                category = scanner.nextLine().trim();
                if (category.equalsIgnoreCase("Food") || category.equalsIgnoreCase("Rent") ||
                        category.equalsIgnoreCase("Travel") || category.equalsIgnoreCase("Utilities") ||
                        category.equalsIgnoreCase("Entertainment") || category.equalsIgnoreCase("Other")) {
                    break;
                } else {
                    System.out.println("Invalid expense category. Please choose from Food, Rent, Travel, Utilities, Entertainment, Other.");
                }
            }
        }

        double amount;
        while (true) {
            System.out.print("Enter amount: ");
            try {
                amount = scanner.nextDouble();
                if (amount <= 0) {
                    System.out.println("Amount must be positive.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid amount. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        scanner.nextLine(); // Consume newline after amount

        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine().trim();

        Transaction transaction = new Transaction(date, type.toUpperCase(), category, amount, description);
        tracker.addTransaction(transaction);
        System.out.println(type + " added successfully!");
    }

    private static void viewMonthlySummary(Scanner scanner, ExpenseTracker tracker) {
        System.out.println("\n--- View Monthly Summary ---");
        int year;
        while (true) {
            System.out.print("Enter year (e.g., 2023): ");
            try {
                year = scanner.nextInt();
                if (year < 1900 || year > 2100) { // Basic validation
                    System.out.println("Please enter a realistic year.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid year. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        int month;
        while (true) {
            System.out.print("Enter month (1-12): ");
            try {
                month = scanner.nextInt();
                if (month < 1 || month > 12) {
                    System.out.println("Month must be between 1 and 12.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid month. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        scanner.nextLine(); // Consume newline

        tracker.generateMonthlySummary(year, month);
    }
}