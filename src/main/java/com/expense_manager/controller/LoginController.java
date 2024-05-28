package com.expense_manager.controller;

import com.expense_manager.comman.Mail;
import com.expense_manager.dtos.User;
import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;
import com.expense_manager.security.JwtAuthenticationFilter;
import com.expense_manager.service.OtpService;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.email.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private PersonService personService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (!validateUser(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First name and phone number can't be empty");
        }
        Optional<Person> person = personService.findByPhone(user.getPhoneNumber());
        if (person.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already exist");
        }
        Person newPerson = new Person(user.getFirstName(), user.getLastName(), user.getEmailId(), user.getPhoneNumber());
        personService.savePerson(newPerson);
        return ResponseEntity.status(HttpStatus.CREATED).body("User register successfully");
    }

    @PostMapping("/triggerOtp")
    public ResponseEntity<String> triggerOtp(@RequestParam("mobileNumber") String mobile) {
        Person person = personService.findByPhone(mobile).orElse(null);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not registered !");
        }
        Integer otp = generateOTP();
        //Generate mail with html body
        Mail mail = generateMailWithBody(otp, person.getEmailId());
        mailService.sendEmail(mail);
        return ResponseEntity.status(HttpStatus.CONTINUE).body("Otp send successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String phone, @RequestParam Integer otp) {
        Person person = personService.findByPhone(phone).orElse(null);
        if (person == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not registered !");
        }
        Optional<Otp> otpEntity = otpService.findByPersonAndOtp(person, otp);

        if (!otpEntity.isEmpty() && otpEntity.get().getExpiration().isAfter(LocalDateTime.now())) {
            // If OTP is valid and not expired, generate JWT token
            String token = JwtAuthenticationFilter.generateToken(person);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }


    private boolean validateUser(User user) {
        return user.getFirstName() != null && user.getPhoneNumber() != null;
    }

    private static Integer generateOTP() {
        return new Random().nextInt(900000) + 100000;
    }


    private Mail generateMailWithBody(Integer otp, String email) {
        //for send mail
        Mail mail = new Mail();
        mail.setMailTo(email);
        mail.setMailSubject("Split-Expense OTP");
        //mail content
        Context context = new Context();
        context.setVariable("title", "Welcome to Split-Expense");
        context.setVariable("message", "Your OTP is : " + otp);

        String htmlContent = templateEngine.process("email/email-template", context);
        mail.setMailContent(htmlContent);
        return mail;
    }
}
