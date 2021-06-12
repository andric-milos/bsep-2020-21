package com.bezbednost.ftn.bsep.controller;

import com.bezbednost.ftn.bsep.dto.CertificateDTO;
import com.bezbednost.ftn.bsep.dto.NewCertificateDTO;
import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.service.impl.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.text.ParseException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @PostMapping(value = "/{keyStorePassword}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> issueCertificate(@RequestBody IssuerAndSubjectData issuerAndSubjectData, @PathVariable("keyStorePassword") String keyStorePassword) {
        logger.info("Date : {}, A user with email : {} has tried to make certificate.", LocalDateTime.now(), issuerAndSubjectData.getEmailIssuer());
        try {
            logger.info("Date : {}, Successfully issued certificate!" + "Issuer email : {}." +
                    "Subject email : {}.", LocalDateTime.now(), issuerAndSubjectData.getEmailIssuer(), issuerAndSubjectData.getEmailSubject());
            this.certificateService.issueCertificate(issuerAndSubjectData, keyStorePassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Date : {}, Error while creating certificate. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchAlgorithmException | CertificateException | NoSuchProviderException e) {
            logger.error("Date : {}, Error while creating certificate. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());
            e.printStackTrace();
        } catch (KeyStoreException e) {
            logger.error("Date : {}, Password is incorrect!" +
                    "Issuer email : {}." + "Subject email : {}." + "Password : {}.", LocalDateTime.now(), issuerAndSubjectData.getEmailIssuer(), issuerAndSubjectData.getEmailSubject(), keyStorePassword);
            return new ResponseEntity<>("Password is incorrect! Please try again.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCertificates() {
        /// treba dodati email admina mozda
        logger.info("Date : {}, An admin has requested all certificate.", LocalDateTime.now());
        try {
            logger.info("Date : {}, Successfully returned list of all certificate requests.", LocalDateTime.now());
            return new ResponseEntity<>(this.certificateService.getCertificates(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Date : {}, Error while returning list of all certificate requests. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/withdraw/{certificateEmail}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> withdrawCertificate(@PathVariable("certificateEmail") String certificateEmail){
        logger.info("Date : {}, A user has requested to withdraw certificate." + "Certificate email : {}.", LocalDateTime.now(), certificateEmail);
        try {
            logger.info("Date : {}, A user withdraw certificate." + "Certificate email : {}.", LocalDateTime.now(), certificateEmail);
            this.certificateService.withdrawCertificate(certificateEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Date : {}, Error while withdraw a certificate. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/children")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getChildCertificates() {
        logger.info("Date : {}, A user has requested all child certificate.", LocalDateTime.now());
        try {
            logger.info("Date : {}, Successfully returned list of all child certificate requests.", LocalDateTime.now());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            return new ResponseEntity<>(this.certificateService.GetChildCertificate(user.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Date : {}, Error while returning list of all child certificate requests. " +
                    "Error : {}.", LocalDateTime.now(), e.toString());
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/issueDefaultCertificate")
    public ResponseEntity<?> issueDefaultCertificate() {
        try {
            this.certificateService.issueDefaultCertificate();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* Email is passed as a parameter, and return value is list of all
    *  "CA" or "INTERMEDIATE" certificates that user with given email has. */
    @GetMapping(value = "/CAorIntermediate/{email}")
    public ResponseEntity<?> getAllAuthorityCertificatesByEmail(@PathVariable("email") String email) {
        List<CertificateDTO> certificateDTOList = this.certificateService.getAllAuthorityCertificatesByEmail(email);

        return new ResponseEntity<List<CertificateDTO>>(certificateDTOList, HttpStatus.OK);
    }
}