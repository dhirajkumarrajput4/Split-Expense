package com.expense_manager.controller;

import com.expense_manager.comman.Mail;
import com.expense_manager.dtos.JwtResponse;
import com.expense_manager.dtos.User;
import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;
import com.expense_manager.security.JwtAuthenticationFilter;
import com.expense_manager.security.JwtHelper;
import com.expense_manager.service.OtpService;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.email.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/auth/")
public class LoginController {

    @Autowired
    private PersonService personService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("helloWorld")
    public ResponseEntity<String> getResponse() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Helle world this testing");
    }

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
        newPerson.setRole("USER");
        newPerson.setPassword(passwordEncoder.encode("091234"));
        personService.savePerson(newPerson);
        Mail mail = successfullyRegistrationMailBody(user.getEmailId());
        mailService.sendEmail(mail);
        return ResponseEntity.status(HttpStatus.CREATED).body("User register successfully");
    }

    @PostMapping("/triggerOtp")
    public ResponseEntity<String> triggerOtp(@RequestParam("phoneEmail") String phoneEmail) {
        Person person = null;
        if (phoneEmail.contains("@")) {
            person = personService.findByEmailId(phoneEmail).orElse(null);
        } else {
            person = personService.findByPhone(phoneEmail).orElse(null);
        }
        if (person == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not registered !");
        }
        Integer otp = generateOTP();
        Otp otpEntity = new Otp(person, otp);
        otpEntity.expireAfter(60, ChronoUnit.HOURS);
        otpService.save(otpEntity);
        //Generate mail with html body
        LOGGER.info("OTP: " + otp);
        if (phoneEmail.contains("@")) {
            Mail mail = generateMailWithBody(otp, person.getEmailId());
            mailService.sendEmail(mail);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Otp send successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestParam String phoneEmail, @RequestParam Integer otp) {
        Person person = null;
        if (phoneEmail.contains("@")) {
            person = personService.findByEmailId(phoneEmail).orElse(null);
        } else {
            person = personService.findByPhone(phoneEmail).orElse(null);
        }
        if (person == null) {
            throw new BadCredentialsException(" Invalid Username or OTP  !!");
        }

        Optional<Otp> otpEntity = otpService.findByPersonAndOtp(person, otp);
        if (otpEntity.isPresent() && otpEntity.get().getExpiration().isAfter(LocalDateTime.now())) {
            doAuthenticate(phoneEmail);
        } else {
            throw new BadCredentialsException(" Invalid OTP  !!");
        }
        UserDetails userDetails = null;
        if (phoneEmail.contains("@")) {
            userDetails = userDetailsService.loadUserByUsername(phoneEmail);
        } else {
            userDetails = userDetailsService.loadUserByUsername(phoneEmail);
        }

        if (otpEntity.get().getExpiration().isAfter(LocalDateTime.now())) {
            // If OTP is valid and not expired, generate JWT token
            String token = this.jwtHelper.generateToken(userDetails);
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername()).build();
            return ResponseEntity.ok(response);
        } else {
            throw new BadCredentialsException("OTP invalid or expired");
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

    private Mail successfullyRegistrationMailBody(String email) {
        //for send mail
        Mail mail = new Mail();
        mail.setMailTo(email);
        mail.setMailSubject("Welcome Split-Expense");
        //mail content
        Context context = new Context();
        context.setVariable("title", "Welcome to Split-Expense");
        context.setVariable("message", "You are successfully register with username " + email);

        String htmlContent = templateEngine.process("email/email-template", context);
        mail.setMailContent(htmlContent);
        return mail;
    }

    private void doAuthenticate(String email) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, "091234");
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }
}
