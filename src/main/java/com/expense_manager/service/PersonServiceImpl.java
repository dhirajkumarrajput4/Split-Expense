package com.expense_manager.service;

import com.expense_manager.entities.Person;
import com.expense_manager.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepo personRepo;

    @Override
    public Optional<Person> findByPhone(String phone) {
        return personRepo.findByMobileNumber(phone);
    }

    @Override
    public void savePerson(Person person) {
        personRepo.save(person);
    }

    @Override
    public void deletePerson(Person person) {
        personRepo.delete(person);
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personRepo.findById(id);
    }
}
