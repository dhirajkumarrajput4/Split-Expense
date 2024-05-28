package com.expense_manager.service;

import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;

import java.util.Optional;


public interface  OtpService {
    public Optional<Otp> findByPersonAndOtp(Person person, Integer otp);
}
