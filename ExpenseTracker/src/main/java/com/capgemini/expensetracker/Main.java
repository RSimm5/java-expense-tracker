package com.capgemini.expensetracker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Expense Tracker App!");

        Scanner scanner = new Scanner(System.in);
        ExpenseTracker expenseTracker = new ExpenseTracker();

        while (true) {
            System.out.print(
                "\n1. Add expense\n" +
                "2. View total expense\n" +
                "3. View total expense by category\n" +
                "4. View expense trend\n" +
                "5. View highest and lowest spend category\n" +
                "6. Exit\n" +
                "Select an option (1-6): "
            );

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid option. Try again.");
                scanner.nextLine();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            System.out.println(); // extra space for readability

            switch (choice) {
                case 1:
                    addExpense(scanner, expenseTracker);
                    break;
                case 2:
                    showTotal(expenseTracker);
                    break;
                case 3:
                    showTotalByCategory(expenseTracker);
                    break;
                case 4:
                    showTrends(expenseTracker);
                    break;
                case 5:
                    showHighestLowestCategory(expenseTracker);
                    break;
                case 6:
                    System.out.println("Thank you for using the Expense Tracker App!");
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }

        }
    }

    // scanner for user inputs, ExpenseTracker to add Expense
    // Uses simple validity checks
    public static void addExpense(Scanner scanner, ExpenseTracker expenseTracker) {
        // get category
        System.out.print("Enter a category: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty!");
            return;
        }

        // get amount
        System.out.print("Enter an amount: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount!");
            scanner.nextLine();
            return;
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }

        // get date
        System.out.print("Enter a date (YYYY-MM-DD): ");
        String dateInput = scanner.next();
        LocalDate date;
        try {
            date = LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format!");
            return;
        }

        // add the expense
        expenseTracker.addExpense(category, amount, date);
        System.out.println("Successfully added expense.");
    }

    // displays total amount
    public static void showTotal(ExpenseTracker expenseTracker) {
        double total = expenseTracker.getTotal();
        System.out.println("Total expenses: " + String.format("%.2f", total));
    }

    // displays totals grouped by category
    public static void showTotalByCategory(ExpenseTracker expenseTracker) {
        Map<String, Double> totals = expenseTracker.getTotalByCategory();

        if (totals.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("=== Total Expenses by Category ===");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            System.out.printf("%-15s : %.2f%n", category, amount);
        }
    }

    // displays all expenses sorted by date, oldest to newest
    public static void showTrends(ExpenseTracker expenseTracker) {
        // sort by date
        List<Expense> expenses = expenseTracker.getExpenses()
            .stream()
            .sorted(Comparator.comparing(Expense::getDate))
            .collect(Collectors.toList());

        // output
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.printf("%-12s | %-15s | %10s%n", "Date", "Category", "Amount");
        System.out.println("-------------------------------------------");
        for (Expense expense : expenses) {
            System.out.printf("%-12s | %-15s | %10.2f%n",
                expense.getDate(),
                expense.getCategory(),
                expense.getAmount());
        }
    }

    // displays category with highest and lowest total expense amounts
    public static void showHighestLowestCategory(ExpenseTracker expenseTracker) {
        Optional<CategoryExtreme> categoryExtremeOpt = expenseTracker.getCategoryExtremes();
        if (!categoryExtremeOpt.isPresent()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        CategoryExtreme categoryExtreme = categoryExtremeOpt.get();
        System.out.println("Category with highest expense:");
        System.out.printf("%-15s : %.2f%n", categoryExtreme.getHighestCategory(), categoryExtreme.getHighestAmount());
        System.out.println("Category with lowest expense:");
        System.out.printf("%-15s : %.2f%n", categoryExtreme.getLowestCategory(), categoryExtreme.getLowestAmount());
    }
}