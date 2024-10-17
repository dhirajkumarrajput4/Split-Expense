package com.expense_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Optional;
import java.util.List;
import java.util.*;

import com.expense_manager.dtos.GroupDto;
import com.expense_manager.entities.Group;
import com.expense_manager.service.PersonService;
import com.expense_manager.service.ExpenseServices.GroupService;
import com.expense_manager.entities.Person;
import com.expense_manager.resonses.AddMemberResonse;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PersonService personService;

    @GetMapping("/fetchGroup/{groupId}")
    public ResponseEntity<?> findGroupById(@PathVariable("groupId") Long groupId, Principal principal) {
        Optional<Person> personOptional = this.personService.findByEmailId(principal.getName());

        Optional<Group> groupOptional = personOptional.get().getGroups()
                .stream().filter(gr -> gr.getId().equals(groupId)).findFirst();

        if (groupOptional.isEmpty()) {
            return new ResponseEntity<>("No group gound with this id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/fetchGroups")
    public ResponseEntity<?> findAllGroups(Principal principal) {
        Optional<Person> personOptional = this.personService.findByEmailId(principal.getName());

        List<Group> groups = personOptional.get().getGroups();

        if (groups.isEmpty()) {
            return new ResponseEntity<>("No group found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupDto groupDto, Principal principal) {
        try {
            Optional<Person> perOptional = this.personService.findByEmailId(principal.getName());
            Group group = this.groupService.saveGroup(groupDto, perOptional.get());
            return new ResponseEntity<>(group, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("somthing went wrong when creating grop...", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{groupId}")
    public ResponseEntity<?> updateGroup(@RequestBody GroupDto groupDto, @PathVariable("groupId") Long groupId,
            Principal principal) {
        try {
            Optional<Person> perOptional = this.personService.findByEmailId(principal.getName());

            Group group = perOptional.get().getGroups()
                    .stream().filter(gr -> gr.getId().equals(groupId)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Group not found"));

            Group updatedGroup = this.groupService.updateGroupDetails(groupDto, group);

            return new ResponseEntity<>(updatedGroup, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("somthing went wrong when updating group details...", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMemberInGroup(@RequestBody AddMemberResonse addMemberResonse, Principal principal) {
        Optional<Person> personOptional = this.personService.findByEmailId(principal.getName());
        Optional<Group> groupOptional = personOptional.get().getGroups()
                .stream().filter(gr -> gr.getId().equals(addMemberResonse.getGroupId())).findFirst();

        if (groupOptional.isEmpty()) {
            return new ResponseEntity<>("No group found with given id: " + addMemberResonse.getGroupId(),
                    HttpStatus.NOT_FOUND);
        }

        try {
            this.groupService.addMember(groupOptional.get(), addMemberResonse);
            return new ResponseEntity<>("Meber added successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("something went wrong...." + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
