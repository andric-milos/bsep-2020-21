package com.bezbednost.ftn.bsep.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssuerAndSubjectDataDTO {

    private String firstNameIssuer;
    private String lastNameIssuer;
    private String organizationIssuer;
    // private String organizationUnitIssuer;
    private String countryIssuer;
    private String cityIssuer;
    private String emailIssuer;
    // private String phoneIssuer;

    private String certificateRole;

    private String firstNameSubject;
    private String lastNameSubject;
    private String organizationSubject;
    // private String organizationUnitSubject;
    private String countrySubject;
    private String citySubject;
    private String emailSubject;
    // private String phoneSubject;

    private String startDate;
    private String expiringDate;
}
