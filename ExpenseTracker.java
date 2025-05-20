package ex;
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseTracker {
    private List<Transaction> transactions;
    private static final String DATA_FILE = "initial_transactions.txt";

    public ExpenseTracker() {
        this.transactions = new ArrayList<>();
        loadData(); // Load data on initialization
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        saveData(); // Save data immediately after adding
    }

    public void loadData() {
        transactions.clear(); // Clear existing data before loading
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = Transaction.fromString(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
            System.out.println("Data loaded successfully from " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting with an empty tracker.");
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadDataFromFile(String filePath) {
        // This method is specifically for loading initial data from an *additional* file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = Transaction.fromString(line);
                if (transaction != null) {
                    transactions.add(transaction);
                    count++;
                } else {
                    System.err.println("Skipping malformed line in input file: " + line);
                }
            }
            System.out.println(count + " transactions loaded from input file: " + filePath);
            saveData(); // Save combined data
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }


    public void generateMonthlySummary(int year, int month) {
        YearMonth targetMonth = YearMonth.of(year, month);

        List<Transaction> monthlyTransactions = transactions.stream()
                .filter(t -> YearMonth.from(t.getDate()).equals(targetMonth))
                .collect(Collectors.toList());

        double totalIncome = monthlyTransactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = monthlyTransactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.println("\n--- Monthly Summary for " + targetMonth.getMonth() + " " + year + " ---");
        System.out.printf("Total Income: %.2f%n", totalIncome);
        System.out.printf("Total Expense: %.2f%n", totalExpense);
        System.out.printf("Net Savings: %.2f%n", (totalIncome - totalExpense));
        System.out.println("----------------------------------------");

        if (monthlyTransactions.isEmpty()) {
            System.out.println("No transactions recorded for this month.");
        } else {
            System.out.println("\nDetailed Transactions:");
            System.out.printf("%-12s %-8s %-15s %-10s %s%n", "Date", "Type", "Category", "Amount", "Description");
            System.out.println("------------------------------------------------------------------");
            monthlyTransactions.forEach(t ->
                    System.out.printf("%-12s %-8s %-15s %-10.2f %s%n",
                            t.getDate(), t.getType(), t.getCategory(), t.getAmount(), t.getDescription())
            );
            System.out.println("------------------------------------------------------------------");
        }
    }
}