package com.bezbednost.ftn.bsep.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class IssuerAndSubject {

    private Long id;

    private String firstName;

    private String lastName;

    private String organization;

    private String organizationUnit;

    private String country;

    private String city;

    private String email;

    private String phone;

    private CertificateStatus certificateRole;

    private String firstNameSubject;

    private String lastNameSubject;

    private String organizationSubject;

    private String organizationUnitSubject;

    private String countrySubject;

    private String citySubject;

    private String emailSubject;

    private String phoneSubject;

    private CertificateStatus certificateStatus;

    private Long parent;

    Date startDate;

    Date expiringDate;

    public IssuerAndSubject(Long id, String firstName, String lastName, String organization,
                                String organizationUnit, String country, String city, String email,
                                String phone, CertificateStatus certificateRole, String firstNameSubject,
                                String lastNameSubject, String organizationSubject, String organizationUnitSubject,
                                String countrySubject, String citySubject, String emailSubject,
                                String phoneSubject, CertificateStatus certificateStatus,
                                Long parent, Date startDate, Date expiringDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.certificateRole = certificateRole;
        this.firstNameSubject = firstNameSubject;
        this.lastNameSubject = lastNameSubject;
        this.organizationSubject = organizationSubject;
        this.organizationUnitSubject = organizationUnitSubject;
        this.countrySubject = countrySubject;
        this.citySubject = citySubject;
        this.emailSubject = emailSubject;
        this.phoneSubject = phoneSubject;
        this.certificateStatus = certificateStatus;
        this.parent = parent;
        this.startDate = startDate;
        this.expiringDate = expiringDate;
    }
}