package com.expense_manager.dtos;

import java.util.List;

import com.expense_manager.entities.Person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private String groupName;

    private Person createdBy;

    private List<Person> members;
}
