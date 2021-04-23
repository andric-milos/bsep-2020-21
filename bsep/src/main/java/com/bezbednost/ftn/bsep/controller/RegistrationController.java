package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.model.RegistrationRequest;
import com.bezbednost.ftn.bsep.service.impl.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/registration")
public class RegistrationController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        // returnValue can be one of next values: "REQUEST_NOT_VALID", "EMAIL_IS_TAKEN" or registration token
        String returnValue = registrationService.register(request);

        if (returnValue.equals("REQUEST_NOT_VALID"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (returnValue.equals("EMAIL_IS_TAKEN"))
            return new ResponseEntity<>("EMAIL_IS_TAKEN", HttpStatus.OK);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping(value = "/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        // returnValue can be one of next values: email, "EMAIL_ALREADY_CONFIRMED", "TOKEN_HAS_EXPIRED"
        String returnValue = registrationService.confirmToken(token);

        if (returnValue.equals("EMAIL_ALREADY_CONFIRMED"))
            return new ResponseEntity<>("<h1>Email has already been confirmed!</h1>", HttpStatus.OK);

        if (returnValue.equals("TOKEN_HAS_EXPIRED"))
            return new ResponseEntity<>("<h1>>Token has expired!</h1>", HttpStatus.OK);

        return new ResponseEntity<>("<h1>Email " + returnValue + " has been confirmed!</h1>", HttpStatus.OK);
    }
}
