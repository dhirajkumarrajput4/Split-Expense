package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import com.expense_manager.comman.MoneyAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.money.Money;

@jakarta.persistence.Entity
@Table(name = "expenseShare")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseShare extends Entity {

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "sharedAmount")
    @Convert(converter = MoneyAttributeConverter.class)
    private Money sharedAmount;
}
