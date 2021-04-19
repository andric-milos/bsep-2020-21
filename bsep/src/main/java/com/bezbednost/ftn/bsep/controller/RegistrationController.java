package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.model.RegistrationRequest;
import com.bezbednost.ftn.bsep.service.impl.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "api/register")
public class RegistrationController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
}
