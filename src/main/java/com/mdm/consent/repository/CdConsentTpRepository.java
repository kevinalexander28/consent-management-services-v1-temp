package com.mdm.consent.repository;

import com.mdm.consent.entity.CdConsentTp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CdConsentTpRepository extends JpaRepository<CdConsentTp, Long> {

    // Mencari list of consent dimana clause_code bukan 0
    @Query(value = "SELECT * FROM cdconsenttp where clause_code <> 0", nativeQuery = true)
    List<CdConsentTp> findAllNotZero();
}
