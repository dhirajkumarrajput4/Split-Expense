package com.expense_manager.repository;

import com.expense_manager.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person,Long> {
    public Optional<Person> findBymobileNumber(String phone);
}
