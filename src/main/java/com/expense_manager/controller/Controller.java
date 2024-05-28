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
@RequestMapping("/user")
public class Controller {

    @Autowired
    private PersonService personService;


}
