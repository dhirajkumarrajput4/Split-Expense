package com.expense_manager.resonses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private Long groupId;
    private String expenseName;
    private String groupName;
    private Double willPay;
    private Double willGet;
    private Double totaleExpense;
}
