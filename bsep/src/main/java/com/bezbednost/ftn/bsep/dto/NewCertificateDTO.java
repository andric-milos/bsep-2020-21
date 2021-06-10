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
public class NewCertificateDTO {
    private Long id; // issuer's certificate id (issuerAndSubjectData id)
    private Long subjectID;
    private String certificateType;
    private String keyStorePassword;
    private String startDate;
    private String endDate;
}
