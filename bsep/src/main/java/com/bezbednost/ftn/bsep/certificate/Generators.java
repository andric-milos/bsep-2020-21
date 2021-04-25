package com.bezbednost.ftn.bsep.certificate;

import com.bezbednost.ftn.bsep.model.CertificateRole;
import com.bezbednost.ftn.bsep.model.IssuerData;

import com.bezbednost.ftn.bsep.model.SubjectData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.*;
import java.util.Calendar;
import java.util.Date;

public class Generators {

    public Generators() {
    }

    public IssuerData generateIssuerData(Long sn, PrivateKey privateKey, String firstName,
                                         String lastName, String organization, String country,
                                         String city, String email) {

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        this.buildData(builder, firstName, lastName, organization, country, city, email, sn.toString());

        return new IssuerData(privateKey, builder.build());
    }

    public SubjectData generateSubjectData(Long sn, String firstName, String lastName, String organization,
                                           String country, String city, String email,  CertificateRole role) {
        try {
            KeyPair keyPairSubject = generateKeyPair();
            Date startDate = new Date();

            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            if(role.equals(CertificateRole.SELF_SIGNED)) {
                c.add(Calendar.YEAR, 30);
            } else if(role.equals(CertificateRole.INTERMEDIATE)){
                c.add(Calendar.YEAR, 20);
            } else {
                c.add(Calendar.YEAR, 10);
            }

            Date endDate = c.getTime();

            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            this.buildData(builder, firstName, lastName, organization, country, city, email, sn.toString());


            return new SubjectData(keyPairSubject.getPublic(), builder.build(),
                    sn.toString(), startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(2048, random);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buildData(X500NameBuilder builder, String firstName, String lastName, String organization,
                            String country, String city, String email, String sn) {

        builder.addRDN(BCStyle.GIVENNAME, firstName);
        builder.addRDN(BCStyle.SURNAME, lastName);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.COUNTRY_OF_RESIDENCE, country);
        builder.addRDN(BCStyle.EmailAddress, email);
        builder.addRDN(BCStyle.UID, sn);

    }


    private void buildDataWithoutId(X500NameBuilder builder, String firstName, String lastName, String organization,
                           String country, String city, String email) {

        builder.addRDN(BCStyle.GIVENNAME, firstName);
        builder.addRDN(BCStyle.SURNAME, lastName);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.COUNTRY_OF_RESIDENCE, country);
        builder.addRDN(BCStyle.EmailAddress, email);

    }

}
