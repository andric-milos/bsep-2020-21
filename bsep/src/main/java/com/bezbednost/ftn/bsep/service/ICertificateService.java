package com.bezbednost.ftn.bsep.service;

import com.bezbednost.ftn.bsep.dto.NewCertificateDTO;
import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Collection;

public interface ICertificateService {
    void issueCertificate(NewCertificateDTO newCertificateDTO) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException, UnrecoverableEntryException;
    Collection<IssuerAndSubjectData> getCertificates();
    void withdrawCertificate(String email);
    Collection<IssuerAndSubjectData> GetChildCertificate (String email);
}
