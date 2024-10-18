package com.expense_manager.service.email;

import com.expense_manager.comman.Mail;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Person;
import com.expense_manager.entities.Share;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendEmail(Mail mail) {
        // mail delivered from this mail id
        mail.setMailFrom("mydesktop2662@gmail.com");

        String to = mail.getMailTo();
        String cc = mail.getMailCc();
        String bcc = mail.getMailBcc();
        String subject = mail.getMailSubject();
        String sender = mail.getMailFrom();
        String bodyHtml = mail.getMailContent();
        List<String> fileAttachments = null;

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // Add subject, from and to lines.
            message.setSubject(subject, "UTF-8");
            message.setFrom(new InternetAddress(sender));
            if (StringUtils.hasLength(to)) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            if (StringUtils.hasLength(cc)) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            if (StringUtils.hasLength(bcc)) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }

            // Create a multipart/alternative child container.

            MimeMultipart msg_body = new MimeMultipart("alternative");

            // Define the HTML part.
            if (bodyHtml != null) {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(bodyHtml, "text/html; charset=UTF-8");
                msg_body.addBodyPart(htmlPart);
            }

            // Add file attachments
            if (fileAttachments != null) {
                for (String filePath : fileAttachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(new File(filePath));
                    msg_body.addBodyPart(attachmentPart);
                }
            }
            // Add the multipart/alternative container to the message.
            message.setContent(msg_body);

            // Send the message.
            javaMailSender.send(message);

        } catch (Exception ex) {
            throw new RuntimeException(String.format("unable to send mail to %s cc %s bcc %s subject %s error %s", to,
                    cc, bcc, subject, ex.getMessage()), ex);
        }
    }

    public Mail createEmailForExpenseNotification(Expense expense, Person person) {

        Mail mail = new Mail();
        mail.setMailTo(person.getEmailId());
        mail.setMailSubject(
                "New expense added by - " + expense.getCreateBy().getFirstName() + " Amount : " + expense.getAmount().getAmount());

        // mail content
        // Context context = new Context();
        // context.setVariable("title", "Expense Notification");
        // context.setVariable("message", getNotificationHtml(expense, person));
        // String htmlContent = templateEngine.process("email/email-template", context);

        mail.setMailContent(getNotificationHtml(expense, person));
        return mail;

    }

    private String getNotificationHtml(Expense expense, Person person) {

        String groupName = expense.getGroup().getGroupName();
        String personName = person.getFirstName()==null ? person.getEmailId() : person.getFirstName();

        String expenseDescription = expense.getDescription();
        String totalAmount = expense.getAmount().getAmount().toPlainString();
        // Find the share for the person, if present
        Optional<Share> optionalShare = expense.getShares().stream()
                .filter(share -> share.getPerson().getId().equals(person.getId()))
                .findFirst();

        // Handle the case where no share is found
        String owedAmount = optionalShare
                .map(share -> share.getSharedAmount().getAmount().toPlainString())
                .orElse("0.00"); // Provide a default value or handle accordingly

        // HTML message
        String htmlMessage = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Expense Notification</title>
                        <style>
                            body {font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;}
                            .container {max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; border-radius: 8px;}
                            h2 {color: #333333;}
                            p {color: #555555; line-height: 1.6;}
                            .amount {color: #28a745; font-weight: bold;}
                            .owed {color: #dc3545; font-weight: bold;}
                            .details {margin-top: 20px; padding: 15px; background-color: #f9f9f9; border-left: 4px solid #007bff;}
                            .footer {margin-top: 20px; padding-top: 10px; border-top: 1px solid #eeeeee; color: #777777; font-size: 12px;}
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h2>Expense Created Notification</h2>
                            <p>Hello <strong>%s</strong>,</p>
                            <p>A new expense has been created in your group, and you are responsible for a portion of the payment. Below are the details of the expense:</p>

                            <div class="details">
                                <p><strong>Expense Group:</strong> %s</p>
                                <p><strong>Expense Description:</strong> %s</p>
                                <p><strong>Total Amount:</strong> <span class="amount">%s INR</span></p>
                                <p><strong>Amount You Owe:</strong> <span class="owed">%s INR</span></p>
                            </div>

                            <p>Please make sure to settle your portion of the expense as soon as possible.</p>

                            <p>If you have any questions, feel free to reach out to the expense creator or your group administrator.</p>

                            <div class="footer">
                                <p>This is an automated message. Please do not reply directly to this email.</p>
                                <p>&copy; 2024 Expense Manager. All Rights Reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                """
                .formatted(personName,groupName,expenseDescription, totalAmount, owedAmount);

        return htmlMessage;
    }
}
