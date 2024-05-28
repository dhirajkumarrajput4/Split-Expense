package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import com.expense_manager.comman.MoneyAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.money.Money;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@jakarta.persistence.Entity
@Table(name = "expense")
public class Expense extends Entity {

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory expenseCategory;

    @Column(name = "amount")
    @Convert(converter = MoneyAttributeConverter.class)
    private Money amount;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person createBy;

    @ManyToOne
    @JoinColumn(name = "paid_by_id")
    private Person paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<Share> shares;
}
