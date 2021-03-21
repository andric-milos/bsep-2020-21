package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.service.IKeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.cert.X509Certificate;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/keyStoreData")
public class KeyStoreDataController {

    @Autowired
    private IKeyStoreService iKeyStoreService;
    private IKeyStoreService keyStoreDataService;

    @GetMapping(value = "/doesKeyStoreExist/{certificateRole}")
    public ResponseEntity<?> doesKeyStoreExist(@PathVariable("certificateRole") String certificateRole) {
        return new ResponseEntity<>(this.iKeyStoreService.doesKeyStoreExist(certificateRole), HttpStatus.OK);
    }

    @GetMapping(value = "/loadCertificate/{role}/{alias}/{password}")
    public ResponseEntity<?> loadCertificate(@PathVariable("role") String role, @PathVariable("alias") String alias, @PathVariable("password") String password) {
        try {

            X509Certificate certificate = this.keyStoreDataService.loadCertificate(role, alias, password);
            if (certificate == null) {
                return new ResponseEntity<>("Certificate with this alias doesn't exist.", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(certificate.getSubjectDN(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Password is incorrect!", HttpStatus.BAD_REQUEST);
    }

}
