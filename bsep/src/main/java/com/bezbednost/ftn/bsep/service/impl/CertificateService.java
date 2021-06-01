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
        CertificateRole certificateRole = getCertificateRole(
                newCertificateDTO.getCertificateType(),
                issuerAndSubjectData.getEmailSubject(),
                subject.getEmail()
        );

        /* provera da li keystore postoji
         * ako ne postoji kreirati ga? */
        KeyStore keyStore = null;
        if (this.keyStoreService.doesKeyStoreExist(certificateRole.toString())) {
            try {
                System.out.println("Success!");
                keyStore = KeyStore.getInstance("JKS", "SUN");
                keyStore.load(new FileInputStream("src/main/resources/keystores/" + certificateRole.toString().toLowerCase() + ".jks"), keyStorePassword.toCharArray());
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
        }

        assert keyStore != null;
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(issuerAndSubjectData.getAlias(), new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));
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
                certificateRole,
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
                certificateRole,
                newCertificateDTO.getStartDate(),
                newCertificateDTO.getEndDate()
        );

        /* čuvanje sertifikata u bazu podataka:
         * 1) ako je sertifikat self-signed, da bismo set-ovali parentId (pošto je sam
         * sebi parent), moramo prvo da snimimo sertifikata u bazu, pa naknadno
         * da set-ujemo parentId i onda takvog da ga opet samo update-ujemo u bazi
         * 2) ako nije self-signed, samo snimimo
         */
        if (certificateRole.equals(CertificateRole.SELF_SIGNED)) {
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
}
