package com.capgemini.expensetracker;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerTest {
    private static final LocalDate DATE = LocalDate.of(2025, 12, 13);

    @Nested
    class AddExpenseTests {

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

            // empty str, 0 double, special chars, negative double
            assertAddExpense(tracker, "", 10.0, DATE);
            assertAddExpense(tracker, "Transport", 0.0, DATE);
            assertAddExpense(tracker, "(Groceries & Snacks)!", 10.0, DATE);
            assertAddExpense(tracker, "Entertainment", -25.0, DATE);
        }
    }

    @Nested
    class getTotalTests {
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
            tracker.addExpense("Food", 0.0, DATE);
            tracker.addExpense("Refund", -5.0, DATE);
            tracker.addExpense("Snack", 10.0, DATE);
            assertEquals(5.0, tracker.getTotal(), 0.001, "Total should sum positive, zero, and negative amounts");
        }
    }
    @Nested
    class getTotalByCategoryTests {
        // helper to assert totals by category
        private void assertTotals(Map<String, Double> totals, String category, double expected) {
            assertEquals(expected, totals.get(category), 0.001, "Total for " + category + " should match");
        }

        @Test
        void testGetTotalByCategory() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 12.50, LocalDate.of(2025, 12, 13));

            Map<String, Double> totals = tracker.getTotalByCategory();
            assertEquals(1, totals.size(), "Totals map should have 1 entry");
            assertTotals(totals, "Food", 12.50);
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
            tracker.addExpense("Food", 12.50, DATE);
            tracker.addExpense("Transport", 3.75, DATE);
            tracker.addExpense("Entertainment", 25.00, DATE);

            Map<String, Double> totals = tracker.getTotalByCategory();
            assertEquals(3, totals.size(), "Totals map should have 3 entries");
            assertTotals(totals, "Food", 12.50);
            assertTotals(totals, "Transport", 3.75);
            assertTotals(totals, "Entertainment", 25.00);
        }

        @Test
        void testGetTotalByCategoryMultipleSame() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 12.50, DATE);
            tracker.addExpense("Food", 3.75, DATE);

            Map<String, Double> totals = tracker.getTotalByCategory();
            assertEquals(1, totals.size(), "Totals map should have 1 entry");
            assertTotals(totals, "Food", 16.25);
        }

        @Test
        void testGetTotalByCategoryMixed() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 12.50, DATE);
            tracker.addExpense("Travel", 5.00, DATE);
            tracker.addExpense("Food", 2.50, DATE);
            tracker.addExpense("Travel", 3.75, DATE);

            Map<String, Double> totals = tracker.getTotalByCategory();

            assertEquals(2, totals.size(), "Totals map should have 2 entries");
            assertTotals(totals, "Food", 15.00);
            assertTotals(totals, "Travel", 8.75);
        }

        @Test
        void testGetTotalByCategorySpecial() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("", 10.0, DATE);
            tracker.addExpense("Refund", -5.0, DATE);
            tracker.addExpense("Food", 20.0, DATE);

            Map<String, Double> totals = tracker.getTotalByCategory();
            assertEquals(3, totals.size());
            assertTotals(totals, "", 10.0);
            assertTotals(totals, "Refund", -5.0);
            assertTotals(totals, "Food", 20.0);
        }
    }

    @Nested
    class getCategoryExtremesTests {
        @Test
        void testGetCategoryExtremes() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 12.50, DATE);

            Optional<CategoryExtreme> extremeOpt = tracker.getCategoryExtremes();
            assertTrue(extremeOpt.isPresent());
            CategoryExtreme extreme = extremeOpt.get();

            assertEquals("Food", extreme.getHighestCategory());
            assertEquals(12.50, extreme.getHighestAmount(), 0.001);
            assertEquals("Food", extreme.getLowestCategory());
            assertEquals(12.50, extreme.getLowestAmount(), 0.001);
        }

        @Test
        void testGetCategoryExtremesEmpty() {
            ExpenseTracker tracker = new ExpenseTracker();
            Optional<CategoryExtreme> extremeOpt = tracker.getCategoryExtremes();
            assertFalse(extremeOpt.isPresent(), "Should return empty Optional if no expenses exist");
        }

        @Test
        void testGetCategoryExtremesMultiple() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 12.50, DATE);
            tracker.addExpense("Travel", 5.00, DATE);
            tracker.addExpense("Entertainment", 25.00, DATE);

            Optional<CategoryExtreme> extremeOpt = tracker.getCategoryExtremes();
            assertTrue(extremeOpt.isPresent());
            CategoryExtreme extreme = extremeOpt.get();

            assertEquals("Entertainment", extreme.getHighestCategory());
            assertEquals(25.00, extreme.getHighestAmount(), 0.001);
            assertEquals("Travel", extreme.getLowestCategory());
            assertEquals(5.00, extreme.getLowestAmount(), 0.001);
        }

        @Test
        void testGetCategoryExtremesSums() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 10.0, DATE);
            tracker.addExpense("Food", 5.0, DATE);
            tracker.addExpense("Travel", 8.0, DATE);

            Optional<CategoryExtreme> extremeOpt = tracker.getCategoryExtremes();
            assertTrue(extremeOpt.isPresent());
            CategoryExtreme extreme = extremeOpt.get();

            assertEquals("Food", extreme.getHighestCategory());
            assertEquals(15.0, extreme.getHighestAmount(), 0.001);
            assertEquals("Travel", extreme.getLowestCategory());
            assertEquals(8.0, extreme.getLowestAmount(), 0.001);
        }

        @Test
        void testCategoryExtremesTiedAmounts() {
            ExpenseTracker tracker = new ExpenseTracker();
            tracker.addExpense("Food", 10.0, DATE);
            tracker.addExpense("Travel", 10.0, DATE);

            Optional<CategoryExtreme> extremeOpt = tracker.getCategoryExtremes();
            assertTrue(extremeOpt.isPresent(), "Optional should be present when expenses exist");
            CategoryExtreme extreme = extremeOpt.get();

            // Either "Food" or "Travel" is valid as highest/lowest
            assertTrue(
                    extreme.getHighestCategory().equals("Food") || extreme.getHighestCategory().equals("Travel"),
                    "Highest category should be either Food or Travel");
            assertTrue(
                    extreme.getLowestCategory().equals("Food") || extreme.getLowestCategory().equals("Travel"),
                    "Lowest category should be either 'Food' or 'Travel' for tied amounts");
            assertEquals(10.0, extreme.getHighestAmount(), 0.001, "Highest amount should match tied value 10.0");
            assertEquals(10.0, extreme.getLowestAmount(), 0.001, "Lowest amount should match tied value 10.0");
        }
    }

}