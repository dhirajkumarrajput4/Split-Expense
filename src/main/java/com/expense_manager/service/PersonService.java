package com.expense_manager.service;

import com.expense_manager.entities.Person;

import java.util.Optional;

public interface PersonService {
    public Optional<Person> findByPhone(String phone);
}
