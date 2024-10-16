package com.expense_manager.service;

import com.expense_manager.entities.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<Person> findByPhone(String phone);
    Optional<Person> findByEmailId(String email);
    void savePerson(Person person);
    void deletePerson(Person person);
    Optional<Person> findById(Long id);
    List<Person> findAll();
}
