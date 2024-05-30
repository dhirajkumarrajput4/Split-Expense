package com.expense_manager.security;

import com.expense_manager.entities.Person;
import com.expense_manager.repository.PersonRepo;
import com.expense_manager.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = null;
        if(username.contains("@")){
            person = this.personService.findByEmailId(username).orElse(null);
        }else {
            person = this.personService.findByPhone(username).orElse(null);
        }
        if (person == null){
            throw new UsernameNotFoundException("Could not found user !!");
        }
      return new User(person.getEmailId(), "", new ArrayList<>());
//        return new CustomUserDetails(person);
    }
}
