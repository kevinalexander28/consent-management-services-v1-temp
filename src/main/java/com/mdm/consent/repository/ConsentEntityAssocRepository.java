package com.mdm.consent.repository;

import com.mdm.consent.entity.ConsentEntityAssoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsentEntityAssocRepository extends JpaRepository<ConsentEntityAssoc, Long> {

}
