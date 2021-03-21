package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.certificate.CertificateGenerator;
import com.bezbednost.ftn.bsep.certificate.Generators;
import com.bezbednost.ftn.bsep.model.CertificateRole;
import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;
import com.bezbednost.ftn.bsep.model.IssuerData;
import com.bezbednost.ftn.bsep.model.SubjectData;
import com.bezbednost.ftn.bsep.repository.IssuerAndSubjectDataRepository;
import com.bezbednost.ftn.bsep.service.ICertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
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

    private Generators generators = new Generators();

    private CertificateGenerator certificateGenerator = new CertificateGenerator();

    @Autowired
    private IssuerAndSubjectDataRepository issuerAndSubjectDataRepository;

    @Override
    public void issueCertificate(IssuerAndSubjectData issuerAndSubjectData, String keyStorePassword) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException {
        if (this.keyStoreService.doesKeyStoreExist(issuerAndSubjectData.getCertificateRole().toString())) {
            try {
                System.out.println("Success!");
                KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");
                keyStore.load(new FileInputStream("src/main/resources/keystores/" + issuerAndSubjectData.getCertificateRole().toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            }
        }

        if (issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailSubject()) != null) {
            System.out.println("Subject already exists!");
            throw new NonUniqueResultException();
        }
        boolean isSelfSigned = issuerAndSubjectData.getCertificateRole().equals(CertificateRole.SELF_SIGNED);

        if (isSelfSigned && issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailIssuer()) != null) {
            System.out.println("Issuer already has a self signed certificate!");
            throw new NonUniqueResultException();
        }

        Long issuerId;
        Long subjectId;

        // saving to db
        if (!isSelfSigned) {
            IssuerAndSubjectData subjectDataToDB = new IssuerAndSubjectData(issuerAndSubjectData.getFirstNameSubject(), issuerAndSubjectData.getLastNameSubject(),
                    issuerAndSubjectData.getOrganizationSubject() , issuerAndSubjectData.getCountrySubject(),
                    issuerAndSubjectData.getCitySubject(), issuerAndSubjectData.getEmailSubject() , issuerAndSubjectData.getTypeOfEntity(),
                    issuerAndSubjectData.getCertificateRole(), issuerAndSubjectData.getKeyUsage(), issuerAndSubjectData.getExtendedKeyUsage());
            Long parentId = issuerAndSubjectDataRepository.findByEmail
                    (issuerAndSubjectData.getEmailIssuer()).getId();
            subjectDataToDB.setParentId(parentId);

            //System.out.println("extended " + issuerAndSubjectData.getExtendedKeyUsage()[0]);
            this.issuerAndSubjectDataRepository.save(subjectDataToDB);
            this.issuerAndSubjectDataRepository.flush();

        } else {
            IssuerAndSubjectData issuerDataToDB = new IssuerAndSubjectData(issuerAndSubjectData.getFirstNameIssuer(), issuerAndSubjectData.getLastNameIssuer(),
                    issuerAndSubjectData.getOrganizationIssuer(), issuerAndSubjectData.getCountryIssuer(),
                    issuerAndSubjectData.getCityIssuer(), issuerAndSubjectData.getEmailIssuer(), issuerAndSubjectData.getTypeOfEntity(),
                    issuerAndSubjectData.getCertificateRole(), issuerAndSubjectData.getKeyUsage(), issuerAndSubjectData.getExtendedKeyUsage());
            //System.out.println("extended " + issuerAndSubjectData.getExtendedKeyUsage()[0]);
            this.issuerAndSubjectDataRepository.save(issuerDataToDB);
            this.issuerAndSubjectDataRepository.flush();

        }

        issuerId = issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailIssuer()).getId();
        if (issuerAndSubjectData.getCertificateRole().equals(CertificateRole.SELF_SIGNED)) {
            subjectId = issuerId;
        } else {
            subjectId = issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailSubject()).getId();
        }

        KeyPair keyPairIssuer = generators.generateKeyPair();

        // name is serial num for now instead of id
        SubjectData subjectData = generators.generateSubjectData(subjectId, issuerAndSubjectData.getFirstNameSubject(),
                issuerAndSubjectData.getLastNameSubject(),
                issuerAndSubjectData.getOrganizationSubject(), issuerAndSubjectData.getCountrySubject(),
                issuerAndSubjectData.getCitySubject(), issuerAndSubjectData.getEmailSubject(),
                issuerAndSubjectData.getCertificateRole());

        IssuerAndSubjectData temp = this.issuerAndSubjectDataRepository.findTopByOrderByIdDesc();
        temp.setStartDate(subjectData.getStartDate());
        temp.setExpiringDate(subjectData.getEndDate());
        if (temp.getCertificateRole().equals(CertificateRole.SELF_SIGNED)) {
            temp.setParentId(temp.getId());
        }
        this.issuerAndSubjectDataRepository.save(temp);

        IssuerData issuerData = generators.generateIssuerData(issuerId, keyPairIssuer.getPrivate(),
                issuerAndSubjectData.getFirstNameIssuer(), issuerAndSubjectData.getLastNameIssuer(),
                issuerAndSubjectData.getOrganizationIssuer(), issuerAndSubjectData.getCountryIssuer(),
                issuerAndSubjectData.getCityIssuer(), issuerAndSubjectData.getEmailIssuer());


        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        saveCertificate(issuerAndSubjectData.getCertificateRole(),
                "sifra", certificate.getSerialNumber().toString(),
                            keyStorePassword, keyPairIssuer.getPrivate(), certificate);

        System.out.println("\n===== Podaci o izdavacu sertifikata =====");
        System.out.println(certificate.getIssuerX500Principal().getName());
        System.out.println("\n===== Podaci o vlasniku sertifikata =====");
        System.out.println(certificate.getSubjectX500Principal().getName());
        System.out.println("\n===== Sertifikat =====");
        System.out.println("-------------------------------------------------------");
        System.out.println(certificate);
        System.out.println("-------------------------------------------------------");

    }

    public void saveCertificate(CertificateRole role, String keyPassword, String alias, String keyStorePassword,
                                PrivateKey privateKey, X509Certificate certificate) throws NoSuchProviderException,
                                KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        String name = role.toString().toLowerCase();
        String file = ("src/main/resources/keystores/" + name + ".jks");
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

        try {
            keyStore.load(new FileInputStream(file), keyStorePassword.toCharArray());
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            createKeyStore(name, keyStorePassword, keyStore);
        } catch (IOException e) {
            System.out.println("Wrong password!");
        } catch (NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
        System.out.println("KeyStore size before: " + keyStore.size());
        keyStore.setKeyEntry(alias, privateKey, keyPassword.toCharArray(), new Certificate[]{certificate}); //save cert
        System.out.println("KeyStore size after: " + keyStore.size());
        keyStore.store(new FileOutputStream(file), keyStorePassword.toCharArray());
    }

    private void createKeyStore(String type, String keyStorePassword, KeyStore keyStore) {
        String file = ("src/main/resources/keystores/" + type + ".jks");
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));

        try {
            keyStore.load(null, keyStorePassword.toCharArray());
            keyStore.store(new FileOutputStream(file), keyStorePassword.toCharArray());
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }



}
