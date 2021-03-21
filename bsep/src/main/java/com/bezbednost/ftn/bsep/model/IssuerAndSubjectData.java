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

    @Column
    private String firstNameIssuer;

    @Column
    private String lastNameIssuer;

    @Column
    private String organizationIssuer;

    @Column
    private String countryIssuer;

    @Column
    private String cityIssuer;

    @Column
    private String emailIssuer;

    @Enumerated
    private TypeOfEntity typeOfEntity;

    @Enumerated(value = EnumType.STRING)
    private CertificateRole certificateRole;

    // @Transient anotacija sprecava cuvanje podataka u mysql
    @Transient
    private String firstNameSubject;

    @Transient
    private String lastNameSubject;

    @Transient
    private String organizationSubject;

    @Transient
    private String countrySubject;

    @Transient
    private String citySubject;

    @Transient
    private String emailSubject;

    @Column
    private boolean[] keyUsage;

    @Column
    private boolean[] extendedKeyUsage;

    @Enumerated(value = EnumType.STRING)
    private CertificateStatus certificateStatus;

    @Column
    private Long parentId;

    @Column
    Date startDate;

    @Column
    Date expiringDate;

    public IssuerAndSubjectData() {
    }

    public IssuerAndSubjectData(String firstNameIssuer, String lastNameIssuer, String organizationIssuer,
                                String countryIssuer, String cityIssuer, String emailIssuer,
                                CertificateRole certificateRole, String firstNameSubject,
                                String lastNameSubject, String organizationSubject, String countrySubject,
                                String citySubject, String emailSubject, CertificateStatus certificateStatus,
                                Date startDate, Date expiringDate, boolean[] keyUsage,boolean[] extendedKeyUsage) {
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

    public IssuerAndSubjectData(String firstName, String lastName, String organization, String country,
                                String city, String email, TypeOfEntity typeOfEntity,
                                CertificateRole certificateRole,boolean[] keyUsage,boolean[] extendedKeyUsage) {
        this.firstNameIssuer = firstName;
        this.lastNameIssuer = lastName;
        this.organizationIssuer = organization;
        this.countryIssuer = country;
        this.cityIssuer = city;
        this.emailIssuer = email;
        this.typeOfEntity = typeOfEntity;
        this.certificateRole = certificateRole;
        this.certificateStatus = CertificateStatus.VALID;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public IssuerAndSubjectData(Long id, String firstNameIssuer, String lastNameIssuer, String organizationIssuer,
                                String countryIssuer, String cityIssuer, String emailIssuer, TypeOfEntity typeOfEntity,
                                CertificateRole certificateRole, String firstNameSubject, String lastNameSubject, String organizationSubject,
                                String countrySubject, String citySubject, String emailSubject, boolean[] keyUsage, boolean[] extendedKeyUsage,
                                CertificateStatus certificateStatus, Long parentId, Date startDate, Date expiringDate) {
        this.id = id;
        this.firstNameIssuer = firstNameIssuer;
        this.lastNameIssuer = lastNameIssuer;
        this.organizationIssuer = organizationIssuer;
        this.countryIssuer = countryIssuer;
        this.cityIssuer = cityIssuer;
        this.emailIssuer = emailIssuer;
        this.typeOfEntity = typeOfEntity;
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

}