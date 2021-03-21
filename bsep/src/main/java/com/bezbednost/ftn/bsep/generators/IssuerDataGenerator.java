package com.bezbednost.ftn.bsep.generators;

import com.bezbednost.ftn.bsep.model.IssuerData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.PrivateKey;
import java.util.Date;

public class IssuerDataGenerator {

    public IssuerDataGenerator() {}

    public static IssuerData generateIssuerData(String firstname,
                                                String lastname,
                                                String organization,
                                                String countryCode,
                                                String city,
                                                String email,
                                                PrivateKey issuerPrivateKey) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.CN, firstname + " " + lastname);
        builder.addRDN(BCStyle.GIVENNAME, firstname);
        builder.addRDN(BCStyle.SURNAME, lastname);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.C, countryCode);
        // builder.addRDN(BCStyle., ); // city?
        builder.addRDN(BCStyle.E, email);
        // UID?

        return new IssuerData(issuerPrivateKey, builder.build());
    }
}
