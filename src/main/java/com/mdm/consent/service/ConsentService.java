package com.mdm.consent.service;

import com.mdm.consent.dto.*;
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
public class ConsentService {
    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    public List<String> addConsent(AddConsentRequestWrapper request) {

        List<String> errMessages = new ArrayList<>();

        try {
            // Get Current Date
            Calendar now = Calendar.getInstance();
            Date currentDate = now.getTime();

            // Declare Consent
            Consent consent = new Consent();

            // Set Values
            consent.setCifId(request.getConsent().getCifId());
            consent.setIdType(request.getConsent().getIdType());
            consent.setIdNumber(request.getConsent().getIdNumber());
            consent.setTenantType(request.getConsent().getTenantType());
            consent.setProcPurpId(request.getConsent().getProcPurpId());
            consent.setAgreeInd(request.getConsent().getAgreeInd());
            consent.setConsentGiverId(request.getConsent().getConsentGiverId());
            consent.setBranchCode(request.getConsent().getBranchCode());

            // Set CreateDate, StartDate, and LastUpdateDate to Current Date
            consent.setCreateDate(currentDate);
            consent.setStartDate(currentDate);
            consent.setLastUpdateDate(currentDate);
            consent.setEndDate(null);

            // Set LastUpdateUser with the same value as CreateUser only when Add New Consent (First Time)
            consent.setLastUpdateUser(request.getConsent().getConsentGiverId());

            List<ConsentEntityAssoc> consentEntityAssocs = new ArrayList<ConsentEntityAssoc>();
            // Set ConsentEntityAssoc
            for (int i=0; i<request.getConsent().getConsentEntityAssocs().size(); i++){
                // Check if ClauseCode exists in CLAUSE table
                long clauseCode = request.getConsent().getConsentEntityAssocs().get(i).getClauseCode();
                Optional<Clause> clause = clauseRepository.findById(clauseCode);
                if (clause.isPresent()){
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

            if (!errMessages.isEmpty()){
                return errMessages;
            }

            // Add List of ConsentEntityAssoc to Consent
            consent.setConsentEntityAssocs(consentEntityAssocs);
            // Save Consent
            consentRepository.save(consent);
            return null;
        } catch (Exception e) {
            errMessages.add(e.getMessage());
            return errMessages;
        }
    }

    public List<String> updateConsent(UpdateConsentRequestWrapper request) {

        List<String> errMessages = new ArrayList<>();

        // Check if ConsentId exists in CONSENT table
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> existingConsent = consentRepository.findById(consentId);

        if (existingConsent.isPresent()) {
            // Get Current Date
            Calendar now = Calendar.getInstance();
            Date currentDate = now.getTime();

            // Set New Values
            existingConsent.get().setCifId(request.getConsent().getCifId());
            existingConsent.get().setIdType(request.getConsent().getIdType());
            existingConsent.get().setIdNumber(request.getConsent().getIdNumber());
            existingConsent.get().setTenantType(request.getConsent().getTenantType());
            existingConsent.get().setProcPurpId(request.getConsent().getProcPurpId());
            existingConsent.get().setAgreeInd(request.getConsent().getAgreeInd());
            existingConsent.get().setBranchCode(request.getConsent().getBranchCode());
            existingConsent.get().setEndReasonType(request.getConsent().getEndReasonType());
            existingConsent.get().setLastUpdateUser(request.getConsent().getConsentGiverId());

            // Set LastUpdateDate to Current Date
            existingConsent.get().setLastUpdateDate(currentDate);

            // Save Consent
            consentRepository.save(existingConsent.get());
            return null;
        } else {
            // Response Mapping for Data Not Found
            errMessages.add("ConsentId " + consentId + " Not Found");
            return errMessages;
        }
    }

    public Consent getConsent(GetConsentRequestWrapper request) {
        // Check if ConsentId exists in CONSENT table
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);

        if (consentData.isPresent()){
            Consent consent = consentData.get();

            // Get ClauseName for every ConsentEntityAssoc
            for(int i=0; i<consent.getConsentEntityAssocs().size(); i++){
                // Check if ClauseCode exists in CLAUSE table
                long clauseCode = consent.getConsentEntityAssocs().get(i).getClauseCode();
                Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                if (clauseData.isPresent()){
                    consent.getConsentEntityAssocs().get(i).setClauseName(clauseData.get().getClauseName());
                } else {
                    consent.getConsentEntityAssocs().get(i).setClauseName("UNDEFINED");
                }
            }
            return consent;
        } else {
            return null;
        }
    }

    public List<Consent> getConsentByCifOrIdNumber(GetConsentByCifOrIdNumberRequestWrapper request) {
        String cif = request.getConsent().getCifId();
        String idType = request.getConsent().getIdType();
        String idNumber = request.getConsent().getIdNumber();

        // Get Consent by Cif or IdNumber
        List<Consent> consents = consentRepository.findByCifId(cif);
        List<Consent> consentsByIdNumber = consentRepository.findByIdTypeAndIdNumber(idType, idNumber);

        // Remove Duplicates
        consentsByIdNumber.removeAll(consents);
        consents.addAll(consentsByIdNumber);

        // Check if empty
        if (consents.isEmpty()) {

            return null;
        } else {
            // Get ClauseName for every ConsentEntityAssoc
            for (Consent consent : consents) {
                for (int j = 0; j < consent.getConsentEntityAssocs().size(); j++) {
                    // Check if ClauseCode exists in CLAUSE table
                    long clauseCode = consent.getConsentEntityAssocs().get(j).getClauseCode();
                    Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                    if (clauseData.isPresent()) {
                        consent.getConsentEntityAssocs().get(j).setClauseName(clauseData.get().getClauseName());
                    } else {
                        consent.getConsentEntityAssocs().get(j).setClauseName("UNDEFINED");
                    }
                }
            }
            return consents;
        }
    }

    public boolean deleteConsent(GetConsentRequestWrapper request) {
        // Get consent_id
        long consentId = request.getConsent().getConsentId();

        // Delete if exists
        if (consentRepository.existsById(consentId)) {
            // Delete consent by ConsentId
            consentRepository.deleteById(consentId);
            return true;
        } else return false;
    }
}
