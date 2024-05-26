package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class Otp extends Entity {

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @Column(nullable = false )
    private Integer otp;

    @Column(nullable = false)
    private LocalDateTime expiration;

}
