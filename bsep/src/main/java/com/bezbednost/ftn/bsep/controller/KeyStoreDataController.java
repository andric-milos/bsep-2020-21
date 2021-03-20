package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.service.IKeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/keyStoreData")
public class KeyStoreDataController {

    @Autowired
    private IKeyStoreService iKeyStoreService;

    @GetMapping(value = "/doesKeyStoreExist/{certificateRole}")
    public ResponseEntity<?> doesKeyStoreExist(@PathVariable("certificateRole") String certificateRole) {
        return new ResponseEntity<>(this.iKeyStoreService.doesKeyStoreExist(certificateRole), HttpStatus.OK);
    }

}
