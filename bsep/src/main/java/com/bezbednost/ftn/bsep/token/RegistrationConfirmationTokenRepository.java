package com.bezbednost.ftn.bsep.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationConfirmationTokenRepository
        extends JpaRepository<RegistrationConfirmationToken, Long> {
    Optional<RegistrationConfirmationToken> findByToken(String token);
}
