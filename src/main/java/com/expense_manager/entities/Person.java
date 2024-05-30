package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@jakarta.persistence.Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Person extends Entity {
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

    @ManyToMany(mappedBy = "members")
    private List<Group> groups;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Group> createdGroups;

    @OneToMany(mappedBy = "createBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> createdExpenses;

    @OneToMany(mappedBy = "paidBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> paidExpenses;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Share> shares;

    public Person(String firstName, String lastName, String emailId, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.mobileNumber = phoneNumber;
    }

}
