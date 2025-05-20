package ex;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private LocalDate date;
    private String type; // "INCOME" or "EXPENSE"
    private String category;
    private double amount;
    private String description;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Transaction(LocalDate date, String type, String category, double amount, String description) {
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return date.format(DATE_FORMATTER) + "," + type + "," + category + "," + amount + "," + description;
    }

    public static Transaction fromString(String transactionString) {
        String[] parts = transactionString.split(",", 5); // Limit split to 5 parts for description
        if (parts.length == 5) {
            LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
            String type = parts[1];
            String category = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String description = parts[4];
            return new Transaction(date, type, category, amount, description);
        }
        return null; // Or throw an exception for invalid format
    }
}