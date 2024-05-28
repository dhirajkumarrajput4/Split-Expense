package com.expense_manager.repository;

import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Long> {
    Optional<Otp> findByOtpAndPerson(Integer otp, Person person);
}
