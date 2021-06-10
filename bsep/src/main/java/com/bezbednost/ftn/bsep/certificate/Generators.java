package com.bezbednost.ftn.bsep.certificate;

import com.bezbednost.ftn.bsep.model.CertificateRole;
import com.bezbednost.ftn.bsep.model.IssuerData;

import com.bezbednost.ftn.bsep.model.SubjectData;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.math.BigInteger;
import java.security.*;
import java.util.Calendar;
import java.util.Date;

public class Generators {

    public Generators() {
    }

    public IssuerData generateIssuerData(Long issuersId,
                                         PrivateKey privateKey,
                                         String firstName,
                                         String lastName,
                                         String organization,
                                         String country,
                                         String city,
                                         String email) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.GIVENNAME, firstName);
        builder.addRDN(BCStyle.SURNAME, lastName);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.COUNTRY_OF_RESIDENCE, country);
        builder.addRDN(BCStyle.L, city);
        builder.addRDN(BCStyle.E, email);
        builder.addRDN(BCStyle.UID, issuersId.toString());

        return new IssuerData(privateKey, builder.build());
    }

    public SubjectData generateSubjectData(String serialNumber,
                                           Long subjectsId,
                                           String firstName,
                                           String lastName,
                                           String organization,
                                           String country,
                                           String city,
                                           String email,
                                           PublicKey publicKey,
                                           Date startDate,
                                           Date endDate) {
        try {
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

            builder.addRDN(BCStyle.GIVENNAME, firstName);
            builder.addRDN(BCStyle.SURNAME, lastName);
            builder.addRDN(BCStyle.O, organization);
            builder.addRDN(BCStyle.COUNTRY_OF_RESIDENCE, country);
            builder.addRDN(BCStyle.L, city);
            builder.addRDN(BCStyle.E, email);
            builder.addRDN(BCStyle.UID, subjectsId.toString());

            return new SubjectData(publicKey, builder.build(), serialNumber, startDate, endDate);
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

    private void buildDataWithoutId(X500NameBuilder builder,
                                    String firstName,
                                    String lastName,
                                    String organization,
                                    String country,
                                    String city,
                                    String email) {
        builder.addRDN(BCStyle.GIVENNAME, firstName);
        builder.addRDN(BCStyle.SURNAME, lastName);
        builder.addRDN(BCStyle.O, organization);
        builder.addRDN(BCStyle.COUNTRY_OF_RESIDENCE, country);
        builder.addRDN(BCStyle.EmailAddress, email);
    }

    public BigInteger randomBigInteger() {
        BigInteger maxLimit = new BigInteger("5000000000000");
        BigInteger minLimit = new BigInteger("25000");

        BigInteger bigInteger = maxLimit.subtract(minLimit);

        // Random randNum = new Random();
        SecureRandom randNum = new SecureRandom();

        int len = maxLimit.bitLength();

        BigInteger res = new BigInteger(len, randNum);

        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);

        return res;
    }
}
