package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.service.IKeyStoreService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class KeyStoreService implements IKeyStoreService {
    @Override
    public boolean doesKeyStoreExist(String certificateRole) {
        File file = new File("src/main/resources/keystores/" + certificateRole + ".jks");
        return file.exists();
    }

    @Override
    public X509Certificate loadCertificate(String role, String alias, String password) throws NoSuchProviderException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return null;
    }
}
