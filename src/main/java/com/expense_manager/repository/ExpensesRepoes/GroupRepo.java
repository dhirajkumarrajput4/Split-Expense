package com.expense_manager.repository.ExpensesRepoes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expense_manager.entities.Group;

public interface GroupRepo extends JpaRepository<Group, Long>{
    
}
