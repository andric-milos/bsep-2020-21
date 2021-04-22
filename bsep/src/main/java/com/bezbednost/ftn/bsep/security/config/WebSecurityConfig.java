package com.bezbednost.ftn.bsep.security.config;

import com.bezbednost.ftn.bsep.security.TokenUtils;
import com.bezbednost.ftn.bsep.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private TokenUtils tokenUtils;

    @Autowired
    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder,
                             UserService userService, TokenUtils tokenUtils) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();

        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/registration/**")
            .permitAll()
            .antMatchers("/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated().and()
            .formLogin();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
