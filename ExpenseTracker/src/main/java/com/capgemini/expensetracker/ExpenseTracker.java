package com.capgemini.expensetracker;

import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    private List<Expense> expenses;

    public ExpenseTracker() {
        expenses = new ArrayList<>();
    }

    // adds Expense to expenses list
    public void addExpense(String category, double amount, LocalDate date) {
        Expense expense = new Expense(category, amount, date);
        expenses.add(expense);
    }

    // returns total amount
    public double getTotal() {
        double total = 0.0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    // returns totals grouped by category
    public Map<String, Double> getTotalByCategory() {
        Map<String, Double> totals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            totals.put(
                    category,
                    totals.getOrDefault(category, 0.0) + amount
            );
        }
        return totals;
    }

    // getter
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    // shows category with highest and lowest total expense amounts
    public Optional<CategoryExtreme> getCategoryExtremes() {
        Map<String, Double> totals = getTotalByCategory();

        if (totals.isEmpty()) {
            return Optional.empty();
        }

        String highestCategory = null;
        String lowestCategory = null;
        double highestAmount = Double.NEGATIVE_INFINITY;
        double lowestAmount = Double.POSITIVE_INFINITY;

        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            if (amount > highestAmount) {
                highestCategory = category;
                highestAmount = amount;
            }
            if (amount < lowestAmount) {
                lowestCategory = category;
                lowestAmount = amount;
            }
        }

        return Optional.of(new CategoryExtreme(highestCategory, highestAmount, lowestCategory, lowestAmount));
    }
}
