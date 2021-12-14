package com.mdm.consent.service;

import com.mdm.consent.dto.addconsent.AddConsentRequestWrapper;
import com.mdm.consent.dto.getconsent.GetConsentRequestWrapper;
import com.mdm.consent.dto.getconsentbyciforidnumber.GetConsentByCifOrIdNumberRequestWrapper;
import com.mdm.consent.dto.updateconsent.UpdateConsentRequestWrapper;
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
public class ConsentService {

    private static final Logger logger = LoggerFactory.getLogger(ClauseService.class);

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    public List<String> addConsent(AddConsentRequestWrapper request) {
        logger.info("AddConsent Service Called");

        List<String> errMessages = new ArrayList<>();

        try {
            Date currentDate = Calendar.getInstance().getTime();

            Consent consent = new Consent();
            consent.setCifId(request.getConsent().getCifId());
            consent.setIdType(request.getConsent().getIdType());
            consent.setIdNumber(request.getConsent().getIdNumber());
            consent.setTenantType(request.getConsent().getTenantType());
            consent.setProcPurpId(request.getConsent().getProcPurpId());
            consent.setAgreeInd(request.getConsent().getAgreeInd());
            consent.setConsentGiverId(request.getConsent().getConsentGiverId());
            consent.setBranchCode(request.getConsent().getBranchCode());
            consent.setCreateDate(currentDate);
            consent.setStartDate(currentDate);
            consent.setLastUpdateDate(currentDate);
            consent.setEndDate(null);
            consent.setLastUpdateUser(request.getConsent().getConsentGiverId());

            List<ConsentEntityAssoc> consentEntityAssocs = new ArrayList<ConsentEntityAssoc>();

            for (int i=0; i<request.getConsent().getConsentEntityAssocs().size(); i++){
                long clauseCode = request.getConsent().getConsentEntityAssocs().get(i).getClauseCode();
                Optional<Clause> clause = clauseRepository.findById(clauseCode);
                if (clause.isPresent()){
                    logger.debug("ClauseCode {} Found", clauseCode);
                    ConsentEntityAssoc consentEntityAssoc = new ConsentEntityAssoc();
                    if (clause.get().getClauseRenewalPeriod() == 0) {
                        consentEntityAssoc.setEndDate(null);
                    } else {
                        Calendar renewed = Calendar.getInstance();
                        renewed.add(Calendar.YEAR, clause.get().getClauseRenewalPeriod());
                        Date renewalDate = renewed.getTime();
                        consentEntityAssoc.setEndDate(renewalDate);
                    }
                    consentEntityAssoc.setClauseCode(request.getConsent().getConsentEntityAssocs().get(i).getClauseCode());
                    consentEntityAssoc.setCreateDate(currentDate);

                    consentEntityAssoc.setCreateUser(request.getConsent().getConsentGiverId());
                    consentEntityAssocs.add(consentEntityAssoc);
                } else {
                    logger.debug("ClauseCode {} Not Found", clauseCode);
                    errMessages.add("ClauseCode " + clauseCode + " Not Found");
                }
            }

            if (!errMessages.isEmpty()){
                return errMessages;
            } else {
                consent.setConsentEntityAssocs(consentEntityAssocs);
                logger.debug("Save this Consent = {}", consent);
                consentRepository.save(consent);
                return null;
            }
        } catch (Exception e) {
            logger.debug("Error = {}", e.getMessage());
            errMessages.add(e.getMessage());
            return errMessages;
        }
    }

