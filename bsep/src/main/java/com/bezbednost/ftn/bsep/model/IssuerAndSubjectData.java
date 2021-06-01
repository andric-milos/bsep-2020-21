package com.bezbednost.ftn.bsep.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter

@Entity
public class IssuerAndSubjectData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String alias;

    private String firstNameIssuer;
    private String lastNameIssuer;
    private String organizationIssuer;
    private String countryIssuer;
    private String cityIssuer;
    private String emailIssuer;

    @Enumerated(value = EnumType.STRING)
    private CertificateRole certificateRole;

    private String firstNameSubject;
    private String lastNameSubject;
    private String organizationSubject;
    private String countrySubject;
    private String citySubject;
    private String emailSubject;

    // čemu služe polja keyUsage i extendedKeyUsage?
    private boolean[] keyUsage;
    private boolean[] extendedKeyUsage;

    @Enumerated(value = EnumType.STRING)
    private CertificateStatus certificateStatus;

    private Long parentId;

    private Date startDate;
    private Date expiringDate;

    public IssuerAndSubjectData() {
    }

    public IssuerAndSubjectData(String firstNameIssuer,
                                String lastNameIssuer,
                                String organizationIssuer,
                                String countryIssuer,
                                String cityIssuer,
                                String emailIssuer,
                                CertificateRole certificateRole,
                                String firstNameSubject,
                                String lastNameSubject,
                                String organizationSubject,
                                String countrySubject,
                                String citySubject,
                                String emailSubject,
                                CertificateStatus certificateStatus,
                                Date startDate,
                                Date expiringDate,
                                boolean[] keyUsage,
                                boolean[] extendedKeyUsage) {
        this.firstNameIssuer = firstNameIssuer;
        this.lastNameIssuer = lastNameIssuer;
        this.organizationIssuer = organizationIssuer;
        this.countryIssuer = countryIssuer;
        this.cityIssuer = cityIssuer;
        this.emailIssuer = emailIssuer;
        this.certificateRole = certificateRole;
        this.firstNameSubject = firstNameSubject;
        this.lastNameSubject = lastNameSubject;
        this.organizationSubject = organizationSubject;
        this.countrySubject = countrySubject;
        this.citySubject = citySubject;
        this.emailSubject = emailSubject;
        this.certificateStatus = certificateStatus;
        this.startDate = startDate;
        this.expiringDate = expiringDate;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public IssuerAndSubjectData(String firstName,
                                String lastName,
                                String organization,
                                String country,
                                String city,
                                String email,
                                CertificateRole certificateRole,
                                boolean[] keyUsage,
                                boolean[] extendedKeyUsage) {
        this.firstNameIssuer = firstName;
        this.lastNameIssuer = lastName;
        this.organizationIssuer = organization;
        this.countryIssuer = country;
        this.cityIssuer = city;
        this.emailIssuer = email;
        this.certificateRole = certificateRole;
        this.certificateStatus = CertificateStatus.VALID;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public IssuerAndSubjectData(Long id,
                                String firstNameIssuer,
                                String lastNameIssuer,
                                String organizationIssuer,
                                String countryIssuer,
                                String cityIssuer,
                                String emailIssuer,
                                CertificateRole certificateRole,
                                String firstNameSubject,
                                String lastNameSubject,
                                String organizationSubject,
                                String countrySubject,
                                String citySubject,
                                String emailSubject,
                                boolean[] keyUsage,
                                boolean[] extendedKeyUsage,
                                CertificateStatus certificateStatus,
                                Long parentId,
                                Date startDate,
                                Date expiringDate) {
        this.id = id;
        this.firstNameIssuer = firstNameIssuer;
        this.lastNameIssuer = lastNameIssuer;
        this.organizationIssuer = organizationIssuer;
        this.countryIssuer = countryIssuer;
        this.cityIssuer = cityIssuer;
        this.emailIssuer = emailIssuer;
        this.certificateRole = certificateRole;
        this.firstNameSubject = firstNameSubject;
        this.lastNameSubject = lastNameSubject;
        this.organizationSubject = organizationSubject;
        this.countrySubject = countrySubject;
        this.citySubject = citySubject;
        this.emailSubject = emailSubject;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.certificateStatus = certificateStatus;
        this.parentId = parentId;
        this.startDate = startDate;
        this.expiringDate = expiringDate;
    }

    public IssuerAndSubjectData(String alias,
                                User subject,
                                User issuer,
                                CertificateRole certificateRole,
                                Date startDate,
                                Date expiringDate) {
        this.alias = alias;

        this.firstNameIssuer = issuer.getFirstName();
        this.lastNameIssuer = issuer.getLastName();
        this.organizationIssuer = issuer.getOrganization();
        this.countryIssuer = issuer.getCountry();
        this.cityIssuer = issuer.getCity();
        this.emailIssuer = issuer.getEmail();

        this.firstNameSubject = subject.getFirstName();
        this.lastNameSubject = subject.getLastName();
        this.organizationSubject = subject.getOrganization();
        this.countrySubject = subject.getCountry();
        this.citySubject = subject.getCity();
        this.emailSubject = subject.getEmail();

        this.certificateRole = certificateRole;

        this.startDate = startDate;
        this.expiringDate = expiringDate;
    }
}