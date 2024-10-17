package com.expense_manager.dtos;

import org.joda.money.Money;

import com.expense_manager.entities.ExpenseCategory;
import com.expense_manager.entities.Person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {

    private String description;

    private ExpenseCategory expenseCategory;

    private Person createBy;

    private Person paidBy;

    private Long groupId;

    private Money amount;

    private Money sharedAmount;

    private boolean equalyShared;

}
