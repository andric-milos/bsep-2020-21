package com.bezbednost.ftn.bsep.service;

import com.bezbednost.ftn.bsep.model.IssuerAndSubject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

public interface ICertificateService {
    void issueCertificate(IssuerAndSubject issuerAndSubjectData, String keyStorePassword) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException;

}
