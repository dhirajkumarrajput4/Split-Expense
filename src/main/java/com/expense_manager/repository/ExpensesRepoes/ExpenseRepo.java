package com.expense_manager.repository.ExpensesRepoes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Person;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Long>{
    
    List<Expense> findByPaidBy(Person person);


    
}
