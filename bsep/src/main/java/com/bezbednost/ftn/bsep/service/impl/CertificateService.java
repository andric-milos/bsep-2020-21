package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.certificate.CertificateGenerator;
import com.bezbednost.ftn.bsep.certificate.Generators;
import com.bezbednost.ftn.bsep.model.*;
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
import java.util.*;

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
        /* provera da li keystore postoji
         * ako ne postoji kreirati ga? */
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

        /* u IssuerAndSubjectDataRepository klasi ima metoda findByEmail kojoj treba da prosledimo
        *  email issuer-a i da nam povratna vrednosti bude objekat klase IssuerAndSubjectData čije je polje
        *  issuer_email jednako tom mailu koji smo prosledili
        *
        *  1. greska ovde je sto je prosledjen emailSubject kao parametar umesto emailIssuer
        *  2. greska: tako ogranicavamo da korisnik ne moze imati vise CA sertifikata, a to tako ne treba
        */
        if (issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailSubject()) != null) {
            System.out.println("Subject already exists!");
            throw new NonUniqueResultException();
        }

        /* ovu proveru ne treba ovako vršiti, jer ne treba na klijentu da imamo da izaberemo da li je
        *  self signed, već ćemo da prepoznamo da li je self signed tako što je issuer = subject */
        boolean isSelfSigned = issuerAndSubjectData.getCertificateRole().equals(CertificateRole.SELF_SIGNED);

        /* kakve veze ima ako issuer već ima self signed sertificate, on sebi može da izda koliko hoće sertifikata */
        if (isSelfSigned && issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailIssuer()) != null) {
            System.out.println("Issuer already has a self signed certificate!");
            throw new NonUniqueResultException();
        }

        Long issuerId;
        Long subjectId;

        /* snimanje sertifikata kao objekat klase IssuerAndSubjectData u BP
        *
        *  ako nije self signed, onda setujemo parentId, a ako jeste, onda ne setujemo
        *  nego ostavimo parentId null? dobro, ovo ima smisla, mada i nema, jer možemo mi snimiti
        *  u bazu i naknadno setovati parentId i snimiti opet
        *
        *  trebalo bi snimiti i javni ključ ovde */
        if (!isSelfSigned) {
            IssuerAndSubjectData subjectDataToDB =
                    new IssuerAndSubjectData(issuerAndSubjectData.getFirstNameSubject(),
                                             issuerAndSubjectData.getLastNameSubject(),
                                             issuerAndSubjectData.getOrganizationSubject(),
                                             issuerAndSubjectData.getCountrySubject(),
                                             issuerAndSubjectData.getCitySubject(),
                                             issuerAndSubjectData.getEmailSubject(),
                                             issuerAndSubjectData.getTypeOfEntity(),
                                             issuerAndSubjectData.getCertificateRole(),
                                             issuerAndSubjectData.getKeyUsage(),
                                             issuerAndSubjectData.getExtendedKeyUsage());
            Long parentId = issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailIssuer()).getId();
            subjectDataToDB.setParentId(parentId);

            //System.out.println("extended " + issuerAndSubjectData.getExtendedKeyUsage()[0]);
            this.issuerAndSubjectDataRepository.save(subjectDataToDB);
            this.issuerAndSubjectDataRepository.flush();
        } else {
            IssuerAndSubjectData issuerDataToDB =
                    new IssuerAndSubjectData(issuerAndSubjectData.getFirstNameIssuer(),
                                             issuerAndSubjectData.getLastNameIssuer(),
                                             issuerAndSubjectData.getOrganizationIssuer(),
                                             issuerAndSubjectData.getCountryIssuer(),
                                             issuerAndSubjectData.getCityIssuer(),
                                             issuerAndSubjectData.getEmailIssuer(),
                                             issuerAndSubjectData.getTypeOfEntity(),
                                             issuerAndSubjectData.getCertificateRole(),
                                             issuerAndSubjectData.getKeyUsage(),
                                             issuerAndSubjectData.getExtendedKeyUsage());
            //System.out.println("extended " + issuerAndSubjectData.getExtendedKeyUsage()[0]);
            this.issuerAndSubjectDataRepository.save(issuerDataToDB);
            this.issuerAndSubjectDataRepository.flush();
        }

        /* ekstrakcija id-a upravo snimljenog sertifikata */
        issuerId = issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailIssuer()).getId();

        /* ovu proveru ne treba ovako vršiti, jer ne treba na klijentu da imamo da izaberemo da li je
         *  self signed, već ćemo da prepoznamo da li je self signed tako što je issuer = subject
         *
         * ovo je u suštini samo da ekstraktuje id subject-a upravo snimljenog sertifikata???? */
        if (issuerAndSubjectData.getCertificateRole().equals(CertificateRole.SELF_SIGNED)) {
            subjectId = issuerId;
        } else {
            subjectId = issuerAndSubjectDataRepository.findByEmail(issuerAndSubjectData.getEmailSubject()).getId();
        }

        /* ne generišemo key pair za issuer-a, on treba da postoji već i mi samo da ga ekstraktujemo
        *  iz keystore-a */
        KeyPair keyPairIssuer = generators.generateKeyPair();

        /* subjectId is passed as serial number
        *  pogledati malo tu metodu generateSubjectData i momenat u kojem se koristi X500NameBuilder */
        SubjectData subjectData = generators.generateSubjectData(subjectId,
                                                                 issuerAndSubjectData.getFirstNameSubject(),
                                                                 issuerAndSubjectData.getLastNameSubject(),
                                                                 issuerAndSubjectData.getOrganizationSubject(),
                                                                 issuerAndSubjectData.getCountrySubject(),
                                                                 issuerAndSubjectData.getCitySubject(),
                                                                 issuerAndSubjectData.getEmailSubject(),
                                                                 issuerAndSubjectData.getCertificateRole());

        /* wtf do next 8 lines do? */
        IssuerAndSubjectData temp = this.issuerAndSubjectDataRepository.findTopByOrderByIdDesc();
        temp.setStartDate(subjectData.getStartDate());
        temp.setExpiringDate(subjectData.getEndDate());

        if (temp.getCertificateRole().equals(CertificateRole.SELF_SIGNED)) {
            temp.setParentId(temp.getId());
        }
        this.issuerAndSubjectDataRepository.save(temp);

        /* issuerId passed as serial number
        *  is serial number necessary in this case?
        *  -> why do we have serial number for subject and issuer both? */
        IssuerData issuerData = generators.generateIssuerData(issuerId,
                                                              keyPairIssuer.getPrivate(),
                                                              issuerAndSubjectData.getFirstNameIssuer(),
                                                              issuerAndSubjectData.getLastNameIssuer(),
                                                              issuerAndSubjectData.getOrganizationIssuer(),
                                                              issuerAndSubjectData.getCountryIssuer(),
                                                              issuerAndSubjectData.getCityIssuer(),
                                                              issuerAndSubjectData.getEmailIssuer());

        /* issuerData treba da sadrži privatni ključ issuer-a, jer će u metodi
        *  certificateGenerator.generateCertificate koja se poziva ispod morati da se
        *  potpiše sertifikat koji kreiramo
        *
        *  nije mi jasno gde skladištimo privatni ključ upravo kreiranog sertifikata
        *  -> aha, u keystore-u skladištimo sertifikata i njegov privatni ključ u paru
        *  (pogledaj 5 linija ispod) */
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        /* analizirati ovu metodu dole i analizirati getCertificate(alias) metodu
        *  - metoda prima certificateRole kao 1. parametar, jer snima sertifikat
        *  u keystore koji nosi ime certificateRole-a
        *  - 3. parametar je alias, mi smo ovde prosledili serial number (ima smisla)
        *  - 4. parametar je šifra kojom otključavamo keystore (ona je prosleđena
        *  kao parametar metode issueCertificate)
        *  - 5. parametar je privatni ključ issuera kojim potpisujemo sertifikat
        *  - 6. parametar je sertifikat koji snimamo u sam keystore */
        saveCertificate(issuerAndSubjectData.getCertificateRole(),
                        "sifra",
                        certificate.getSerialNumber().toString(),
                        keyStorePassword,
                        keyPairIssuer.getPrivate(),
                        certificate);

        System.out.println("\n===== Podaci o izdavacu sertifikata =====");
        System.out.println(certificate.getIssuerX500Principal().getName());
        System.out.println("\n===== Podaci o vlasniku sertifikata =====");
        System.out.println(certificate.getSubjectX500Principal().getName());
        System.out.println("\n===== Certificates =====");
        System.out.println("-------------------------------------------------------");
        System.out.println(certificate);
        System.out.println("-------------------------------------------------------");

    }

    public void saveCertificate(CertificateRole role,
                                String keyPassword,
                                String alias,
                                String keyStorePassword,
                                PrivateKey privateKey,
                                X509Certificate certificate) throws
                                NoSuchProviderException,
                                KeyStoreException,
                                IOException,
                                CertificateException,
                                NoSuchAlgorithmException {
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

    @Override
    public Collection<IssuerAndSubjectData> getCertificates() {
        return this.issuerAndSubjectDataRepository.getSSAndCA();
    }

    @Override
    public void withdrawCertificate(String email) {

        IssuerAndSubjectData forWithdraw = this.issuerAndSubjectDataRepository.findByEmail(email);
        List<IssuerAndSubjectData> all = this.issuerAndSubjectDataRepository.findAll();

        forWithdraw.setCertificateStatus(CertificateStatus.REVOKED);
        this.issuerAndSubjectDataRepository.save(forWithdraw);

        List<IssuerAndSubjectData> selfSigned = new ArrayList<>();
        List<IssuerAndSubjectData> intermediate = new ArrayList<>();
        List<IssuerAndSubjectData> endEntity = new ArrayList<>();

        for (IssuerAndSubjectData issuerAndSubjectData : all) {
            if (issuerAndSubjectData.getCertificateRole().equals(CertificateRole.SELF_SIGNED)) {
                selfSigned.add(issuerAndSubjectData);
            } else if (issuerAndSubjectData.getCertificateRole().equals(CertificateRole.INTERMEDIATE)) {
                intermediate.add(issuerAndSubjectData);
            } else if (issuerAndSubjectData.getCertificateRole().equals(CertificateRole.END_ENTITY)) {
                endEntity.add(issuerAndSubjectData);
            }
        }

        Set<Long> ids = new HashSet<>();
        ids.add(forWithdraw.getId());

        if (forWithdraw.getCertificateRole().equals(CertificateRole.END_ENTITY)) {
            System.out.println("Don't have children!");
        } else {
            for (IssuerAndSubjectData temp : intermediate) {
                for (Long id : ids) {
                    if (temp.getParentId().equals(id)) {
                        temp.setCertificateStatus(CertificateStatus.REVOKED);
                        this.issuerAndSubjectDataRepository.save(temp);
                        ids.add(temp.getId());
                        break;
                    }
                }
            }

            for (IssuerAndSubjectData temp2 : endEntity) {
                for (Long id : ids) {
                    if (temp2.getParentId().equals(id)) {
                        temp2.setCertificateStatus(CertificateStatus.REVOKED);
                        this.issuerAndSubjectDataRepository.save(temp2);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Collection<IssuerAndSubjectData> GetChildCertificate (String email) {
        IssuerAndSubjectData issuer = this.issuerAndSubjectDataRepository.findByEmailIssuer(email);

        return this.issuerAndSubjectDataRepository.findByParentId(issuer.getId());
    }

}
