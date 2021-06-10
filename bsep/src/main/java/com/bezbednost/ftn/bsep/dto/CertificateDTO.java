package com.bezbednost.ftn.bsep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CertificateDTO {
    private Long id;
    private String alias;
    private String certificateRole;
    private Long parentId;

    private String firstNameIssuer;
    private String lastNameIssuer;
    private String organizationIssuer;
    private String countryIssuer;
    private String cityIssuer;
    private String emailIssuer;

    private String firstNameSubject;
    private String lastNameSubject;
    private String organizationSubject;
    private String countrySubject;
    private String citySubject;
    private String emailSubject;

    // maybe it's best for dates to be passed as milliseconds?
    // private Date startDate;
    // private Date expiringDate;
}
