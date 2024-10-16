package com.expense_manager.controller;

import com.expense_manager.entities.Person;
import com.expense_manager.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class Controller {

    @Autowired
    private PersonService personService;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<Person>> fetchAllUsers(){
        List<Person> personList = personService.findAll();
        return ResponseEntity.ok(personList);
    }

}
