package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import jakarta.persistence.*;

import java.util.List;

@jakarta.persistence.Entity
@Table(name = "group")
public class Group extends Entity {

    @Column(name = "groupName")
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person createdBy;

    @OneToMany(mappedBy = "group",cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Person> members;
}
