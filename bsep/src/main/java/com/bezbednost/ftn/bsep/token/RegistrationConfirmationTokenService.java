package com.bezbednost.ftn.bsep.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationConfirmationTokenService {

    private RegistrationConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public RegistrationConfirmationTokenService(RegistrationConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveConfirmationToken(RegistrationConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<RegistrationConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }
}
