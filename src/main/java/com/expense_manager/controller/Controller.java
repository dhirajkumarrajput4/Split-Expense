package com.expense_manager.controller;

import com.expense_manager.entities.Person;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.ExpenseServices.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class Controller {

    @Autowired
    private PersonService personService;


    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Person>> fetchAllUsers(){
        List<Person> personList = personService.findAll();
        return ResponseEntity.ok(personList);
    }


    @GetMapping("/calculatedExpense")
    public ResponseEntity<?> fetchCalculatedExpense(Principal principal){
        Optional<Person> perOptional= personService.findByEmailId(principal.getName());


        return null;

    }

}
