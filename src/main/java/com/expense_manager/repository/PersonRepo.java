package com.expense_manager.repository;

import com.expense_manager.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person,Long> {
    Optional<Person> findByMobileNumber(String phone);
    Optional<Person> findByEmailId(String phone);

}
