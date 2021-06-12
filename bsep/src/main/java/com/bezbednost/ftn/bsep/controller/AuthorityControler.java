package com.bezbednost.ftn.bsep.controller;


import com.bezbednost.ftn.bsep.dto.UserDTO;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.model.UserTokenState;
import com.bezbednost.ftn.bsep.security.auth.JwtAuthenticationRequest;
import com.bezbednost.ftn.bsep.service.impl.AuthorityService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorityControler {

    @Autowired
    private AuthorityService authorityService;

    Logger logger = LoggerFactory.getLogger(AuthorityControler.class.getName());

    @PostMapping(value = "/login")
    public ResponseEntity<UserTokenState> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        logger.info("Date : {}, A user tried to login with email : {}.", LocalDateTime.now(), authenticationRequest.getEmail());
        try {
            UserTokenState userTokenState = authorityService.login(authenticationRequest);
            if (userTokenState == null) {
                logger.error("Date : {}, A user with email : {} does not exist.", LocalDateTime.now(), authenticationRequest.getEmail());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logger.info("Date : {}, A user with email : {} has successfully logged in.", LocalDateTime.now(), authenticationRequest.getEmail());
            return new ResponseEntity<>(userTokenState, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error("Date : {}, Unsuccessful log in. A user with email : {} does not exist.", LocalDate.now(), authenticationRequest.getEmail());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/secured")
    public String secured(){
        System.out.println("Inside secured()");
        return "Hello user !!! : " + new Date();
    }
}
