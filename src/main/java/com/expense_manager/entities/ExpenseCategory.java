package com.expense_manager.entities;

import com.expense_manager.comman.EnumUtils;

public enum ExpenseCategory {
    GENERAL("General"),
    DRINK("Drink"),
    FOOD("Food"),
    TRAVEL("Travel"),
    GROCERIES("Groceries");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public static ExpenseCategory fromDisplayName(String s) {
        return (ExpenseCategory) EnumUtils.lookupEnum(s, values(), "getDisplayName");
    }

    public String getDisplayName() {
        return displayName;
    }
}
