package com.bezbednost.ftn.bsep.repository;

import com.bezbednost.ftn.bsep.model.IssuerAndSubjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IssuerAndSubjectDataRepository  extends JpaRepository<IssuerAndSubjectData, Long> {

    @Query(value = "SELECT * FROM issuer_and_subject_data dataa WHERE dataa.email_issuer = :email", nativeQuery = true)
    IssuerAndSubjectData findByEmail(String email);

    @Query(value = "SELECT * FROM issuer_and_subject_data d WHERE d.certificate_role = 'SELF_SIGNED' OR d.certificate_role = 'INTERMEDIATE' AND d.certificate_status = 'VALID'", nativeQuery = true)
    Collection<IssuerAndSubjectData> getSSAndCA();

    IssuerAndSubjectData findByEmailIssuer(String email);

    Collection<IssuerAndSubjectData> findByParentId(Long id);

    IssuerAndSubjectData findTopByOrderByIdDesc();

    IssuerAndSubjectData findByAlias(String alias);

    @Query(value = "SELECT * FROM issuer_and_subject_data d WHERE d.certificate_role = 'SELF_SIGNED' OR d.certificate_role = 'INTERMEDIATE' AND d.email_subject = :email", nativeQuery = true)
    Collection<IssuerAndSubjectData> getAllAuthorityCertificatesByEmail(String email);

}

