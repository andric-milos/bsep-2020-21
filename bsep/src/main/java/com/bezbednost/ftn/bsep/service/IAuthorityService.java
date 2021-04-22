package com.bezbednost.ftn.bsep.service;

import com.bezbednost.ftn.bsep.model.Authority;
import com.bezbednost.ftn.bsep.model.UserTokenState;
import com.bezbednost.ftn.bsep.security.auth.JwtAuthenticationRequest;

import java.util.Set;

public interface IAuthorityService {
    Set<Authority> findByName(String name);

    Set<Authority> findById(Long id);

    UserTokenState login(JwtAuthenticationRequest authenticationRequest);

}
