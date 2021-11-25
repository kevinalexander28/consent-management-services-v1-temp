package com.mdm.consent.controller;

import com.mdm.consent.dto.CdConsentTpRequest;
import com.mdm.consent.dto.ConsentAssocRequest;
import com.mdm.consent.dto.ConsentRequest;
import com.mdm.consent.entity.CdConsentTp;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentAssoc;
import com.mdm.consent.repository.CdConsentTpRepository;
import com.mdm.consent.repository.ConsentAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentController {
    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentAssocRepository consentAssocRepository;

    @Autowired
    private CdConsentTpRepository cdConsentTpRepository;

    @PostMapping("/addConsent")
    public ResponseEntity<Consent> addConsent(@RequestBody ConsentRequest request) {
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            Date date = calendar.getTime();

            request.getConsent().setCreatedDate(date);
            request.getConsent().setLastUpdateDate(date);

            request.getConsent().setLastUpdateUser(request.getConsent().getCreatedUser());

            for(int i = 0; i<request.getConsent().getConsentAssocs().size(); i++) {
                long checkClauseCode = request.getConsent().getConsentAssocs().get(i).getClauseCode();
                Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(checkClauseCode);
                if (cdConsentTpData.isPresent()) {
                    request.getConsent().getConsentAssocs().get(i).setClauseName(cdConsentTpData.get().getClauseName());
                    request.getConsent().getConsentAssocs().get(i).setCreatedDate(date);
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateDate(date);
                    request.getConsent().getConsentAssocs().get(i).setCreatedUser(request.getConsent().getCreatedUser());
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateUser(request.getConsent().getCreatedUser());
                } else {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            Consent _consent = consentRepository
                    .save(request.getConsent());
            return new ResponseEntity<>(_consent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsent")
    public ResponseEntity<Consent> getConsent(@RequestBody ConsentRequest request){

        try {
            long consentId = request.getConsent().getConsentId();
            Optional<Consent> consentData = consentRepository.findById(consentId);

            if (consentData.isPresent()){
                Consent consent = consentData.get();
                for (int j = 0; j < consent.getConsentAssocs().size(); j++) {
                    ConsentAssoc consentAssoc = consent.getConsentAssocs().get(j);
                    long clauseCode = consentAssoc.getClauseCode();
                    Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                    if (cdConsentTpData.isPresent()) {
                        String clauseName = cdConsentTpData.get().getClauseName();
                        consent.getConsentAssocs().get(j).setClauseName(clauseName);
                    }
                }
                return new ResponseEntity<>(consent, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllConsentByCIFId")
    public ResponseEntity<List<Consent>> getAllConsentByCifId(@RequestBody ConsentRequest request){
        try {
            String cifId = request.getConsent().getCifId();
            List<Consent> consents = consentRepository.findByCifId(cifId);
            if (consents.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            for (Consent consent : consents) {
                for (int j = 0; j < consent.getConsentAssocs().size(); j++) {
                    ConsentAssoc consentAssoc = consent.getConsentAssocs().get(j);
                    long clauseCode = consentAssoc.getClauseCode();
                    Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                    if (cdConsentTpData.isPresent()) {
                        String clauseName = cdConsentTpData.get().getClauseName();
                        consent.getConsentAssocs().get(j).setClauseName(clauseName);
                    }
                }
            }
            return new ResponseEntity<>(consents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateConsent")
    public ResponseEntity<Consent> updateConsent(@RequestBody ConsentRequest request) {

        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);

        Date date = Calendar.getInstance().getTime();

        if (consentData.isPresent()) {
            Consent _consent = consentData.get();
            _consent.setCifId(request.getConsent().getCifId());
            _consent.setIdType(request.getConsent().getIdType());
            _consent.setIdNumber(request.getConsent().getIdNumber());
            _consent.setClauseRenewalPeriod(request.getConsent().getClauseRenewalPeriod());
            _consent.setSourceSystem(request.getConsent().getSourceSystem());
            //_consent.setCreatedDate(request.getConsent().getCreateDate());
            //_consent.setCreatedUser(request.getConsent().getCreatedUser());
            _consent.setLastUpdateUser(request.getConsent().getLastUpdateUser());
            _consent.setLastUpdateDate(date);
            _consent.setBranchCode(request.getConsent().getBranchCode());
            return new ResponseEntity<>(consentRepository.save(_consent), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteConsent")
    public ResponseEntity<HttpStatus> deleteConsent(@RequestBody ConsentRequest request) {
        try {
            long consentId = request.getConsent().getConsentId();
            consentRepository.deleteById(consentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addConsentAssoc")
    public ResponseEntity<Consent> addConsentAssoc(@RequestBody ConsentRequest request) {
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            Date date = calendar.getTime();

            long consentId = request.getConsent().getConsentId();
            Optional<Consent> consentData = consentRepository.findById(consentId);

            for(int i = 0; i<request.getConsent().getConsentAssocs().size(); i++) {
                ConsentAssoc consentAssoc = request.getConsent().getConsentAssocs().get(i);
                long clauseCode = consentAssoc.getClauseCode();
                Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                if (cdConsentTpData.isPresent()) {
                    request.getConsent().getConsentAssocs().get(i).setCreatedDate(date);
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateDate(date);
                    request.getConsent().getConsentAssocs().get(i).setCreatedUser(request.getConsent().getCreatedUser());
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateUser(request.getConsent().getCreatedUser());
                } else {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            if (consentData.isPresent()){
                request.getConsent().setCifId(consentData.get().getCifId());
                request.getConsent().setIdType(consentData.get().getIdType());
                request.getConsent().setIdNumber(consentData.get().getIdNumber());
                request.getConsent().setClauseRenewalPeriod(consentData.get().getClauseRenewalPeriod());
                request.getConsent().setSourceSystem(consentData.get().getSourceSystem());
                request.getConsent().setCreatedDate(consentData.get().getCreatedDate());
                request.getConsent().setCreatedUser(consentData.get().getCreatedUser());
                request.getConsent().setLastUpdateUser(consentData.get().getLastUpdateUser());
                request.getConsent().setLastUpdateDate(consentData.get().getLastUpdateDate());
                request.getConsent().setBranchCode(consentData.get().getBranchCode());

                for(int i = 0; i<consentData.get().getConsentAssocs().size(); i++) {
                    request.getConsent().getConsentAssocs().add(consentData.get().getConsentAssocs().get(i));
                }

                Consent _consent = consentRepository.save(request.getConsent());
                return new ResponseEntity<>(_consent, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsentAssoc")
    public ResponseEntity<HttpStatus> deleteConsentAssoc(@RequestBody ConsentAssocRequest request) {
        try {
            long consentAssocId = request.getConsentAssoc().getConsentAssocId();
            consentAssocRepository.deleteById(consentAssocId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListConsent")
    public ResponseEntity<List<CdConsentTp>> getListConsent() {
        try {
            List<CdConsentTp> cdConsentTps = cdConsentTpRepository.findAllNotZero();
            return new ResponseEntity<>(cdConsentTps, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addListConsent")
    public ResponseEntity<CdConsentTp> addListConsent(@RequestBody CdConsentTpRequest request) {
        try {
            CdConsentTp _cdConsentTp = cdConsentTpRepository.save(request.getCdConsentTp());
            return new ResponseEntity<>(_cdConsentTp, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
