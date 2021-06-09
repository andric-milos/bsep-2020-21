package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.certificate.CertificateGenerator;
import com.bezbednost.ftn.bsep.certificate.Generators;
import com.bezbednost.ftn.bsep.dto.NewCertificateDTO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CertificateService implements ICertificateService {

    private KeyStoreService keyStoreService;
    private UserService userService;

    private Generators generators = new Generators();
    private CertificateGenerator certificateGenerator = new CertificateGenerator();

    private IssuerAndSubjectDataRepository issuerAndSubjectDataRepository;

    @Autowired
    public CertificateService(KeyStoreService keyStoreService,
                              UserService userService,
                              IssuerAndSubjectDataRepository issuerAndSubjectDataRepository) {
        this.keyStoreService = keyStoreService;
        this.userService = userService;
        this.issuerAndSubjectDataRepository = issuerAndSubjectDataRepository;
    }

    @Override
    public void issueCertificate(NewCertificateDTO newCertificateDTO) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException, IOException, UnrecoverableEntryException {
        // repository.getOne throws EntityNotFoundException if entity isn't found
        IssuerAndSubjectData issuerAndSubjectData = issuerAndSubjectDataRepository.getOne(newCertificateDTO.getId());
        String keyStorePassword = newCertificateDTO.getKeyStorePassword();
        User subject = userService.getUserById(newCertificateDTO.getSubjectID());

        /* u getCertificateRole kao drugi parametar (issuersEmail) prosleđujemo
         * issuerAndSubjectData.getEmailSubject() što i jeste email issuer-a, jer
         * issuerAndSubjectData predstavlja podatke o issuer sertifikatu, što znači
         * da je on tu subject ...
         */
        CertificateRole subjectCertificateRole = getCertificateRole(
                newCertificateDTO.getCertificateType(),
                issuerAndSubjectData.getEmailSubject(),
                subject.getEmail()
        );

        CertificateRole issuerCertificateRole = issuerAndSubjectData.getCertificateRole();

        /* else scenario: ako keystore ne postoji, kreirati ga sa lozinkom koja je prosleđena sa klijenta,
         * malo je glupo, ali svakako ne bi ni trebalo da dođe do ovog scenaria, osim pri inicijalnom
         * kreiranju keystore-eva ... */
        KeyStore keyStoreSubject = null;
        if (this.keyStoreService.doesKeyStoreExist(subjectCertificateRole.toString())) {
            try {
                System.out.println("Success!");
                keyStoreSubject = KeyStore.getInstance("JKS", "SUN");
                keyStoreSubject.load(new FileInputStream("src/main/resources/keystores/" + subjectCertificateRole.toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            } catch (CertificateException e) {
                System.out.println("Certificate couldn't be loaded!");
                throw new KeyStoreException();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Algorithm couldn't be found!!");
                throw new KeyStoreException();
            }
        } else {
            try {
                keyStoreSubject = KeyStore.getInstance("JKS", "SUN");
                String file = ("src/main/resources/keystores/" + subjectCertificateRole.toString().toLowerCase() + ".jks");

                keyStoreSubject.load(null, keyStorePassword.toCharArray());
                keyStoreSubject.store(new FileOutputStream(file), keyStorePassword.toCharArray());
            } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }
        }

        /* obično su issuer-ov sertifikat i ovaj koji će on upravo da izda različitog CertificateRole-a
        *  -> zato moramo da loadujemo dva keystore-a, jer ćemo u jedan da sačuvamo upravo izdati
        *  sertifikat, a drugi keystore nam treba da učitamo PrivateKey issuer-a */
        KeyStore keyStoreIssuer = null;
        if (this.keyStoreService.doesKeyStoreExist(subjectCertificateRole.toString())) {
            try {
                System.out.println("Success!");
                keyStoreIssuer = KeyStore.getInstance("JKS", "SUN");
                keyStoreIssuer.load(new FileInputStream("src/main/resources/keystores/" + issuerCertificateRole.toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            } catch (CertificateException e) {
                System.out.println("Certificate couldn't be loaded!");
                throw new KeyStoreException();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Algorithm couldn't be found!!");
                throw new KeyStoreException();
            }
        } else {
            try {
                keyStoreIssuer = KeyStore.getInstance("JKS", "SUN");
                String file = ("src/main/resources/keystores/" + subjectCertificateRole.toString().toLowerCase() + ".jks");

                keyStoreIssuer.load(null, keyStorePassword.toCharArray());
                keyStoreIssuer.store(new FileOutputStream(file), keyStorePassword.toCharArray());
            } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }
        }

        assert keyStoreIssuer != null;
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStoreIssuer.getEntry(issuerAndSubjectData.getAlias(), new KeyStore.PasswordProtection("sifra".toCharArray())); // svaki put kada snimamo novi KeyEntry u keystore, uvek stavimo da je lozinka "sifra", zato ovde otključavamo sa baš tom lozinkom
        PrivateKey issuersPrivateKey = entry.getPrivateKey();

        KeyPair keyPairSubject = generators.generateKeyPair();

        // izvršiti proveru da li generisani serialNumber postoji u bazi podataka?
        String serialNumber = generators.randomBigInteger().toString(); // this will be certificate's serial number and also alias for saving it in the keystore
        SubjectData subjectData = generators.generateSubjectData(
                serialNumber,
                subject.getId(),
                subject.getFirstName(),
                subject.getLastName(),
                subject.getOrganization(),
                subject.getCountry(),
                subject.getCity(),
                subject.getEmail(),
                keyPairSubject.getPublic(),
                newCertificateDTO.getStartDate(),
                newCertificateDTO.getEndDate()
        );

        /* dobavljamo issuer-a iz baze podataka sa getUserByEmail, a prosleđeno je
         * issuerAndSubjectData.getEmailSubject() -> kako to? kako emailSubject, a issuer nam treba
         * --> issuerAndSubjectData predstavlja podatke o issuer sertifikatu, što znači da je
         * subject u issuerAndSubjectData naš issuer ...
         */
        User issuer = userService.getUserByEmail(issuerAndSubjectData.getEmailSubject());
        IssuerData issuerData = generators.generateIssuerData(
                issuer.getId(),
                issuersPrivateKey,
                issuer.getFirstName(),
                issuer.getLastName(),
                issuer.getOrganization(),
                issuer.getCountry(),
                issuer.getCity(),
                issuer.getEmail()
        );

        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);
        saveCertificate(
                subjectCertificateRole,
                "sifra",
                serialNumber,
                keyStorePassword,
                keyPairSubject.getPrivate(),
                certificate
        );

        IssuerAndSubjectData certificateData = new IssuerAndSubjectData(
                serialNumber,
                subject,
                issuer,
                subjectCertificateRole,
                newCertificateDTO.getStartDate(),
                newCertificateDTO.getEndDate()
        );

        /* čuvanje sertifikata u bazu podataka:
         * 1) ako je sertifikat self-signed, da bismo set-ovali parentId (pošto je sam
         * sebi parent), moramo prvo da snimimo sertifikata u bazu, pa naknadno
         * da set-ujemo parentId i onda takvog da ga opet samo update-ujemo u bazi
         * 2) ako nije self-signed, samo snimimo
         */
        if (subjectCertificateRole.equals(CertificateRole.SELF_SIGNED)) {
            IssuerAndSubjectData data = this.issuerAndSubjectDataRepository.save(certificateData);
            data.setParentId(data.getId());
            this.issuerAndSubjectDataRepository.save(data);
        } else {
            certificateData.setParentId(issuerAndSubjectData.getId());
            this.issuerAndSubjectDataRepository.save(certificateData);
        }

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
            System.out.println("CertificateService [method: saveCertificate] : Wrong password!");
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

    /**
     * It receives certificateRole in shape of a String, from client application, which should
     * be either "CA" or "END_ENTITY". It should return CertificateRole object, but if something
     * isn't right e.g. certificateRoleString equals to null or String other than "END_ENTITY"
     * or "CA" then the function returns null.
     *
     * @param certificateRoleString String ("END_ENTITY" or "CA")
     * @param issuersEmail          issuer's email address
     * @param subjectsEmail         subject's email address
     * @return                      CertificateRole object or null
     */
    public CertificateRole getCertificateRole(String certificateRoleString,
                                              String issuersEmail,
                                              String subjectsEmail) {
        if (certificateRoleString.equals("END_ENTITY")) {
            return CertificateRole.END_ENTITY;
        } else if (certificateRoleString.equals("CA")) {
            if (issuersEmail.equals(subjectsEmail)) {
                return CertificateRole.SELF_SIGNED;
            } else {
                return CertificateRole.INTERMEDIATE;
            }
        }

        return null;
    }

    /* this method serves for issuing certificates outside of the client
    *  (using fixed data inside of the method - of course, that data should
    *  be valid and existing in the database, otherwise the method will throw
    *  an error or an exception */
    public void issueDefaultCertificate() throws KeyStoreException, ParseException, NoSuchAlgorithmException, CertificateException, NoSuchProviderException, IOException, UnrecoverableEntryException {
        CertificateRole certificateRoleSubject = CertificateRole.INTERMEDIATE;
        CertificateRole certificateRoleIssuer = CertificateRole.SELF_SIGNED;
        String keyStorePassword = "JKSSifra";  // KOJA JE ŠIFRA ZA KEYSTORE?
        String issuerAlias = "3757053589842";

        User user = new User();
        user.setId(3L);
        user.setFirstName("Milos");
        user.setLastName("Andric");
        user.setOrganization("ftn");
        user.setCountry("Serbia");
        user.setCity("Novi Sad");
        user.setEmail("milos@gmail.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Admin");
        user2.setLastName("Admin");
        user2.setOrganization("admin");
        user2.setCountry("Serbia");
        user2.setCity("Novi Sad");
        user2.setEmail("admin@gmail.com");

        KeyStore keyStoreSubject = null;
        if (this.keyStoreService.doesKeyStoreExist(certificateRoleSubject.toString())) {
            try {
                System.out.println("Success!");
                keyStoreSubject = KeyStore.getInstance("JKS", "SUN");
                keyStoreSubject.load(new FileInputStream("src/main/resources/keystores/" + certificateRoleSubject.toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            } catch (CertificateException e) {
                System.out.println("Certificate couldn't be loaded!");
                throw new KeyStoreException();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Algorithm couldn't be found!!");
                throw new KeyStoreException();
            } catch (NoSuchProviderException e) {
                System.out.println("Provider couldn't be found!!");
                throw new KeyStoreException();
            }
        } else {
            try {
                keyStoreSubject = KeyStore.getInstance("JKS", "SUN");
                String file = ("src/main/resources/keystores/" + certificateRoleSubject.toString().toLowerCase() + ".jks");

                keyStoreSubject.load(null, keyStorePassword.toCharArray());
                keyStoreSubject.store(new FileOutputStream(file), keyStorePassword.toCharArray());
            } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }
        }

        KeyStore keyStoreIssuer = null;
        if (this.keyStoreService.doesKeyStoreExist(certificateRoleIssuer.toString())) {
            try {
                System.out.println("Success!");
                keyStoreIssuer = KeyStore.getInstance("JKS", "SUN");
                keyStoreIssuer.load(new FileInputStream("src/main/resources/keystores/" + certificateRoleIssuer.toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
            } catch (IOException e) {
                System.out.println("Wrong password!");
                throw new KeyStoreException();
            } catch (CertificateException e) {
                System.out.println("Certificate couldn't be loaded!");
                throw new KeyStoreException();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Algorithm couldn't be found!!");
                throw new KeyStoreException();
            } catch (NoSuchProviderException e) {
                System.out.println("Provider couldn't be found!!");
                throw new KeyStoreException();
            }
        } else {
            try {
                keyStoreIssuer = KeyStore.getInstance("JKS", "SUN");
                String file = ("src/main/resources/keystores/" + certificateRoleIssuer.toString().toLowerCase() + ".jks");

                keyStoreIssuer.load(null, keyStorePassword.toCharArray());
                keyStoreIssuer.store(new FileOutputStream(file), keyStorePassword.toCharArray());
            } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
                e.printStackTrace();
            }
        }

        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStoreIssuer.getEntry(issuerAlias, new KeyStore.PasswordProtection("sifra".toCharArray()));
        PrivateKey issuersPrivateKey = entry.getPrivateKey();

        KeyPair keyPairSubject = generators.generateKeyPair();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String startDateString = "01-01-2000";
        String endDateString = "01-01-2050";
        Date startDate = sdf.parse(startDateString);
        Date endDate = sdf.parse(endDateString);

        // izvršiti proveru da li generisani serialNumber postoji u bazi podataka?
        String serialNumber = generators.randomBigInteger().toString(); // this will be certificate's serial number and also alias for saving it in the keystore
        SubjectData subjectData = generators.generateSubjectData(
                serialNumber,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getOrganization(),
                user.getCountry(),
                user.getCity(),
                user.getEmail(),
                keyPairSubject.getPublic(),
                startDate,
                endDate
        );

        IssuerData issuerData = generators.generateIssuerData(
                user2.getId(),
                issuersPrivateKey,
                user2.getFirstName(),
                user2.getLastName(),
                user2.getOrganization(),
                user2.getCountry(),
                user2.getCity(),
                user2.getEmail()
        );

        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);
        saveCertificate(
                certificateRoleSubject,
                "sifra",
                serialNumber,
                keyStorePassword,
                keyPairSubject.getPrivate(),
                certificate
        );

        IssuerAndSubjectData certificateData = new IssuerAndSubjectData(
                serialNumber,
                user,
                user2,
                certificateRoleSubject,
                startDate,
                endDate
        );

        if (certificateRoleSubject.equals(CertificateRole.SELF_SIGNED)) {
            // ovo ovako, jer je self-signed, pa moramo naknadno set-ovati parentId
            IssuerAndSubjectData data = this.issuerAndSubjectDataRepository.save(certificateData);
            data.setParentId(data.getId());
            this.issuerAndSubjectDataRepository.save(data);
        } else {
            IssuerAndSubjectData issuerCertificateData = issuerAndSubjectDataRepository.findByAlias(issuerAlias);
            certificateData.setParentId(issuerCertificateData.getId());
            this.issuerAndSubjectDataRepository.save(certificateData);
        }

        System.out.println("\n===== Podaci o izdavacu sertifikata =====");
        System.out.println(certificate.getIssuerX500Principal().getName());
        System.out.println("\n===== Podaci o vlasniku sertifikata =====");
        System.out.println(certificate.getSubjectX500Principal().getName());
        System.out.println("\n===== Certificates =====");
        System.out.println("-------------------------------------------------------");
        System.out.println(certificate);
        System.out.println("-------------------------------------------------------");
    }
}
