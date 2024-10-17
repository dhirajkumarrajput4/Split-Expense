package com.expense_manager.service.ExpenseServices;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense_manager.dtos.GroupDto;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.repository.ExpensesRepoes.GroupRepo;
import com.expense_manager.resonses.AddMemberResonse;
import com.expense_manager.service.PersonService;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Group> findGroupById(Long idLong) {

        return this.groupRepo.findById(idLong);

    }

    @Override
    public List<Group> findAllGroups() {
        return this.groupRepo.findAll();
    }

    @Override
    public Group saveGroup(GroupDto groupDto, Person person) {
        groupDto.setCreatedBy(person);

        Group group = new Group();
        group.setGroupName(groupDto.getGroupName());
        group.setCreatedBy(groupDto.getCreatedBy());
        group.setMembers(Arrays.asList(person));

        return this.groupRepo.save(group);
    }

    @Override
    public Group updateGroupDetails(GroupDto groupDto, Group group) {
        group.setGroupName(groupDto.getGroupName());
        return groupRepo.save(group);
    }

    @Override
    public Group addMember(Group group, AddMemberResonse addMemberResonse) {

        Person newMember = this.personService.findByEmailId(addMemberResonse.getEmailId())
                .orElseGet(() -> {
                    Person newPerson = new Person();
                    newPerson.setEmailId(addMemberResonse.getEmailId());
                    newPerson.setPassword(passwordEncoder.encode("091234"));
                    personService.savePerson(newPerson);
                    return newPerson;
                });

        group.setMembers(Arrays.asList(newMember));
        return group;
    }

}
