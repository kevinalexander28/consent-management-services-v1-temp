package com.mdm.consent.controller;

import com.mdm.consent.dto.ConsentRequest;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.repository.ConsentAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentController {
    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentAssocRepository consentAssocRepository;

    @PostMapping("/addConsent")
    public Consent addConsent(@RequestBody ConsentRequest request){
        return consentRepository.save(request.getConsent());
    }

    @GetMapping("/getConsent")
    public ResponseEntity<Consent> getConsent(@RequestBody ConsentRequest request){
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);

        return consentData.map(consent -> new ResponseEntity<>(consent, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAllConsentByCIFId")
    public ResponseEntity<List<Consent>> getAllConsentByCifId(@RequestBody ConsentRequest request){
        try {
            String cifId = request.getConsent().getCifId();
            List<Consent> consents = consentRepository.findByCifId(cifId);
            if (consents.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

        if (consentData.isPresent()) {
            Consent _consent = consentData.get();
            _consent.setCifId(request.getConsent().getCifId());
            _consent.setIdType(request.getConsent().getIdType());
            _consent.setIdNumber(request.getConsent().getIdNumber());
            _consent.setClauseRenewalPeriod(request.getConsent().getClauseRenewalPeriod());
            _consent.setSourceSystem(request.getConsent().getSourceSystem());
            _consent.setLastUpdateUser(request.getConsent().getLastUpdateUser());
            _consent.setLastUpdateDt(request.getConsent().getLastUpdateDt());
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
}
