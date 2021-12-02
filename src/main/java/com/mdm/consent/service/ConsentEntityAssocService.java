package com.mdm.consent.service;

import com.mdm.consent.dto.AddConsentEntityAssocRequestWrapper;
import com.mdm.consent.dto.DeleteConsentEntityAssocRequestWrapper;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentEntityAssoc;
import com.mdm.consent.repository.ClauseRepository;
import com.mdm.consent.repository.ConsentEntityAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsentEntityAssocService {
    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    public List<String> addConsentEntityAssoc(AddConsentEntityAssocRequestWrapper request) {

        List<String> errMessages = new ArrayList<>();

        // Get Current Date
        Calendar now = Calendar.getInstance();
        Date currentDate = now.getTime();

        // Check if ConsentId exists in CONSENT table
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);

        if (consentData.isPresent()) {

            // Get All Existing ClauseCode
            List<Long> clauseCodeExists = new ArrayList<Long>();
            for(int i=0; i<consentData.get().getConsentEntityAssocs().size(); i++){
                clauseCodeExists.add(consentData.get().getConsentEntityAssocs().get(i).getClauseCode());
            }

            List<ConsentEntityAssoc> consentEntityAssocs = new ArrayList<ConsentEntityAssoc>();
            // Set ConsentEntityAssoc
            if (!request.getConsent().getConsentEntityAssocs().toString().isEmpty()) {
                for (int i = 0; i < request.getConsent().getConsentEntityAssocs().size(); i++) {
                    // Check if ClauseCode exists in CLAUSE table
                    long clauseCode = request.getConsent().getConsentEntityAssocs().get(i).getClauseCode();

                    if (clauseCodeExists.contains(clauseCode)){
                        // Response Mapping for Data Not Found
                        errMessages.add("ConsentId " + consentId + " already have ClauseCode " + clauseCode);
                        return errMessages;
                    }
                    Optional<Clause> clause = clauseRepository.findById(clauseCode);
                    if (clause.isPresent()) {
                        // Set EndDate to 5 years from Start Date
                        Calendar renewed = Calendar.getInstance();
                        renewed.add(Calendar.YEAR, clause.get().getClauseRenewalPeriod());
                        Date renewalDate = renewed.getTime();

                        ConsentEntityAssoc consentEntityAssoc = new ConsentEntityAssoc();
                        // Set ClauseCode
                        consentEntityAssoc.setClauseCode(request.getConsent().getConsentEntityAssocs().get(i).getClauseCode());

                        // Set CreateDate to Current Date
                        consentEntityAssoc.setCreateDate(currentDate);

                        // Set EndDate to Renewal Date
                        consentEntityAssoc.setEndDate(renewalDate);

                        // Set CreateUser with the same value as ConsentGiverId
                        consentEntityAssoc.setCreateUser(request.getConsent().getConsentGiverId());
                        // Add ConsentEntityAssoc to List of ConsentEntityAssoc
                        consentEntityAssocs.add(consentEntityAssoc);
                    } else {
                        // Set ErrorMessages
                        errMessages.add("ClauseCode " + clauseCode + " Not Found");
                    }
                }

                // Validate ClauseCode
                if (!errMessages.isEmpty()){
                    return errMessages;
                }

                // Add new ConsentEntityAssocs to existing consent
                consentData.get().getConsentEntityAssocs().addAll(consentEntityAssocs);
                consentRepository.save(consentData.get());
            }
            return null;
        } else {
            // Response Mapping for Data Not Found
            errMessages.add("ConsentId " + consentId + " Not Found");
            return errMessages;
        }
    }

    public boolean deleteConsentEntityAssoc(DeleteConsentEntityAssocRequestWrapper request) {
        // Check if consentEntityAssocId exists
        long consentEntityAssocId = request.getConsentEntityAssoc().getConsentEntityAssocId();

        if (consentEntityAssocRepository.existsById(consentEntityAssocId)) {
            // Delete clause by ClauseCode
            consentEntityAssocRepository.deleteById(consentEntityAssocId);
            return true;
        } else return false;
    }
}
