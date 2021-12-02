package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.service.ClauseService;
import com.mdm.consent.service.ConsentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ConsentController.class);

    @Autowired
    private ConsentService consentService;

    @PostMapping("/addConsent")
    public ResponseEntity<ResponseWrapper> addConsent(@Valid @RequestBody AddConsentRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try{
            logger.info("Validate Request Body");
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }

                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            logger.info("Call AddConsent Service");
            errMessages = consentService.addConsent(request);
            logger.debug("errMessages = {}", errMessages);

            if (errMessages != null){
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.NOT_FOUND.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            logger.debug("Response Mapping for {} = {}", HttpStatus.CREATED.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            assert errMessages != null;
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateConsent")
    public ResponseEntity<ResponseWrapper> updateConsent(@Valid @RequestBody UpdateConsentRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try{
            logger.info("Validate Request Body");
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }

                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            logger.info("Call UpdateConsent Service");
            errMessages = consentService.updateConsent(request);
            logger.debug("errMessages = {}", errMessages);

            if (errMessages != null){
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.NOT_FOUND.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            logger.debug("Response Mapping for {} = {}", HttpStatus.CREATED.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            assert errMessages != null;
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsent")
    public ResponseEntity<ConsentResponse> getConsent(@Valid @RequestBody GetConsentRequestWrapper request, Errors errors) {

        ConsentResponse consentResponse = new ConsentResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            logger.info("Validate Request Body");
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }

                consentResponse.setErrors(errMessages);
                consentResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), consentResponse);
                return new ResponseEntity<>(consentResponse, HttpStatus.BAD_REQUEST);
            }

            logger.info("Call GetConsent Service");
            Consent consent = consentService.getConsent(request);
            logger.debug("consent = {}", consent);

            if (consent != null) {
                consentResponse.setConsent(consent);
                consentResponse.setResponseMessage(HttpStatus.OK.name());
                consentResponse.setStatus(HttpStatus.OK.value());
                logger.debug("Response Mapping for {} = {}", HttpStatus.OK.value(), consentResponse);
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                errMessages.add("ConsentId " + request.getConsent().getConsentId() + " Not Found");
                consentResponse.setErrors(errMessages);
                consentResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                consentResponse.setStatus(HttpStatus.NOT_FOUND.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.NOT_FOUND.value(), consentResponse);
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            errMessages.add(e.getMessage());
            consentResponse.setErrors(errMessages);
            consentResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            consentResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), consentResponse);
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsentByCifOrIdNumber")
    public ResponseEntity<ConsentListResponse> getConsentByCifOrIdNumber(@Valid @RequestBody GetConsentByCifOrIdNumberRequestWrapper request, Errors errors) {

        ConsentListResponse consentListResponse = new ConsentListResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            logger.info("Validate Request Body");
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }

                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), consentListResponse);
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }

            if (request.getConsent().getCifId() == null || (request.getConsent().getIdType() == null && request.getConsent().getIdNumber() == null)){
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), consentListResponse);
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }

            String cif = request.getConsent().getCifId();
            String idType = request.getConsent().getIdType();
            String idNumber = request.getConsent().getIdNumber();

            if (!cif.isBlank() || (!idType.isBlank() && !idNumber.isBlank())) {

                logger.info("Call GetConsentByCifOrIdNumber Service");
                List<Consent> consents = consentService.getConsentByCifOrIdNumber(request);
                logger.debug("consents = {}", consents);

                if (consents != null) {
                    consentListResponse.setConsents(consents);
                    consentListResponse.setResponseMessage(HttpStatus.OK.name());
                    consentListResponse.setStatus(HttpStatus.OK.value());
                    logger.debug("Response Mapping for {} = {}", HttpStatus.OK.value(), consentListResponse);
                    return new ResponseEntity<>(consentListResponse, HttpStatus.OK);
                } else {
                    errMessages.add("Data Not Found");
                    consentListResponse.setErrors(errMessages);
                    consentListResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                    consentListResponse.setStatus(HttpStatus.NOT_FOUND.value());
                    logger.error("Error Response Mapping for {} = {}", HttpStatus.NOT_FOUND.value(), consentListResponse);
                    return new ResponseEntity<>(consentListResponse, HttpStatus.NOT_FOUND);
                }
            } else {
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrors(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), consentListResponse);
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            errMessages.add(e.getMessage());
            consentListResponse.setErrors(errMessages);
            consentListResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            consentListResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), consentListResponse);
            return new ResponseEntity<>(consentListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsent")
    public ResponseEntity<ResponseWrapper> deleteConsent(@Valid @RequestBody GetConsentRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try {
            logger.info("Validate Request Body");
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }

                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            logger.info("Call DeleteConsent Service");
            boolean isDeleted = consentService.deleteConsent(request);
            logger.debug("isDeleted = {}", isDeleted);

            if (!isDeleted) {
                errMessages.add("ConsentId " + request.getConsent().getConsentId() + " Not Found");
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                logger.error("Error Response Mapping for {} = {}", HttpStatus.NOT_FOUND.value(), responseWrapper);
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            responseWrapper.setResponseMessage(HttpStatus.OK.name());
            responseWrapper.setStatus(HttpStatus.OK.value());
            logger.debug("Response Mapping for {} = {}", HttpStatus.OK.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } catch (Exception e) {
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
