package com.bezbednost.ftn.bsep.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IssuerAndSubjectData {

    // private Long id;
    private String firstNameIssuer;
    private String lastNameIssuer;
    private String organizationIssuer;
    // private String organizationUnitIssuer;
    private String countryIssuer;
    private String cityIssuer;
    private String emailIssuer;
    // private String phoneIssuer;

    private CertificateRole certificateRole;

    private String firstNameSubject;
    private String lastNameSubject;
    private String organizationSubject;
    // private String organizationUnitSubject;
    private String countrySubject;
    private String citySubject;
    private String emailSubject;
    // private String phoneSubject;

    // private CertificateStatus certificateStatus;

    // private Long parent;

    private Date startDate;
    private Date expiringDate;

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
                                Date startDate,
                                Date expiringDate) {
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
        this.startDate = startDate;
        this.expiringDate = expiringDate;
    }
}