package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.dto.IssuerAndSubjectDataDTO;
import com.bezbednost.ftn.bsep.generators.CertificateGenerator;
import com.bezbednost.ftn.bsep.generators.IssuerDataGenerator;
import com.bezbednost.ftn.bsep.generators.KeyGenerator;
import com.bezbednost.ftn.bsep.generators.SubjectDataGenerator;
import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;
import com.bezbednost.ftn.bsep.model.IssuerData;
import com.bezbednost.ftn.bsep.model.SubjectData;
import com.bezbednost.ftn.bsep.service.ICertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateService implements ICertificateService {

    @Autowired
    private KeyStoreService keyStoreService;

    @Override
    public void issueCertificate(IssuerAndSubjectDataDTO issuerAndSubjectData, String keyStorePassword) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        if (this.keyStoreService.doesKeyStoreExist(issuerAndSubjectData.getCertificateRole().toString())) {
            try {
                // System.out.println("Success!");
                KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
                try {
                    keyStore.load(new FileInputStream("file/pki/keystores/" + issuerAndSubjectData.getCertificateRole().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
                } catch (FileNotFoundException e) {
                    keyStore.load(null, keyStorePassword.toCharArray());
                } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
                    e.printStackTrace();
                }

                /*
                za sada je ovo sertifikata issuera (CA-a), jer nemamo korisnike sačuvane u bazi;
                ovo znači da će svaki put da se izgenerišu drugačiji ključevi i samim tim, ako se
                na frontendu unese da neki CA više puta izdaje sertifikat, to će svaki put biti sa
                drugačijim ključevima potpisano, tako da nema smisla, ali ispravićemo kada
                ubacimo i korisnike
                 */
                KeyPair keyPairIssuer = KeyGenerator.generateKeyPair();
                assert keyPairIssuer != null : "keyPairIssuer is null!";
                PrivateKey issuerPrivateKey = keyPairIssuer.getPrivate();
                // PublicKey issuerPublicKey = keyPairIssuer.getPublic();

                KeyPair keyPairSubject = KeyGenerator.generateKeyPair();

                // snimiti issuerAndSubjectData u bazu i vratiti id pod kojim je snimljeno i to će biti serial number

                // izgenerisati subjectdata
                SubjectData subjectData = SubjectDataGenerator.generateSubjectData(issuerAndSubjectData.getFirstNameSubject(),
                                                                                   issuerAndSubjectData.getLastNameSubject(),
                                                                                   issuerAndSubjectData.getOrganizationSubject(),
                                                                                   issuerAndSubjectData.getCountrySubject(),
                                                                                   issuerAndSubjectData.getCitySubject(),
                                                                                   issuerAndSubjectData.getEmailSubject(),
                                                                                   issuerAndSubjectData.getStartDate(),
                                                                                   issuerAndSubjectData.getExpiringDate(),
                                                                                   "1111");
                assert keyPairSubject != null : "keyPairSubject is null!";
                subjectData.setPublicKey(keyPairSubject.getPublic());

                // izgenerisati issuerdata
                // issuerPrivateKey za sada neka bude hardcore zakucan ključ
                IssuerData issuerData = IssuerDataGenerator.generateIssuerData(issuerAndSubjectData.getFirstNameIssuer(),
                                                                               issuerAndSubjectData.getLastNameIssuer(),
                                                                               issuerAndSubjectData.getOrganizationIssuer(),
                                                                               issuerAndSubjectData.getCountryIssuer(),
                                                                               issuerAndSubjectData.getCityIssuer(),
                                                                               issuerAndSubjectData.getEmailIssuer(),
                                                                               issuerPrivateKey);

                X509Certificate certificate = CertificateGenerator.generateCertificate(subjectData, issuerData);

                // skladištiti certificate u keystore
                String alias = issuerAndSubjectData.getFirstNameSubject() + " " + issuerAndSubjectData.getLastNameSubject();
                keyStore.setKeyEntry(alias, keyPairSubject.getPrivate(), keyStorePassword.toCharArray(), new Certificate[] { certificate });

                keyStore.store(new FileOutputStream("file/pki/keystores/" + issuerAndSubjectData.getCertificateRole().toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());

                System.out.println("\n===== Podaci o izdavacu sertifikata =====");
                System.out.println(certificate.getIssuerX500Principal().getName());
                System.out.println("\n===== Podaci o vlasniku sertifikata =====");
                System.out.println(certificate.getSubjectX500Principal().getName());
                System.out.println("\n===== Sertifikat =====");
                System.out.println("-------------------------------------------------------");
                System.out.println(certificate);
                System.out.println("-------------------------------------------------------");
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            }
        }
    }

}
