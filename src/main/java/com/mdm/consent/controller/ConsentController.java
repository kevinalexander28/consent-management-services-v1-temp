package com.mdm.consent.controller;

import com.mdm.consent.dto.ConsentRequest;
import com.mdm.consent.dto.ConsentResponse;
import com.mdm.consent.entity.Consent;
import com.mdm.consent.repository.ConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ConsentController {

    @Autowired
    private ConsentRepository consentRepository;

    @PostMapping("/saveConsent")
    public ResponseEntity<ConsentResponse> saveConsent(@RequestBody ConsentRequest request) {
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
            Consent consent;

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
                // Save Consent
                consentRepository.save(consent);
                // Set status to Updated
                consentResponse.setStatus("UPDATED");
            } else {
                // Get new consent
                consent = request.getConsent();
                // Set CreateDate and LastUpdateDate to Current Date
                consent.setCreateDate(currentDate);
                consent.setLastUpdateDate(currentDate);
                consent.setStartDate(currentDate);

                // Set End Date to 5 years from Start Date
                calendar.add(Calendar.YEAR, 5);
                Date renewalDate = calendar.getTime();
                consent.setEndDate(renewalDate);

                // Set LastUpdateUser with the same value as CreateUser only when Add New Consent (First Time)
                String consentGiver = request.getConsent().getConsentGiverId();
                consent.setLastUpdateUser(consentGiver);

                // Set Null
                consent.setEndReasonType(null);

                // Save Consent
                consentRepository.save(consent);
                // Set status to Created
                consentResponse.setStatus("CREATED");
            }
            // Print
            System.out.println(consent);
            // Response Mapping
            consentResponse.setConsent(consent);
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("INTERNAL SERVER ERROR");
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

                // TODO (if needed)
                // Join Table

                // Response Mapping
                consentResponse.setConsent(consent);
                consentResponse.setStatus("SUCCESS");
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                // Response Mapping for Data Not Found
                consentResponse.setStatus("DATA NOT FOUND");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
                consentResponse.setStatus("ConsentId is not exists");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }

            // Delete consent by ConsentId
            consentRepository.deleteById(consentId);

            // Response Mapping
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
