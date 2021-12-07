package com.mdm.consent.service;

import com.mdm.consent.dto.AddConsentEntityAssocRequestWrapper;
import com.mdm.consent.dto.DeleteConsentEntityAssocRequestWrapper;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentEntityAssoc;
import com.mdm.consent.repository.ClauseRepository;
import com.mdm.consent.repository.ConsentEntityAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsentEntityAssocService {

    private static final Logger logger = LoggerFactory.getLogger(ClauseService.class);

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    public List<String> addConsentEntityAssoc(AddConsentEntityAssocRequestWrapper request) {
        logger.info("AddConsentEntityAssoc Service Called");

        List<String> errMessages = new ArrayList<>();
        Date currentDate = Calendar.getInstance().getTime();
        long consentId = request.getConsent().getConsentId();

        logger.debug("Find Consent where ConsentId = {}", consentId);
        Optional<Consent> consentData = consentRepository.findById(consentId);

        if (consentData.isPresent()) {
            logger.debug("ConsentId {} Found", consentId);
            List<Long> clauseCodeExists = new ArrayList<Long>();

            for(int i=0; i<consentData.get().getConsentEntityAssocs().size(); i++){
                clauseCodeExists.add(consentData.get().getConsentEntityAssocs().get(i).getClauseCode());
            }

            List<ConsentEntityAssoc> consentEntityAssocs = new ArrayList<ConsentEntityAssoc>();

            if (!request.getConsent().getConsentEntityAssocs().toString().isEmpty()) {
                for (int i = 0; i < request.getConsent().getConsentEntityAssocs().size(); i++) {
                    long clauseCode = request.getConsent().getConsentEntityAssocs().get(i).getClauseCode();
                    if (clauseCodeExists.contains(clauseCode)){
                        errMessages.add("ConsentId " + consentId + " already have ClauseCode " + clauseCode);
                        return errMessages;
                    }
                    Optional<Clause> clause = clauseRepository.findById(clauseCode);
                    if (clause.isPresent()) {
                        logger.debug("ClauseCode {} Found", clauseCode);
                        Calendar renewed = Calendar.getInstance();
                        renewed.add(Calendar.YEAR, clause.get().getClauseRenewalPeriod());
                        Date renewalDate = renewed.getTime();

                        ConsentEntityAssoc consentEntityAssoc = new ConsentEntityAssoc();
                        consentEntityAssoc.setClauseCode(request.getConsent().getConsentEntityAssocs().get(i).getClauseCode());
                        consentEntityAssoc.setCreateDate(currentDate);
                        consentEntityAssoc.setEndDate(renewalDate);
                        consentEntityAssoc.setCreateUser(request.getConsent().getConsentGiverId());
                        consentEntityAssocs.add(consentEntityAssoc);
                    } else {
                        logger.debug("ClauseCode {} Not Found", clauseCode);
                        errMessages.add("ClauseCode " + clauseCode + " Not Found");
                    }
                }
                if (!errMessages.isEmpty()){
                    return errMessages;
                }
                consentData.get().getConsentEntityAssocs().addAll(consentEntityAssocs);
                logger.debug("Save this Consent = {}", consentData);
                consentRepository.save(consentData.get());
            }
            return null;
        } else {
            logger.debug("ConsentId {} Not Found", consentId);
            errMessages.add("ConsentId " + consentId + " Not Found");
            return errMessages;
        }
    }

    public boolean deleteConsentEntityAssoc(DeleteConsentEntityAssocRequestWrapper request) {
        logger.info("DeleteConsentEntityAssoc Service Called");
        long consentEntityAssocId = request.getConsentEntityAssoc().getConsentEntityAssocId();

        if (consentEntityAssocRepository.existsById(consentEntityAssocId)) {
            logger.debug("ClauseCode {} Found", consentEntityAssocId);
            logger.debug("Delete ConsentEntityAssocRepository where ConsentEntityAssocId = {}", consentEntityAssocId);
            consentEntityAssocRepository.deleteById(consentEntityAssocId);
            return true;
        } else {
            logger.debug("ClauseCode {} Not Found", consentEntityAssocId);
            return false;
        }
    }
}
