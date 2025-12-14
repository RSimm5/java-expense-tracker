package com.capgemini.expensetracker;

import java.time.LocalDate;

public class Expense {
    private final String category;
    private final double amount;
    private final LocalDate date;

    public Expense(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // getters
    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
