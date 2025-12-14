package com.capgemini.expensetracker;

public class CategoryExtreme {

    private final String highestCategory;
    private final double highestAmount;
    private final String lowestCategory;
    private final double lowestAmount;

    public CategoryExtreme(String highestCategory, double highestAmount, String lowestCategory, double lowestAmount) {
        this.highestCategory = highestCategory;
        this.highestAmount = highestAmount;
        this.lowestCategory = lowestCategory;
        this.lowestAmount = lowestAmount;
    }

    // getters
    public String getHighestCategory() {
        return highestCategory;
    }

    public double getHighestAmount() {
        return highestAmount;
    }

    public String getLowestCategory() {
        return lowestCategory;
    }

    public double getLowestAmount() {
        return lowestAmount;
    }

}
