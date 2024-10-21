package com.expense_manager.service.ExpenseServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.expense_manager.comman.Mail;
import com.expense_manager.dtos.User;
import com.expense_manager.entities.Person;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.email.MailService;

@Service
public class LogInService {

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailService mailService;

    public Person createUser(User user) {

        Person newPerson = new Person(user.getFirstName(), user.getLastName(), user.getEmailId(),
                user.getPhoneNumber());
        newPerson.setRole("USER");
        newPerson.setPassword(passwordEncoder.encode("091234"));
        personService.savePerson(newPerson);
        Mail mail = successfullyRegistrationMailBody(user.getEmailId());
        mailService.sendEmail(mail);
        return newPerson;
    }

    public Mail successfullyRegistrationMailBody(String email) {
        // for send mail
        Mail mail = new Mail();
        mail.setMailTo(email);
        mail.setMailSubject("Welcome Split-Expense");
        // mail content
        Context context = new Context();
        context.setVariable("title", "Welcome to Split-Expense");
        context.setVariable("message", "You are successfully register with username " + email);

        String htmlContent = templateEngine.process("email/email-template", context);
        mail.setMailContent(htmlContent);
        return mail;
    }

}
