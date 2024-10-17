package com.expense_manager.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_manager.dtos.ExpenseDto;
import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.ExpenseServices.ExpenseService;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private PersonService personService;

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseDto expenseDto, Principal principal) {
        
        Optional<Person> person = personService.findByEmailId(principal.getName());
        expenseDto.setCreateBy(person.get());
        expenseDto.setPaidBy(person.get());

        Optional<Group> grouOptional = person.get().getGroups().stream()
            .filter(gr->gr.getId().equals(expenseDto.getGroupId())).findFirst();

        if(grouOptional.isEmpty()){
        return new ResponseEntity<>("Group not found for creating response please check username",HttpStatus.NOT_FOUND);
        }            
        try {
            Expense expense = this.expenseService.createExpense(expenseDto, grouOptional.get());
            return new ResponseEntity<>(expense, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Something went wrong...", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> fetchExpenseById(@PathVariable("expenseId") Long idLong, Principal principal) {
        Optional<Person> person = personService.findByEmailId(principal.getName());
        Expense expense = this.expenseService.findExpensesByUser(person.get()).stream()
                .filter(expe -> expe.getId().equals(idLong)).findFirst()
                .orElseThrow(() -> new RuntimeException("Expense not found with the given id"));
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<?> fetchAllExpensesPaidByUser(Principal principal) {
        Optional<Person> person = personService.findByEmailId(principal.getName());
        List<Expense> expenses = this.expenseService.findExpensesByUser(person.get());
        if (expenses.isEmpty()) {
            return new ResponseEntity<>("No expense record found...", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }
}
