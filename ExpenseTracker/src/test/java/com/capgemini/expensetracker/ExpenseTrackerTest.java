package com.capgemini.expensetracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerTest {

    /* === addExpense tests === */

    // helper for single additions
    private void assertAddExpense(ExpenseTracker tracker, String category, double amount, LocalDate date) {
        tracker.addExpense(category, amount, date);
        Expense e = tracker.getExpenses().get(tracker.getExpenses().size() - 1);
        assertEquals(category, e.getCategory());
        assertEquals(amount, e.getAmount(), 0.001);
        assertEquals(date, e.getDate());
    }

    @Test
    void testAddExpense() {
        ExpenseTracker tracker = new ExpenseTracker();
        assertAddExpense(tracker, "Food", 12.50, LocalDate.of(2025, 12, 13));
        assertEquals(1, tracker.getExpenses().size(), "Should have 1 expense");
    }

    @Test
    void testAddExpenseMultiple() {
        ExpenseTracker tracker = new ExpenseTracker();

        // Arrange
        String[] categories = {"Food", "Transport", "Entertainment"};
        double[] amounts = {12.50, 3.75, 25.00};
        LocalDate[] dates = {
                LocalDate.of(2025, 12, 13),
                LocalDate.of(2025, 12, 14),
                LocalDate.of(2025, 12, 15)
        };

        // Act
        for (int i = 0; i < categories.length; i++) {
            tracker.addExpense(categories[i], amounts[i], dates[i]);
        }

        // Assert
        List<Expense> expenses = tracker.getExpenses();
        assertEquals(3, expenses.size(), "Should have 3 expenses");

        for (int i = 0; i < categories.length; i++) {
            Expense e = expenses.get(i);
            assertEquals(categories[i], e.getCategory(), "Category should match");
            assertEquals(amounts[i], e.getAmount(), 0.001, "Amount should match");
            assertEquals(dates[i], e.getDate(), "Date should match");
        }
    }

    @Test
    void testAddExpenseSpecial() {
        ExpenseTracker tracker = new ExpenseTracker();
        LocalDate today = LocalDate.now();

        // empty str, 0 double, special chars, negative double
        assertAddExpense(tracker, "", 10.0, today);
        assertAddExpense(tracker, "Transport", 0.0, today);
        assertAddExpense(tracker, "(Groceries & Snacks)!", 10.0, today);
        assertAddExpense(tracker, "Entertainment", -25.0, today);
    }

    /* === getTotal tests === */
    @Test
    void testGetTotal() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.of(2025, 12, 13));
        assertEquals(12.50, tracker.getTotal(), 0.001, "Total should match single expense amount");
    }

    @Test
    void testGetTotalEmpty() {
        ExpenseTracker tracker = new ExpenseTracker();
        assertEquals(0.0, tracker.getTotal(), 0.001, "Total should be 0.0 for empty tracker");
    }

    @Test
    void testGetTotalMultiple() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.of(2025, 12, 13));
        tracker.addExpense("Transport", 3.75, LocalDate.of(2025, 12, 14));
        tracker.addExpense("Entertainment", 25.00, LocalDate.of(2025, 12, 15));
        assertEquals(41.25, tracker.getTotal(), 0.001, "Total should sum all expenses");
    }

    @Test
    void testGetTotalSpecial() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 0.0, LocalDate.now());
        tracker.addExpense("Refund", -5.0, LocalDate.now());
        tracker.addExpense("Snack", 10.0, LocalDate.now());
        assertEquals(5.0, tracker.getTotal(), 0.001, "Total should sum positive, zero, and negative amounts");
    }

    /* === getTotalByCategory tests === */
    @Test
    void testGetTotalByCategory() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.of(2025, 12, 13));

        Map<String, Double> totals = tracker.getTotalByCategory();
        assertEquals(1, totals.size(), "Totals map should have 1 entry");
        assertEquals(12.50, totals.get("Food"), 0.001, "Total for Food should be 12.50");
    }

    @Test
    void testGetTotalByCategoryEmpty() {
        ExpenseTracker tracker = new ExpenseTracker();
        Map<String, Double> totals = tracker.getTotalByCategory();
        assertTrue(totals.isEmpty(), "Totals map should be empty when no expenses exist");
    }

    @Test
    void testGetTotalByCategoryMultiple() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.now());
        tracker.addExpense("Transport", 3.75, LocalDate.now());
        tracker.addExpense("Entertainment", 25.00, LocalDate.now());

        Map<String, Double> totals = tracker.getTotalByCategory();
        assertEquals(3, totals.size(), "Totals map should have 3 entries");
        assertEquals(12.50, totals.get("Food"), 0.001);
        assertEquals(3.75, totals.get("Transport"), 0.001);
        assertEquals(25.00, totals.get("Entertainment"), 0.001);
    }

    @Test
    void testGetTotalByCategoryMultipleSame() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.now());
        tracker.addExpense("Food", 3.75, LocalDate.now());

        Map<String, Double> totals = tracker.getTotalByCategory();
        assertEquals(1, totals.size(), "Totals map should have 1 entry");
        assertEquals(16.25, totals.get("Food"), 0.001, "Total for Food should sum correctly");
    }

    @Test
    void testGetTotalByCategoryMixed() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.now());
        tracker.addExpense("Travel", 5.00, LocalDate.now());
        tracker.addExpense("Food", 2.50, LocalDate.now());
        tracker.addExpense("Travel", 3.75, LocalDate.now());

        Map<String, Double> totals = tracker.getTotalByCategory();

        assertEquals(2, totals.size(), "Totals map should have 2 entries");
        assertEquals(15.00, totals.get("Food"), 0.001, "Total for Food should sum correctly");
        assertEquals(8.75, totals.get("Travel"), 0.001, "Total for Travel should sum correctly");
    }

    @Test
    void testGetTotalByCategorySpecial() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("", 10.0, LocalDate.now());
        tracker.addExpense("Refund", -5.0, LocalDate.now());
        tracker.addExpense("Food", 20.0, LocalDate.now());

        Map<String, Double> totals = tracker.getTotalByCategory();
        assertEquals(3, totals.size());
        assertEquals(10.0, totals.get(""), 0.001);
        assertEquals(-5.0, totals.get("Refund"), 0.001);
        assertEquals(20.0, totals.get("Food"), 0.001);
    }

    /* === getCategoryExtremes tests === */
    @Test
    void TestGetCategoryExtremes() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.now());

        CategoryExtreme extreme = tracker.getCategoryExtremes();
        assertNotNull(extreme);
        assertEquals("Food", extreme.getHighestCategory());
        assertEquals(12.50, extreme.getHighestAmount(), 0.001);
        assertEquals("Food", extreme.getLowestCategory());
        assertEquals(12.50, extreme.getLowestAmount(), 0.001);
    }

    @Test
    void testGetCategoryExtremesEmpty() {
        ExpenseTracker tracker = new ExpenseTracker();
        assertNull(tracker.getCategoryExtremes(), "Should return null if no expenses exist");
    }

    @Test
    void testGetCategoryExtremesMultiple() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 12.50, LocalDate.now());
        tracker.addExpense("Travel", 5.00, LocalDate.now());
        tracker.addExpense("Entertainment", 25.00, LocalDate.now());

        CategoryExtreme extreme = tracker.getCategoryExtremes();
        assertNotNull(extreme);
        assertEquals("Entertainment", extreme.getHighestCategory());
        assertEquals(25.00, extreme.getHighestAmount(), 0.001);
        assertEquals("Travel", extreme.getLowestCategory());
        assertEquals(5.00, extreme.getLowestAmount(), 0.001);
    }

    @Test
    void testGetCategoryExtremesSums() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 10.0, LocalDate.now());
        tracker.addExpense("Food", 5.0, LocalDate.now());
        tracker.addExpense("Travel", 8.0, LocalDate.now());

        CategoryExtreme extreme = tracker.getCategoryExtremes();
        assertNotNull(extreme);
        assertEquals("Food", extreme.getHighestCategory()); // 10 + 5 = 15
        assertEquals(15.0, extreme.getHighestAmount(), 0.001);
        assertEquals("Travel", extreme.getLowestCategory());
        assertEquals(8.0, extreme.getLowestAmount(), 0.001);
    }

    @Test
    void testCategoryExtremesTiedAmounts() {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.addExpense("Food", 10.0, LocalDate.now());
        tracker.addExpense("Travel", 10.0, LocalDate.now());

        CategoryExtreme extreme = tracker.getCategoryExtremes();
        assertNotNull(extreme);
        // Either "Food" or "Travel" is valid as highest/lowest
        assertTrue(extreme.getHighestCategory().equals("Food") || extreme.getHighestCategory().equals("Travel"));
        assertTrue(extreme.getLowestCategory().equals("Food") || extreme.getLowestCategory().equals("Travel"));
        assertEquals(10.0, extreme.getHighestAmount(), 0.001);
        assertEquals(10.0, extreme.getLowestAmount(), 0.001);
    }

}