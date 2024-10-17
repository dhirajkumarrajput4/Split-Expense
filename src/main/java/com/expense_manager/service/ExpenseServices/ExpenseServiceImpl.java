package com.expense_manager.service.ExpenseServices;

import java.util.List;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.ArrayList;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense_manager.dtos.ExpenseDto;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.entities.Share;
import com.expense_manager.repository.ExpensesRepoes.ExpenseRepo;
import com.expense_manager.service.PersonService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PersonService personService;

    @Override
    public Expense createExpense(ExpenseDto expenseDto, Group group) {

        if (expenseDto.isEqualyShared()) {
            Integer membersCount = group.getMembers().size();

            Money perPersonAmount = expenseDto.getAmount().dividedBy(membersCount, RoundingMode.HALF_EVEN);

            List<Share> shares = new ArrayList<>();
            for (Person member : group.getMembers()) {
                Share share = new Share();
                share.setSharedAmount(perPersonAmount);
                share.setPerson(member);
                shares.add(share);
            }

            Expense expense = new Expense();
            expense.setAmount(expenseDto.getAmount());
            expense.setDescription(expenseDto.getDescription());
            expense.setExpenseCategory(expenseDto.getExpenseCategory());
            expense.setShares(shares);
            expense.setGroup(group);

            return expenseRepo.save(expense);
        }
        // return expenseRepo.save(expense);
        return null;
    }

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepo.findAll();
    }

    @Override
    public Optional<Expense> findExpenseById(Long id) {
        return expenseRepo.findById(id);
    }

    @Override
    public List<Expense> findExpensesByUser(Person person) {
        return expenseRepo.findByPaidBy(person);
    }

}
