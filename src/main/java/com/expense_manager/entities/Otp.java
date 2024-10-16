package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@jakarta.persistence.Entity
@Table(name = "otp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp extends Entity {
    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @Column(nullable = false )
    private Integer otp;

    @Column(nullable = false)
    private LocalDateTime expiration;

    public Otp(Person person, Integer otp){
        this.person = person;
        this.otp = otp;
    }

    public void expireAfter(int duration, ChronoUnit chronoUnit) {
        expiration = LocalDateTime.now().plus(Duration.of(duration, chronoUnit));
    }

}
