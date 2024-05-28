package com.expense_manager.service;

import com.expense_manager.entities.Person;
import com.expense_manager.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepo personRepo;

    @Override
    public Optional<Person> findByPhone(String phone) {
        return personRepo.findBymobileNumber(phone);
    }
}
