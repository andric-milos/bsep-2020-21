package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.dto.UserDTO;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('USER')")
    public UserDTO getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) auth.getPrincipal();
        UserDetails userDetails = this.userService.loadUserByUsername(user1.getEmail());
        UserDTO retVal = new UserDTO(userDetails);
        return retVal;
    }

}
