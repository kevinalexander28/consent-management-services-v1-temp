package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentEntityAssocController {

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    @PostMapping("/addConsentEntityAssoc")
    public ResponseEntity<ResponseWrapper> addConsentEntityAssoc(@Valid @RequestBody AddConsentEntityAssocRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try {
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

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
                            responseWrapper.setErrors(errMessages);
                            responseWrapper.setResponseMessage(HttpStatus.NOT_ACCEPTABLE.name());
                            responseWrapper.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                            return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_ACCEPTABLE);
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
                        // Response Mapping for Data Not Found
                        responseWrapper.setErrors(errMessages);
                        responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                        responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                        return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
                    }

                    // Add new ConsentEntityAssocs to existing consent
                    consentData.get().getConsentEntityAssocs().addAll(consentEntityAssocs);
                    consentRepository.save(consentData.get());
                }
            } else {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + consentId + " Not Found");
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }
            // Response Mapping
            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsentEntityAssoc")
    public ResponseEntity<ResponseWrapper> deleteConsentEntityAssoc(@Valid @RequestBody DeleteConsentEntityAssocRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try {
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Check if consentEntityAssocId exists
            long consentEntityAssocId = request.getConsentEntityAssoc().getConsentEntityAssocId();
            if (!consentEntityAssocRepository.existsById(consentEntityAssocId)) {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentEntityAssocId " + consentEntityAssocId + " Not Found");
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            // Delete clause by ClauseCode
            consentEntityAssocRepository.deleteById(consentEntityAssocId);

            // Response Mapping
            responseWrapper.setResponseMessage(HttpStatus.OK.name());
            responseWrapper.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 }
