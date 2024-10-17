package com.expense_manager.entities;

import com.expense_manager.comman.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@jakarta.persistence.Entity
@Table(name = "expense_group") // Escaping the table name
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group extends Entity {

    @Column(name = "groupName")
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person createdBy;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expense> expenses;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> members;
}
