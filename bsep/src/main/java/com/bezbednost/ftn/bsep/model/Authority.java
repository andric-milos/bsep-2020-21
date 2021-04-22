package com.bezbednost.ftn.bsep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Authority implements GrantedAuthority {

    @Id
    @Column
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long id;

    @Column
    String name;

    @Override
    public String getAuthority() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
