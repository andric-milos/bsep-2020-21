package com.bezbednost.ftn.bsep.generators;

import com.bezbednost.ftn.bsep.model.SubjectData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.KeyPair;
import java.util.Date;

public class SubjectDataGenerator {

    public SubjectDataGenerator() {}

    public static SubjectData generateSubjectData(String firstname,
                                                  String lastname,
                                                  String organization,
                                                  String countryCode,
                                                  String city,
                                                  String email,
                                                  Date startDate,
                                                  Date endDate,
                                                  String serialNumber) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.CN, firstname + " " + lastname);
        builder.addRDN(BCStyle.GIVENNAME, firstname);
        builder.addRDN(BCStyle.SURNAME, lastname);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.C, countryCode);
        // builder.addRDN(BCStyle., ); // city?
        builder.addRDN(BCStyle.E, email);
        // UID?

        return new SubjectData(builder.build(), serialNumber, startDate, endDate);
    }

    public static SubjectData generateSubjectData(String firstname,
                                                  String lastname,
                                                  String organization,
                                                  String countryCode,
                                                  String city,
                                                  String email,
                                                  String startDateString,
                                                  String endDateString,
                                                  String serialNumber) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.CN, firstname + " " + lastname);
        builder.addRDN(BCStyle.GIVENNAME, firstname);
        builder.addRDN(BCStyle.SURNAME, lastname);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.C, countryCode);
        // builder.addRDN(BCStyle., ); // city?
        builder.addRDN(BCStyle.E, email);
        // UID?

        Date startDate = new Date(startDateString);
        Date endDate = new Date(endDateString);

        return new SubjectData(builder.build(), serialNumber, startDate, endDate);
    }
}
