package com.sip.store.controllers;

import com.sip.store.entities.User;
import com.sip.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class BasicAuthRestController {
    @Autowired
    private UserService userService;
    @GetMapping(path = "/basicauth")
    public User basicauth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user);
        return user;
    }
    @GetMapping("/getuserbyemail/{email}")
    public User getuserbyemail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }
}
