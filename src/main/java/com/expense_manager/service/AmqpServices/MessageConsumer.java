package com.expense_manager.service.AmqpServices;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense_manager.comman.Job;
import com.expense_manager.entities.Expense;
import com.expense_manager.repository.ExpensesRepoes.ExpenseRepo;
import com.expense_manager.service.ExpenseServices.ExpenseService;

@Service
public class MessageConsumer {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepo expenseRepo;

    @RabbitListener(queues = "myQueue")
    public <T> void receivedMessage(Job job) {

        System.out.println("message is : " + job.toString());
        Long expenseId = job.getLong("expenseId");

        Expense expense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("No expense found with this id"));

        expenseService.sendEmailNotificationToGroupMembers(expense);

    }
}
