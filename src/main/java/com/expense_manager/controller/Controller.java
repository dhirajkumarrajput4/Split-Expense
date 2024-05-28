package com.expense_manager.controller;

import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;
import com.expense_manager.repository.OtpRepo;
import com.expense_manager.security.JwtAuthenticationFilter;
import com.expense_manager.service.OtpService;
import com.expense_manager.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class Controller {
    @Autowired
    private OtpService otpService;

    @Autowired
    private PersonService personService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String phone, @RequestParam Integer otp) {
        Person person = personService.findByPhone(phone).orElse(null);
        Optional<Otp> otpEntity =otpService.findByPersonAndOtp(person,otp);

        if (!otpEntity.isEmpty() && otpEntity.get().getExpiration().isAfter(LocalDateTime.now())) {
            // If OTP is valid and not expired, generate JWT token
            String token = JwtAuthenticationFilter.generateToken(person);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }

}
