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
public class ConsentController {

    @Autowired
    private ConsentRepository consentRepository;

    @Autowired
    private ConsentEntityAssocRepository consentEntityAssocRepository;

    @Autowired
    private ClauseRepository clauseRepository;

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
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

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
            if (!request.getConsent().getConsentEntityAssocs().toString().isEmpty()) {
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
            }

            // Validate ClauseCode
            if (!errMessages.isEmpty()){
                // Response Mapping for Data Not Found
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            // Add List of ConsentEntityAssoc to Consent
            consent.setConsentEntityAssocs(consentEntityAssocs);
            // Save Consent
            consentRepository.save(consent);
            // Response Mapping
            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrros(errMessages);
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
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

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
            } else {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + consentId + " Not Found");
                responseWrapper.setErrros(errMessages);
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
            responseWrapper.setErrros(errMessages);
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
                consentResponse.setErrros(errMessages);
                consentResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.BAD_REQUEST);
            }

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

                // Response Mapping
                consentResponse.setConsent(consent);
                consentResponse.setResponseMessage(HttpStatus.OK.name());
                consentResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + consentId + " Not Found");
                consentResponse.setErrros(errMessages);
                consentResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                consentResponse.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            consentResponse.setErrros(errMessages);
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
                consentListResponse.setErrros(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }

            // Check If Cif or IdNumber is Null
            if (request.getConsent().getCifId() == null || (request.getConsent().getIdType() == null && request.getConsent().getIdNumber() == null)){
                // Response Mapping for Bad Request
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrros(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }
            String cif = request.getConsent().getCifId();
            String idType = request.getConsent().getIdType();
            String idNumber = request.getConsent().getIdNumber();

            if (!cif.isBlank() || (!idType.isBlank() && !idNumber.isBlank())) {
                // Get Consent by Cif or IdNumber
                List<Consent> consents = consentRepository.findByCifId(cif);
                List<Consent> consentsByIdNumber = consentRepository.findByIdTypeAndIdNumber(idType, idNumber);

                // Remove Duplicates
                consentsByIdNumber.removeAll(consents);
                consents.addAll(consentsByIdNumber);

                // Check if empty
                if (consents.isEmpty()) {
                    errMessages.add("Data Not Found");
                    consentListResponse.setErrros(errMessages);
                    consentListResponse.setResponseMessage(HttpStatus.NOT_FOUND.name());
                    consentListResponse.setStatus(HttpStatus.NOT_FOUND.value());
                    return new ResponseEntity<>(consentListResponse, HttpStatus.NOT_FOUND);
                } else {
                    // Get ClauseName for every ConsentEntityAssoc
                    for (int i=0; i<consents.size(); i++){
                        for (int j=0; j<consents.get(i).getConsentEntityAssocs().size(); j++){
                            // Check if ClauseCode exists in CLAUSE table
                            long clauseCode = consents.get(i).getConsentEntityAssocs().get(i).getClauseCode();
                            Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                            if (clauseData.isPresent()){
                                consents.get(i).getConsentEntityAssocs().get(j).setClauseName(clauseData.get().getClauseName());
                            } else {
                                consents.get(i).getConsentEntityAssocs().get(j).setClauseName("UNDEFINED");
                            }
                        }
                    }
                }

                // Response Mapping
                consentListResponse.setConsents(consents);
                consentListResponse.setResponseMessage(HttpStatus.OK.name());
                consentListResponse.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.OK);
            } else {
                // Response Mapping for Bad Request
                errMessages.add("(CIF) or (IdType and IdNumber) can't be Null");
                consentListResponse.setErrros(errMessages);
                consentListResponse.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                consentListResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(consentListResponse, HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            // Response Mapping
            errMessages.add(e.getMessage());
            consentListResponse.setErrros(errMessages);
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
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Get consent_id
            long consentId = request.getConsent().getConsentId();

            // Check if ConsentId exists in CONSENT table
            if (!consentRepository.existsById(consentId)) {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentId " + consentId + " Not Found");
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            // Delete consent by ConsentId
            consentRepository.deleteById(consentId);

            // Response Mapping
            responseWrapper.setResponseMessage(HttpStatus.OK.name());
            responseWrapper.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrros(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
                responseWrapper.setErrros(errMessages);
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
                            responseWrapper.setErrros(errMessages);
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
                        responseWrapper.setErrros(errMessages);
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
                responseWrapper.setErrros(errMessages);
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
            responseWrapper.setErrros(errMessages);
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
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Check if consentEntityAssocId exists
            long consentEntityAssocId = request.getConsentEntityAssoc().getConsentEntityAssocId();
            if (!consentEntityAssocRepository.existsById(consentEntityAssocId)) {
                // Response Mapping for Data Not Found
                errMessages.add("ConsentEntityAssocId " + consentEntityAssocId + " Not Found");
                responseWrapper.setErrros(errMessages);
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
            responseWrapper.setErrros(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveClause")
    public ResponseEntity<ResponseWrapper> saveClause(@Valid @RequestBody SaveClauseRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try{
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Get Current Date
            Calendar now = Calendar.getInstance();
            Date currentDate = now.getTime();

            // Set Clause
            Clause clause = new Clause();

            clause.setClauseCode(request.getClause().getClauseCode());
            clause.setClauseName(request.getClause().getClauseName());
            clause.setClauseCategory(request.getClause().getClauseCategory());
            clause.setClauseRenewalPeriod(request.getClause().getClauseRenewalPeriod());
            clause.setCreateUser(request.getClause().getCreateUser());

            clause.setCreateDate(currentDate);

            // Save Clause
            clauseRepository.save(clause);

            // Response Mapping
            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrros(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllClauses")
    public ResponseEntity<ClauseListResponse> getAllClauses() {

        ClauseListResponse clauseListResponse = new ClauseListResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            // Get all clauses in CLAUSE table
            List<Clause> clauseList = clauseRepository.findAll();

            // Response Mapping
            clauseListResponse.setClause(clauseList);
            clauseListResponse.setResponseMessage(HttpStatus.OK.name());
            clauseListResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(clauseListResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            clauseListResponse.setErrros(errMessages);
            clauseListResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            clauseListResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(clauseListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteClause")
    public ResponseEntity<ResponseWrapper> deleteClause(@Valid @RequestBody DeleteClauseRequestWrapper request, Errors errors) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        List<String> errMessages = new ArrayList<>();

        try {
            // Validate Request Body
            if (errors.hasErrors()) {
                for (int i=0; i<errors.getErrorCount(); i++) {
                    errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
                }
                // Response Mapping for Bad Request
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Check if clause exists
            long clauseCode = request.getClause().getClauseCode();
            if (!clauseRepository.existsById(clauseCode)) {
                // Response Mapping for Data Not Found
                errMessages.add("ClauseCode " + clauseCode + " Not Found");
                responseWrapper.setErrros(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.NOT_FOUND.name());
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }

            // Delete clause by ClauseCode
            clauseRepository.deleteById(clauseCode);

            // Response Mapping
            responseWrapper.setErrros(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.OK.name());
            responseWrapper.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            responseWrapper.setErrros(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
