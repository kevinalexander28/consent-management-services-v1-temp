package com.mdm.consent.repository;

import com.mdm.consent.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsentRepository extends JpaRepository<Consent, Long> {

    // Melakukan pencarian by CIF ID
    List<Consent> findByCifId(String cifId);
}
