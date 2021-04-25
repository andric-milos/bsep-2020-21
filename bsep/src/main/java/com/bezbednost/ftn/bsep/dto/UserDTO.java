package com.bezbednost.ftn.bsep.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private Set<String> userRoles;

    public UserDTO(UserDetails userDetails){
        this.email = userDetails.getUsername();
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
        userRoles = new HashSet<>();
        for (GrantedAuthority a:authorities) {
            userRoles.add(a.getAuthority());
        }
    }
}
