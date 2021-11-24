package com.mdm.consent.controller;

import com.mdm.consent.dto.ConsentAssocRequest;
import com.mdm.consent.dto.ConsentRequest;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentAssoc;
import com.mdm.consent.repository.ConsentAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentController {
    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentAssocRepository consentAssocRepository;

    @PostMapping("/addConsent")
    public ResponseEntity<Consent> addConsent(@RequestBody ConsentRequest request) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            System.out.println();
            Date date = calendar.getTime();

            request.getConsent().setCreatedDate(calendar.getTime());
            request.getConsent().setLastUpdateDate(calendar.getTime());

            request.getConsent().setLastUpdateUser(request.getConsent().getCreatedUser());
            for(int i = 0; i<request.getConsent().getConsentAssocs().size(); i++) {
                request.getConsent().getConsentAssocs().get(i).setCreatedDate(calendar.getTime());
                request.getConsent().getConsentAssocs().get(i).setLastUpdateDate(calendar.getTime());
                request.getConsent().getConsentAssocs().get(i).setCreatedUser(request.getConsent().getCreatedUser());
                request.getConsent().getConsentAssocs().get(i).setLastUpdateUser(request.getConsent().getCreatedUser());
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

        Date date = Calendar.getInstance().getTime();

        if (consentData.isPresent()) {
            Consent _consent = consentData.get();
            _consent.setCifId(request.getConsent().getCifId());
            _consent.setIdType(request.getConsent().getIdType());
            _consent.setIdNumber(request.getConsent().getIdNumber());
            _consent.setClauseRenewalPeriod(request.getConsent().getClauseRenewalPeriod());
            _consent.setSourceSystem(request.getConsent().getSourceSystem());
            //_consent.setCreateDate(request.getConsent().getCreateDate());
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
    public ResponseEntity<List<ConsentAssoc>> addConsentAssoc(@RequestBody ConsentRequest request) {
        try {
            List<ConsentAssoc> _consentAssoc = consentAssocRepository.saveAll(request.getConsent().getConsentAssocs());
            return new ResponseEntity<>(_consentAssoc, HttpStatus.CREATED);
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
}