    public List<String> updateConsent(UpdateConsentRequestWrapper request) {
        logger.info("UpdateConsent Service Called");

        List<String> errMessages = new ArrayList<>();
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> existingConsent = consentRepository.findById(consentId);

        if (existingConsent.isPresent()) {
            logger.debug("ConsentId {} Found", consentId);

            Date currentDate = Calendar.getInstance().getTime();
            existingConsent.get().setCifId(request.getConsent().getCifId());
            existingConsent.get().setIdType(request.getConsent().getIdType());
            existingConsent.get().setIdNumber(request.getConsent().getIdNumber());
            existingConsent.get().setTenantType(request.getConsent().getTenantType());
            existingConsent.get().setProcPurpId(request.getConsent().getProcPurpId());
            existingConsent.get().setAgreeInd(request.getConsent().getAgreeInd());
            existingConsent.get().setBranchCode(request.getConsent().getBranchCode());
            existingConsent.get().setEndReasonType(request.getConsent().getEndReasonType());
            existingConsent.get().setLastUpdateUser(request.getConsent().getConsentGiverId());
            existingConsent.get().setLastUpdateDate(currentDate);

            logger.debug("Update this Consent = {}", existingConsent);
            consentRepository.save(existingConsent.get());
            return null;
        } else {
            logger.debug("ConsentId {} Not Found", consentId);
            errMessages.add("ConsentId " + consentId + " Not Found");
            return errMessages;
        }
    }

    public Consent getConsent(GetConsentRequestWrapper request) {
        logger.info("GetConsent Service Called");

        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);

        if (consentData.isPresent()){
            logger.debug("ConsentId {} Found", consentId);
            Consent consent = consentData.get();

            for(int i=0; i<consent.getConsentEntityAssocs().size(); i++){
                long clauseCode = consent.getConsentEntityAssocs().get(i).getClauseCode();
                Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                if (clauseData.isPresent()){
                    logger.debug("ClauseCode {} Found", clauseCode);
                    consent.getConsentEntityAssocs().get(i).setClauseName(clauseData.get().getClauseName());
                } else {
                    logger.debug("ClauseCode {} Not Found", clauseCode);
                    consent.getConsentEntityAssocs().get(i).setClauseName("UNDEFINED");
                }
            }
            return consent;
        } else {
            logger.debug("ConsentId {} Not Found", consentId);
            return null;
        }
    }

    public List<Consent> getConsentByCifOrIdNumber(GetConsentByCifOrIdNumberRequestWrapper request) {
        logger.info("GetConsentByCifOrIdNumber Service Called");
        String cif = request.getConsent().getCifId();
        String idType = request.getConsent().getIdType();
        String idNumber = request.getConsent().getIdNumber();

        List<Consent> consents = consentRepository.findByCifId(cif);
        List<Consent> consentsByIdNumber = consentRepository.findByIdTypeAndIdNumber(idType, idNumber);

        consentsByIdNumber.removeAll(consents);
        consents.addAll(consentsByIdNumber);

        if (consents.isEmpty()) {
            logger.debug("(CIF = {}) or (IdType = {} and IdNumber = {}) Not Found", cif, idType, idNumber);
            return null;
        } else {
            logger.debug("(CIF = {}) or (IdType = {} and IdNumber = {}) Found", cif, idType, idNumber);
            for (Consent consent : consents) {
                for (int j = 0; j < consent.getConsentEntityAssocs().size(); j++) {
                    long clauseCode = consent.getConsentEntityAssocs().get(j).getClauseCode();
                    Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                    if (clauseData.isPresent()) {
                        logger.debug("ClauseCode {} Found", clauseCode);
                        consent.getConsentEntityAssocs().get(j).setClauseName(clauseData.get().getClauseName());
                    } else {
                        logger.debug("ClauseCode {} Not Found", clauseCode);
                        consent.getConsentEntityAssocs().get(j).setClauseName("UNDEFINED");
                    }
                }
            }
            return consents;
        }
    }

    public boolean deleteConsent(GetConsentRequestWrapper request) {
        logger.info("DeleteConsent Service Called");
        long consentId = request.getConsent().getConsentId();
        if (consentRepository.existsById(consentId)) {
            logger.debug("ConsentId {} Found", consentId);
            logger.debug("Delete Consent where ConsentId = {}", consentId);
            consentRepository.deleteById(consentId);
            return true;
        } else {
            logger.debug("ConsentId {} Not Found", consentId);
            return false;
        }
    }
}
