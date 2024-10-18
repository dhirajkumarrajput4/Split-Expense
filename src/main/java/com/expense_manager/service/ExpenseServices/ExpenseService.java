package com.expense_manager.service.ExpenseServices;

import java.util.List;
import java.util.Optional;

import com.expense_manager.dtos.ExpenseDto;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;

public interface ExpenseService {

    public Expense createExpense(ExpenseDto expenseDto, Group group);

    List<Expense> findAllExpenses();

    Optional<Expense> findExpenseById(Long id);

    List<Expense> findExpensesByUser(Person person);

    void sendEmailNotificationToGroupMembers(Expense expense);
}
