package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.repository.UserRepository;
import com.bezbednost.ftn.bsep.token.RegistrationConfirmationToken;
import com.bezbednost.ftn.bsep.token.RegistrationConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RegistrationConfirmationTokenService registrationConfirmationTokenService;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       RegistrationConfirmationTokenService registrationConfirmationTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.registrationConfirmationTokenService = registrationConfirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with " + email + " not found!")
        );
    }

    public String signUp(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            // TODO: if user does exist and it hasn't confirmed its email, send the confirmation mail again, otherwise just throw email is already takens exception

            throw new IllegalStateException("Email " + user.getEmail() + " is already taken!");
        }

        // TODO: hash & salt instead of Bcrypt
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        RegistrationConfirmationToken confirmationToken = new RegistrationConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        registrationConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: send email

        return token;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
