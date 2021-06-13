package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.dto.StringDTO;
import com.bezbednost.ftn.bsep.dto.UserDTO;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class.getName());

    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('USER')")
    public UserDTO getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user1 = (User) auth.getPrincipal();
        UserDetails userDetails = this.userService.loadUserByUsername(user1.getEmail());
        UserDTO retVal = new UserDTO(userDetails);
        logger.info("Get User {} .", userDetails.getUsername());
        return retVal;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<User> users = userService.getAll();
        logger.info("Get all Users.");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/getEmailFromJwtToken")
    public ResponseEntity<?> getEmailFromJwtToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return new ResponseEntity<StringDTO>(new StringDTO(user.getEmail()), HttpStatus.OK);
    }
}
