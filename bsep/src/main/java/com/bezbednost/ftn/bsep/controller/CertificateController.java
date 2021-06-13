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
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    Logger logger = LoggerFactory.getLogger(CertificateController.class);

    @PostMapping(value = "/issue")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> issueCertificate(@RequestBody NewCertificateDTO newCertificateDTO) {
        logger.info("A user with id : {} has tried to make certificate.", newCertificateDTO.getId());
        try {
            logger.info("Successfully issued certificate!" + "Issuer id : {}." +
                    "Subject id : {}.", newCertificateDTO.getId(), newCertificateDTO.getSubjectID());

            this.certificateService.issueCertificate(newCertificateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error while creating certificate. ");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (NoSuchAlgorithmException | CertificateException | NoSuchProviderException | UnrecoverableEntryException | ParseException e) {
            logger.error("Error while creating certificate. ");
            e.printStackTrace();
        } catch (KeyStoreException e) {
            logger.error("Password is incorrect!" +
                    "Issuer id : {}." + "Subject id : {}.", newCertificateDTO.getId(), newCertificateDTO.getSubjectID());
            return new ResponseEntity<>("Password is incorrect! Please try again.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCertificates() {
        logger.info("An admin has requested all certificate.");
        try {
            logger.info("Successfully returned list of all certificate requests for ADMIN.");
            return new ResponseEntity<>(this.certificateService.getCertificates(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while returning list of all certificate requests for ADMIN.");
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/withdraw/{certificateEmail}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> withdrawCertificate(@PathVariable("certificateEmail") String certificateEmail){
        logger.info("A user has requested to withdraw certificate." + "Certificate email : {}.", certificateEmail);
        try {
            logger.info("A user withdraw certificate." + "Certificate email : {}.", certificateEmail);
            this.certificateService.withdrawCertificate(certificateEmail);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while withdraw a certificate." + "Certificate email : {}.", certificateEmail);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/children")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getChildCertificates() {
        logger.info("A user has requested all child certificate.");
        try {
            logger.info("Successfully returned list of all child certificate requests.");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();
            this.certificateService.GetChildCertificate(user.getUsername());
            return new ResponseEntity<>(this.certificateService.getCertificates(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while returning list of all child certificate requests.");
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
        logger.info("Get all autority certificates bt email : {}.", email);
        return new ResponseEntity<List<CertificateDTO>>(certificateDTOList, HttpStatus.OK);
    }
}
