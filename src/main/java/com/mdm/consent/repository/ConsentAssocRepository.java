package com.mdm.consent.repository;

import com.mdm.consent.entity.ConsentAssoc;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ConsentAssocRepository extends JpaRepository<ConsentAssoc, Long> {

}
