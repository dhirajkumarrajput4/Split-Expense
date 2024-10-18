package com.expense_manager.service.email;

import com.expense_manager.comman.Mail;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Person;


public interface MailService {
    
    public void sendEmail(Mail mail);

    public Mail createEmailForExpenseNotification(Expense expense, Person person);
}
