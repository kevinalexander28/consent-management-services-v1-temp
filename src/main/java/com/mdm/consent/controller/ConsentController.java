package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentEntityAssoc;
import com.mdm.consent.repository.ClauseRepository;
import com.mdm.consent.repository.ConsentEntityAssocRepository;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ConsentResponse> addConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();

        try{
            // TODO: Validation (if needed)
            // Check if IdType exists in the List of ID Types
            // Check if TenantType exists in the List of Tenant Types
            // Check if ProcPurpId exists in the List of Processing Purpose IDs
            // Check if AgreeInd exists in the List of Agree Indicators
            // Check if EndReasonType exists in the List of End Reason Types

            // Check if ConsentId exists in CONSENT table
            long consentId = request.getConsent().getConsentId();
            Optional<Consent> existingConsent = consentRepository.findById(consentId);

            // Get Current Date
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            // Declare Consent
            Consent consent = new Consent();

            if (existingConsent.isEmpty()) {
                // Get new consent
                consent = request.getConsent();

                // Set CreateDate and LastUpdateDate to Current Date
                consent.setCreateDate(currentDate);
                consent.setLastUpdateDate(currentDate);
                consent.setStartDate(currentDate);

                // Set EndDate to 5 years from Start Date
                calendar.add(Calendar.YEAR, 5);
                Date renewalDate = calendar.getTime();
                consent.setEndDate(renewalDate);

                // Set LastUpdateUser with the same value as CreateUser only when Add New Consent (First Time)
                String consentGiver = request.getConsent().getConsentGiverId();
                consent.setLastUpdateUser(consentGiver);

                // Set Null
                consent.setEndReasonType(null);

                // Set ConsentEntityAssoc
                for (int i=0; i<consent.getConsentEntityAssocs().size(); i++){
                    // Set CreateDate and LastUpdateDate to Current Date
                    consent.getConsentEntityAssocs().get(i).setCreateDate(currentDate);
                    consent.getConsentEntityAssocs().get(i).setLastUpdateDate(currentDate);

                    // Set CreateUser and LastUpdateUser with the same value as ConsentGiverId
                    consent.getConsentEntityAssocs().get(i).setCreateUser(consentGiver);
                    consent.getConsentEntityAssocs().get(i).setLastUpdateUser(consentGiver);

                    // Check if ClauseCode exists in CLAUSE table
                    long clauseCode = consent.getConsentEntityAssocs().get(i).getClauseCode();
                    Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                    if (clauseData.isPresent()){
                        consent.getConsentEntityAssocs().get(i).setClauseName(clauseData.get().getClauseName());
                    }
                }

                // Save Consent
                consentRepository.save(consent);
            } else {
                // Set status to Invalid Body Request
                consentResponse.setStatus("Invalid Body Request");
                return new ResponseEntity<>(consentResponse, HttpStatus.BAD_REQUEST);
            }
            // Set status to Created
            consentResponse.setStatus("Created");
            // Response Mapping
            consentResponse.setConsent(consent);
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateConsent")
    public ResponseEntity<ConsentResponse> updateConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();

        try{
            // TODO: Validation (if needed)
            // Check if IdType exists in the List of ID Types
            // Check if TenantType exists in the List of Tenant Types
            // Check if ProcPurpId exists in the List of Processing Purpose IDs
            // Check if AgreeInd exists in the List of Agree Indicators
            // Check if EndReasonType exists in the List of End Reason Types

            // Check if ConsentId exists in CONSENT table
            long consentId = request.getConsent().getConsentId();
            Optional<Consent> existingConsent = consentRepository.findById(consentId);

            // Get Current Date
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();

            // Declare Consent
            Consent consent = new Consent();

            if (existingConsent.isPresent()) {
                // Get existing consent
                consent = existingConsent.get();

                // Set New Values
                consent.setCifId(request.getConsent().getCifId());
                consent.setIdType(request.getConsent().getIdType());
                consent.setIdNumber(request.getConsent().getIdNumber());
                consent.setTenantType(request.getConsent().getTenantType());
                consent.setProcPurpId(request.getConsent().getProcPurpId());
                consent.setAgreeInd(request.getConsent().getAgreeInd());
                consent.setBranchCode(request.getConsent().getBranchCode());
                consent.setEndReasonType(request.getConsent().getEndReasonType());
                consent.setLastUpdateUser(request.getConsent().getConsentGiverId());

                // Set LastUpdateDate to Current Date
                consent.setLastUpdateDate(currentDate);

                // Get ClauseName for every ConsentEntityAssoc
                for(int i=0; i<consent.getConsentEntityAssocs().size(); i++){
                    // Check if ClauseCode exists in CLAUSE table
                    long clauseCode = consent.getConsentEntityAssocs().get(i).getClauseCode();
                    Optional<Clause> clauseData = clauseRepository.findById(clauseCode);
                    if (clauseData.isPresent()){
                        consent.getConsentEntityAssocs().get(i).setClauseName(clauseData.get().getClauseName());
                    }
                }

                // Save Consent
                consentRepository.save(consent);
            } else {
                // Set status to not found
                consentResponse.setStatus("ConsentId Not Found");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
            // Set status to Updated
            consentResponse.setStatus("Updated");
            // Response Mapping
            consentResponse.setConsent(consent);
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("Internal Server Error");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();

            // Check if ConsentId exists in CONSENT table
            Optional<Consent> consentData = consentRepository.findById(request.getConsent().getConsentId());

            if (consentData.isPresent()) {
                for(int i = 0; i<request.getConsent().getConsentEntityAssocs().size(); i++) {
                    request.getConsent().getConsentEntityAssocs().get(i).setCreateDate(date);
                    request.getConsent().getConsentEntityAssocs().get(i).setLastUpdateDate(date);
                    request.getConsent().getConsentEntityAssocs().get(i).setCreateUser(request.getConsent().getConsentGiverId());
                    request.getConsent().getConsentEntityAssocs().get(i).setLastUpdateUser(request.getConsent().getConsentGiverId());
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
                consentResponse.setConsent(consent);
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

            // Get list of clauses
            Clause clauses = clauseRepository.save(request.getClause());

            clauseResponse.setStatus("Created");
            clauseResponse.setClause(clauses);
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
