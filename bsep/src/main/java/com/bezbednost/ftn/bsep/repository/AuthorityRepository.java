package com.bezbednost.ftn.bsep.repository;

import com.bezbednost.ftn.bsep.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}