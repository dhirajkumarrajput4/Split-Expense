package com.expense_manager.service;

import com.expense_manager.entities.Otp;
import com.expense_manager.entities.Person;
import com.expense_manager.repository.OtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService{

    @Autowired
    private OtpRepo otpRepo;


    @Override
    public Optional<Otp> findByPersonAndOtp(Person person, Integer otp) {
        return otpRepo.findByOtpAndPerson(otp,person);
    }

    @Override
    public void save(Otp otp) {
        otpRepo.save(otp);
    }

    @Override
    public List<Otp> findAllOtp(Person person) {
        return otpRepo.findAllByOrderByIdDesc();
    }
}
