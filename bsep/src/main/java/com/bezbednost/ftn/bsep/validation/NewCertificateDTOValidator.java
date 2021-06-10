package com.bezbednost.ftn.bsep.validation;

import com.bezbednost.ftn.bsep.dto.NewCertificateDTO;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
public class NewCertificateDTOValidator {

    public static boolean validate(NewCertificateDTO newCertificateDTO) throws ParseException {
        if (newCertificateDTO.getId() == null || newCertificateDTO.getSubjectID() == null ||
            newCertificateDTO.getCertificateType() == null || newCertificateDTO.getKeyStorePassword() == null ||
            newCertificateDTO.getStartDate() == null || newCertificateDTO.getEndDate() == null)
            return false;

        if (newCertificateDTO.getCertificateType().equals("") ||
            newCertificateDTO.getKeyStorePassword().equals("") ||
            newCertificateDTO.getStartDate().equals("") ||
            newCertificateDTO.getEndDate().equals(""))
            return false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(newCertificateDTO.getStartDate());
        Date endDate = dateFormat.parse(newCertificateDTO.getEndDate());

        int flag = startDate.compareTo(endDate);
        if (flag >= 0)
            return false;

        return true;
    }
}
