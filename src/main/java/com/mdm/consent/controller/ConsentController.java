package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.repository.*;
import com.mdm.consent.util.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
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

        ConstraintValidator constraintValidator = new ConstraintValidator();

        List<String> errMessages = constraintValidator.checkError(errors);

        if (errMessages.isEmpty()) {
            responseWrapper.setErrorMessage(errMessages);
            responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
        }

        try{
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
                    // Response Mapping for ClauseCode Not Found
                    responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            // Add List of ConsentEntityAssoc to Consent
            consent.setConsentEntityAssocs(consentEntityAssocs);
            // Save Consent
            consentRepository.save(consent);
            // Response Mapping
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            System.out.println("==============");
            System.out.println(e.getMessage());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateConsent")
    public ResponseEntity<ResponseWrapper> updateConsent(@RequestBody UpdateConsentRequest request) {
        ResponseWrapper responseWrapper = new ResponseWrapper();

        try{
            // TODO: Validation (if needed)

            // Check if ConsentId exists in CONSENT table
            long consentId = request.getConsentId();
            Optional<Consent> existingConsent = consentRepository.findById(consentId);

            if (existingConsent.isPresent()) {
                // Get Current Date
                Calendar now = Calendar.getInstance();
                Date currentDate = now.getTime();

                // Set New Values
                existingConsent.get().setCifId(request.getCifId());
                existingConsent.get().setIdType(request.getIdType());
                existingConsent.get().setIdNumber(request.getIdNumber());
                existingConsent.get().setTenantType(request.getTenantType());
                existingConsent.get().setProcPurpId(request.getProcPurpId());
                existingConsent.get().setAgreeInd(request.getAgreeInd());
                existingConsent.get().setBranchCode(request.getBranchCode());
                existingConsent.get().setEndReasonType(request.getEndReasonType());
                existingConsent.get().setLastUpdateUser(request.getConsentGiverId());

                // Set LastUpdateDate to Current Date
                existingConsent.get().setLastUpdateDate(currentDate);

                // Save Consent
                consentRepository.save(existingConsent.get());
            } else {
                // Set status to not found
                responseWrapper.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.NOT_FOUND);
            }
            // Set status to Updated
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            // Response Mapping
            // consentResponse.setConsent(consent);
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsent")
    public ResponseEntity<ConsentResponse> getConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
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
                    }
                }

                // Response Mapping
                consentResponse.setConsent(consent);
                consentResponse.setStatus("Success");
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                // Response Mapping for Data Not Found
                consentResponse.setStatus("ConsentId Not Found");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsentByCifOrIdNumber")
    public ResponseEntity<ConsentListResponse> getConsentByCifOrIdNumber(@RequestBody ConsentRequest request) {
        ConsentListResponse consentListResponse = new ConsentListResponse();
        try {
            // Get Consent by CifId or IdType-IdNumber
            String cifId = request.getConsent().getCifId();
            String idType = request.getConsent().getIdType();
            String idNumber = request.getConsent().getIdNumber();
            List<Consent> consentsByCif = consentRepository.findByCifId(cifId);
            List<Consent> consentsByIdNumber = consentRepository.findByIdTypeAndIdNumber(idType, idNumber);

            // Remove Duplicates
            consentsByIdNumber.removeAll(consentsByCif);
            consentsByCif.addAll(consentsByIdNumber);

            // Check if empty
            if (consentsByCif.isEmpty()) {
                consentListResponse.setStatus("Data Not Found");
                return new ResponseEntity<>(consentListResponse, HttpStatus.NOT_FOUND);
            } else {
                // Get ClauseName for every ConsentEntityAssoc
                for (int i=0; i<consentsByCif.size(); i++){
                    for (int j=0; j<consentsByCif.get(i).getConsentEntityAssocs().size(); j++){
                        // Check if ClauseCode exists in CLAUSE table
                        long clauseCode = consentsByCif.get(i).getConsentEntityAssocs().get(i).getClauseCode();
                        Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                        if (clauseData.isPresent()){
                            consentsByCif.get(i).getConsentEntityAssocs().get(j).setClauseName(clauseData.get().getClauseName());
                        }
                    }
                }
            }

            // Response Mapping
            consentListResponse.setConsent(consentsByCif);
            consentListResponse.setStatus("Success");
            return new ResponseEntity<>(consentListResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping
            consentListResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(consentListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsent")
    public ResponseEntity<ConsentResponse> deleteConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            // Get consent_id
            long consentId = request.getConsent().getConsentId();

            // Check if ConsentId exists in CONSENT table
            if (!consentRepository.existsById(consentId)) {
                // Response Mapping for Data Not Found
                consentResponse.setStatus("ConsentId Not Found");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }

            // Delete consent by ConsentId
            consentRepository.deleteById(consentId);

            // Response Mapping
            consentResponse.setStatus("Success");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addConsentEntityAssoc")
    public ResponseEntity<ConsentResponse> addConsentEntityAssoc(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            // Get Current Date
            Calendar now = Calendar.getInstance();
            Date date = now.getTime();

            // Check if ConsentId exists in CONSENT table
            Optional<Consent> consentData = consentRepository.findById(request.getConsent().getConsentId());

            if (consentData.isPresent()) {
                for(int i = 0; i<request.getConsent().getConsentEntityAssocs().size(); i++) {
                    request.getConsent().getConsentEntityAssocs().get(i).setCreateDate(date);
                    request.getConsent().getConsentEntityAssocs().get(i).setCreateUser(request.getConsent().getConsentGiverId());
                }

                // Add new ConsentEntityAssocs to existing consent
                consentData.get().getConsentEntityAssocs().addAll(request.getConsent().getConsentEntityAssocs());
                Consent consent = consentRepository.save(consentData.get());

                // Get ClauseName for every ClauseCode in ConsentEntityAssoc
                for(int i = 0; i<consent.getConsentEntityAssocs().size(); i++) {
                    long clauseCode = consent.getConsentEntityAssocs().get(i).getClauseCode();
                    Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                    if (clauseData.isPresent()) {
                        String clauseName = clauseData.get().getClauseName();
                        consent.getConsentEntityAssocs().get(i).setClauseName(clauseName);
                    }
                }
                // consentResponse.setConsent(consent);
            } else {
                // Response Mapping for Data Not Found
                consentResponse.setStatus("Data Not Found");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
            // Response Mapping
            consentResponse.setStatus("Success");
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsentEntityAssoc")
    public ResponseEntity<ConsentResponse> deleteConsentEntityAssoc(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            // Delete clause by ClauseCode
            consentEntityAssocRepository.deleteAll(request.getConsent().getConsentEntityAssocs());

            // Response Mapping
            consentResponse.setStatus("Deleted");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveClause")
    public ResponseEntity<ClauseResponse> saveClause(@RequestBody ClauseRequest request) {
        ClauseResponse clauseResponse = new ClauseResponse();

        try{
            // TODO: Validation (if needed)

            // Get Current Date
            Calendar now = Calendar.getInstance();
            Date currentDate = now.getTime();

            // Set CreateDate to Current Date
            request.getClause().setCreateDate(currentDate);

            // Get list of clauses
            Clause clauses = clauseRepository.save(request.getClause());

            clauseResponse.setStatus("Created");
            // clauseResponse.setClause(clauses);
            return new ResponseEntity<>(clauseResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            clauseResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(clauseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllClauses")
    public ResponseEntity<ClauseListResponse> getAllClauses() {
        ClauseListResponse clauseListResponse = new ClauseListResponse();
        try {
            // Get all clauses in CLAUSE table
            List<Clause> clauseList = clauseRepository.findAll();

            // Response Mapping
            clauseListResponse.setClause(clauseList);
            clauseListResponse.setStatus("Success");
            return new ResponseEntity<>(clauseListResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            clauseListResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(clauseListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteClause")
    public ResponseEntity<ClauseResponse> deleteClause(@RequestBody ClauseRequest request) {
        ClauseResponse clauseResponse = new ClauseResponse();
        try {
            // Delete clause by ClauseCode
            clauseRepository.deleteById(request.getClause().getClauseCode());

            // Response Mapping
            clauseResponse.setStatus("Deleted");
            return new ResponseEntity<>(clauseResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            clauseResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(clauseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
