package com.expense_manager.service.ExpenseServices;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense_manager.comman.Job;
import com.expense_manager.comman.Mail;
import com.expense_manager.dtos.ExpenseDto;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.entities.Share;
import com.expense_manager.repository.ExpensesRepoes.ExpenseRepo;
import com.expense_manager.resonses.ExpenseResponse;
import com.expense_manager.service.AmqpServices.MessageProducer;
import com.expense_manager.service.email.MailService;

import jakarta.transaction.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MailService mailService;

    @Override
    @Transactional
    public Expense createExpense(ExpenseDto expenseDto, Group group) {
        if (expenseDto.isEqualyShared()) {
            Integer membersCount = group.getMembers().size();

            double dividedAmount = expenseDto.getAmount() / membersCount;
            
            BigDecimal bd = new BigDecimal(Double.toString(dividedAmount));
            bd = bd.setScale(2, RoundingMode.HALF_UP); 

            Money perPersonAmount = Money.of(CurrencyUnit.of("INR"), bd.doubleValue());

            Money totalPaidAmount = Money.of(CurrencyUnit.of("INR"), expenseDto.getAmount());

            // Money perPersonAmount = expenseDto.getAmount().dividedBy(membersCount,
            // RoundingMode.HALF_EVEN);.

            Expense expense = new Expense();
            expense.setAmount(totalPaidAmount);
            expense.setDescription(expenseDto.getDescription());
            expense.setExpenseCategory(expenseDto.getExpenseCategory());
            expense.setPaidBy(expenseDto.getPaidBy());
            expense.setCreateBy(expenseDto.getCreateBy());
            expense.setGroup(group);
            List<Share> shares = new ArrayList<>();
            for (Person member : group.getMembers()) {
                Share share = new Share();
                share.setSharedAmount(perPersonAmount);
                share.setPerson(member);
                share.setExpense(expense);
                shares.add(share);
            }
            expense.setShares(shares);
            expenseRepo.save(expense);

            sendEmailNotificationToGroupMembersJob(expense);

            return expense;
        }
        // return expenseRepo.save(expense);
        return null;
    }

    private void sendEmailNotificationToGroupMembersJob(Expense expense) {
        Job job = new Job("ExpenseNotificationJob", expense.getLockKey());
        job.put("expenseId", expense.getId());
        this.messageProducer.sendMessage(job);
    }

    @Transactional
    public void sendEmailNotificationToGroupMembers(Expense expense) {
        for (Person person : expense.getGroup().getMembers()) {
            // if(person.equals(expense.getCreateBy())) {
            sendEmailNotification(expense, person);
            // }

        }
    }

    public void sendEmailNotification(Expense expense, Person person) {
        Mail mail = this.mailService.createEmailForExpenseNotification(expense, person);
        this.mailService.sendEmail(mail);
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

    public List<Expense> findGroupExpense(Group group) {
        return this.expenseRepo.findByGroup(group);
    }

    public List<ExpenseResponse> fetchExpeseWithDetails(Group group, Person person) {

        List<Expense> expenses = this.expenseRepo.findByGroup(group);

        List<ExpenseResponse> expenseReponses = calculateExpense(expenses, person);

        return expenseReponses;

    }

    private List<ExpenseResponse> calculateExpense(List<Expense> expenses, Person person) {
        List<ExpenseResponse> expenseResponses = new ArrayList<>();
        for (Expense expense : expenses) {
            Double shared = expense.getShares().stream().filter(share -> share.getPerson().equals(person)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Shared not found of user"))
                    .getSharedAmount().getAmount().doubleValue();

            Double willPay = shared;
            Double willGet = 0.0;
            if (expense.getPaidBy().equals(person)) {
                willPay = 0.0;
                willGet = (expense.getAmount().getAmount().doubleValue()) - shared;
            }
            expenseResponses.add(
                    new ExpenseResponse(expense.getId(), expense.getGroup().getId(), expense.getDescription(),
                            expense.getGroup().getGroupName(), willPay, willGet,expense.getAmount().getAmount().doubleValue()));
        }
        return expenseResponses;
    }

}
