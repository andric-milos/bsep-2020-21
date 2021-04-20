package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.model.RegistrationRequest;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.model.UserRole;
import com.bezbednost.ftn.bsep.token.RegistrationConfirmationToken;
import com.bezbednost.ftn.bsep.token.RegistrationConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private UserService userService;
    private RegistrationConfirmationTokenService registrationConfirmationTokenService;

    @Autowired
    public RegistrationService(UserService userService,
                               RegistrationConfirmationTokenService registrationConfirmationTokenService) {
        this.userService = userService;
        this.registrationConfirmationTokenService = registrationConfirmationTokenService;
    }

    public String register(RegistrationRequest request) {
        // TODO: check if email is valid (if not, throw an exception)

        return userService.signUp(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER
                )
        );
    }

    public String confirmToken(String token) {
        RegistrationConfirmationToken confirmationToken =
                registrationConfirmationTokenService.getToken(token).orElseThrow(
                        () -> new IllegalStateException("Token " + token + " not found!")
                );

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email is already confirmed!");
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token has expired!");
        }

        // Podesiti confirmedAt i promeniti user-u enabled sa default-nog false na true
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        registrationConfirmationTokenService.saveConfirmationToken(confirmationToken);

        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userService.updateUser(user);

        return "confirmed"; // for now
    }
}
