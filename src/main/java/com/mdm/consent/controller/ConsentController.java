package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.repository.*;
import com.mdm.consent.service.ClauseService;
import com.mdm.consent.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentController {

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

    @Autowired
    private ConsentService consentService;

    @PostMapping("/addConsent")
    public ResponseEntity<ResponseWrapper> addConsent(@Valid @RequestBody AddConsentRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try{
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

            errMessages = consentService.addConsent(request);
            System.out.println("TEST" + errMessages);

            // Validate ClauseCode
            if (errMessages != null){
                // Response Mapping for Data Not Found
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

    @PutMapping("/updateConsent")
    public ResponseEntity<ResponseWrapper> updateConsent(@Valid @RequestBody UpdateConsentRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try{
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

            errMessages = consentService.updateConsent(request);

            // Validate ClauseCode
            if (errMessages != null){
                // Response Mapping for Data Not Found
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

    @GetMapping("/getConsent")
    public ResponseEntity<ConsentResponse> getConsent(@Valid @RequestBody GetConsentRequestWrapper request, Errors errors) {

        ConsentResponse consentResponse = new ConsentResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                consentResponse.setErrors(errMessages);
                consentResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.BAD_REQUEST);
            }

            // Check if ConsentId exists in CONSENT table
            Consent consent = consentService.getConsent(request);

            if (consent != null) {
                // Response Mapping
                consentResponse.setConsent(consent);
                consentResponse.setResponseMessage(HttpStatus.OK.name());
                consentResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + request.getConsent().getConsentId() + " Not Found");
                consentResponse.setErrors(errMessages);
                consentResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                consentResponse.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            consentResponse.setErrors(errMessages);
            consentResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            consentResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsentByCifOrIdNumber")
    public ResponseEntity<ConsentListResponse> getConsentByCifOrIdNumber(@Valid @RequestBody GetConsentByCifOrIdNumberRequestWrapper request, Errors errors) {

        ConsentListResponse consentListResponse = new ConsentListResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }

            // Check If Cif or IdNumber is Null
            if (request.getConsent().getCifId() == null || (request.getConsent().getIdType() == null && request.getConsent().getIdNumber() == null)){
                // Response Mapping for Bad Request
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }

            String cif = request.getConsent().getCifId();
            String idType = request.getConsent().getIdType();
            String idNumber = request.getConsent().getIdNumber();

            if (!cif.isBlank() || (!idType.isBlank() && !idNumber.isBlank())) {
                List<Consent> consents = consentService.getConsentByCifOrIdNumber(request);

                if (consents != null) {
                    // Response Mapping
                    consentListResponse.setConsents(consents);
                    consentListResponse.setResponseMessage(HttpStatus.OK.name());
                    consentListResponse.setStatus(HttpStatus.OK.value());
                    return new ResponseEntity<>(consentListResponse, HttpStatus.OK);
                } else {
                    // Response Mapping for Data Not Found
                    errMessages.add("Data Not Found");
                    consentListResponse.setErrors(errMessages);
                    consentListResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                    consentListResponse.setStatus(HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<>(consentListResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                // Response Mapping for Bad Request
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Response Mapping
            errMessages.add(e.getMessage());
            consentListResponse.setErrors(errMessages);
            consentListResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            consentListResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(consentListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsent")
    public ResponseEntity<ResponseWrapper> deleteConsent(@Valid @RequestBody GetConsentRequestWrapper request, Errors errors) {

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

            // Get consent_id
            long consentId = request.getConsent().getConsentId();

            // Check if ConsentId exists in CONSENT table
            boolean isDeleted = consentService.deleteConsent(request);

            if (!isDeleted) {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + consentId + " Not Found");
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

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
