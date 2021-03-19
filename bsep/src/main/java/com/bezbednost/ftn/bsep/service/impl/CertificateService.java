package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.model.IssuerAndSubject;
import com.bezbednost.ftn.bsep.service.ICertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

@Service
public class CertificateService implements ICertificateService {

    @Autowired
    private KeyStoreService keyStoreService;

    @Override
    public void issueCertificate(IssuerAndSubject issuerAndSubjectData, String keyStorePassword) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        if (this.keyStoreService.doesKeyStoreExist(issuerAndSubjectData.getCertificateRole().toString())) {
            try {
                System.out.println("Success!");
                KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
                keyStore.load(new FileInputStream("file/pki/keystores/" + issuerAndSubjectData.getCertificateRole().toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            }
        }
    }

}
