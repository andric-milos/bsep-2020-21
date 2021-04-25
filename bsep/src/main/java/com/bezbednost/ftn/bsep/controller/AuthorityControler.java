package com.bezbednost.ftn.bsep.controller;


import com.bezbednost.ftn.bsep.dto.UserDTO;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.model.UserTokenState;
import com.bezbednost.ftn.bsep.security.auth.JwtAuthenticationRequest;
import com.bezbednost.ftn.bsep.service.impl.AuthorityService;
import com.bezbednost.ftn.bsep.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorityControler {

    @Autowired
    private AuthorityService authorityService;

    @PostMapping(value = "/login")
    public ResponseEntity<UserTokenState> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            UserTokenState userTokenState = authorityService.login(authenticationRequest);
            if (userTokenState == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userTokenState, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/secured")
    public String secured(){
        System.out.println("Inside secured()");
        return "Hello user !!! : " + new Date();
    }
}
