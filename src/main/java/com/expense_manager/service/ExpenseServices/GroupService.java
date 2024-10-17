package com.expense_manager.service.ExpenseServices;

import java.util.Optional;
import java.util.List;

import com.expense_manager.dtos.GroupDto;
import com.expense_manager.entities.Group;
import com.expense_manager.entities.Person;
import com.expense_manager.resonses.AddMemberResonse;
public interface GroupService {
    
    Optional<Group> findGroupById(Long idLong);

    List<Group> findAllGroups();

    Group saveGroup(GroupDto group,Person person);

    Group addMember(Group group, AddMemberResonse aMemberResonse);

    Group updateGroupDetails(GroupDto groupDto, Group group);

    
}
