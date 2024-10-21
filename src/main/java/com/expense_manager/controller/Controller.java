package com.expense_manager.controller;

import com.expense_manager.entities.Expense;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.repository.ExpensesRepoes.GroupRepo;
import com.expense_manager.resonses.ExpenseResponse;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.ExpenseServices.ExpenseService;
import com.expense_manager.service.ExpenseServices.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private GroupService groupService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Person>> fetchAllUsers() {
        List<Person> personList = personService.findAll();
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/calculatedExpense/{groupId}")
    public ResponseEntity<?> fetchCalculatedExpense(Principal principal, @PathVariable("groupId") Long groupId)
            throws Exception {
        Person person = personService.findByEmailId(principal.getName())
                .orElseThrow(() -> new Exception("User not found..."));
        Group group = this.groupService.findGroupById(groupId)
                .orElseThrow(() -> new RuntimeException("No group found with given id: " + groupId));
        try {
            List<ExpenseResponse> expenseResponses = expenseService.fetchExpeseWithDetails(group, person);
            return new ResponseEntity<>(expenseResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong fetching details" + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
