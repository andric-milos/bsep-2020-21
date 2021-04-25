package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.service.impl.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    @PostMapping(value = "/{keyStorePassword}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> issueCertificate(@RequestBody IssuerAndSubjectData issuerAndSubjectData, @PathVariable("keyStorePassword") String keyStorePassword) {
        try {
            this.certificateService.issueCertificate(issuerAndSubjectData, keyStorePassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchAlgorithmException | CertificateException | NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            return new ResponseEntity<>("Password is incorrect! Please try again.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCertificates() {
        try {
            return new ResponseEntity<>(this.certificateService.getCertificates(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/withdraw/{certificateEmail}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> withdrawCertificate(@PathVariable("certificateEmail") String certificateEmail){
        try {
            this.certificateService.withdrawCertificate(certificateEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/children")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getChildCertificates() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            this.certificateService.GetChildCertificate(user.getUsername());
            return new ResponseEntity<>(this.certificateService.getCertificates(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}