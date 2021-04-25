package com.bezbednost.ftn.bsep.service.impl;

import com.bezbednost.ftn.bsep.model.Administrator;
import com.bezbednost.ftn.bsep.model.Authority;
import com.bezbednost.ftn.bsep.model.User;
import com.bezbednost.ftn.bsep.model.UserTokenState;
import com.bezbednost.ftn.bsep.repository.AuthorityRepository;
import com.bezbednost.ftn.bsep.security.TokenUtils;
import com.bezbednost.ftn.bsep.security.auth.JwtAuthenticationRequest;
import com.bezbednost.ftn.bsep.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class AuthorityService implements IAuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;


    @Override
    public Set<Authority> findByName(String name) {
        Authority authority = this.authorityRepository.findByName(name);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public Set<Authority> findById(Long id) {
        Authority authority = this.authorityRepository.getOne(id);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) {
        final Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String token = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(token, expiresIn);
    }

}
