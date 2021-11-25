package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
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
    public ResponseEntity<ConsentResponse> addConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            Date date = calendar.getTime();

            // Mengisi waktu saat ini untuk created_date dan last_update_date untuk pertama kali
            request.getConsent().setCreatedDate(date);
            request.getConsent().setLastUpdateDate(date);

            // Mengisi last_update_user dengan created_user untuk pertama kali
            request.getConsent().setLastUpdateUser(request.getConsent().getCreatedUser());

            for(int i = 0; i<request.getConsent().getConsentAssocs().size(); i++) {
                // Mengambil data clause_name berdasarkan clause_code jika ada
                long checkClauseCode = request.getConsent().getConsentAssocs().get(i).getClauseCode();
                Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(checkClauseCode);
                if (cdConsentTpData.isPresent()) {
                    request.getConsent().getConsentAssocs().get(i).setClauseName(cdConsentTpData.get().getClauseName());
                    // Mengisi created_date, last_update_date, created_user, last_update_user pada ConsentAssoc
                    // sesuai isian pada Consent untuk pertama kali
                    request.getConsent().getConsentAssocs().get(i).setCreatedDate(date);
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateDate(date);
                    request.getConsent().getConsentAssocs().get(i).setCreatedUser(request.getConsent().getCreatedUser());
                    request.getConsent().getConsentAssocs().get(i).setLastUpdateUser(request.getConsent().getCreatedUser());
                } else {
                    // Mapping Response - status, invalid clause code
                    consentResponse.setStatus("INVALID CLAUSE CODE");
                    return new ResponseEntity<>(consentResponse, HttpStatus.NOT_ACCEPTABLE);
                }
            }
            // Simpan Consent
            Consent _consent = consentRepository.save(request.getConsent());
            // Mapping Response - status, success
            consentResponse.setConsent(_consent);
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getConsent")
    public ResponseEntity<ConsentResponse> getConsent(@RequestBody ConsentRequest request){
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            // Melakukan pengecekan cif_id terdapat atau tidak pada tabel Consent
            long consentId = request.getConsent().getConsentId();
            Optional<Consent> consentData = consentRepository.findById(consentId);
            if (consentData.isPresent()){
                Consent consent = consentData.get();
                // Mengambil data clause_name berdasarkan clause_code jika ada
                for (int j = 0; j < consent.getConsentAssocs().size(); j++) {
                    ConsentAssoc consentAssoc = consent.getConsentAssocs().get(j);
                    long clauseCode = consentAssoc.getClauseCode();
                    Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                    if (cdConsentTpData.isPresent()) {
                        String clauseName = cdConsentTpData.get().getClauseName();
                        consent.getConsentAssocs().get(j).setClauseName(clauseName);
                    }
                }
                // Mapping Response - status, success
                consentResponse.setConsent(consent);
                consentResponse.setStatus("SUCCESS");
                return new ResponseEntity<>(consentResponse, HttpStatus.OK);
            } else {
                // Mapping Response - status, data not found
                consentResponse.setStatus("DATA NOT FOUND");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllConsentByCIFId")
    public ResponseEntity<ConsentListResponse> getAllConsentByCifId(@RequestBody ConsentRequest request){
        ConsentListResponse consentListResponse = new ConsentListResponse();
        try {
            // Melakukan pengecekan cif_id terdapat atau tidak pada tabel Consent
            String cifId = request.getConsent().getCifId();
            List<Consent> consents = consentRepository.findByCifId(cifId);
            if (consents.isEmpty()) {
                // Mapping Response - status, data not found
                consentListResponse.setStatus("DATA NOT FOUND");
                return new ResponseEntity<>(consentListResponse, HttpStatus.NOT_FOUND);
            }
            // Mengambil data clause_name berdasarkan clause_code jika ada
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
            // Mapping Response - status, success
            consentListResponse.setConsent(consents);
            consentListResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentListResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentListResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateConsent")
    public ResponseEntity<ConsentResponse> updateConsent(@RequestBody ConsentRequest request) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Date date = calendar.getTime();

        ConsentResponse consentResponse = new ConsentResponse();

        // Melakukan pengecekan consent_id terdapat atau tidak pada tabel Consent
        long consentId = request.getConsent().getConsentId();
        Optional<Consent> consentData = consentRepository.findById(consentId);
        if (consentData.isPresent()) {
            Consent _consent = consentData.get();
            _consent.setCifId(request.getConsent().getCifId());
            _consent.setIdType(request.getConsent().getIdType());
            _consent.setIdNumber(request.getConsent().getIdNumber());
            _consent.setClauseRenewalPeriod(request.getConsent().getClauseRenewalPeriod());
            _consent.setSourceSystem(request.getConsent().getSourceSystem());
            // Update tidak mengubah created_date dan created_user
            _consent.setLastUpdateUser(request.getConsent().getLastUpdateUser());
            _consent.setLastUpdateDate(date);
            _consent.setBranchCode(request.getConsent().getBranchCode());

            consentRepository.save(_consent);
            for (int j = 0; j < _consent.getConsentAssocs().size(); j++) {
                long clauseCode = _consent.getConsentAssocs().get(0).getClauseCode();
                Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                if (cdConsentTpData.isPresent()) {
                    String clauseName = cdConsentTpData.get().getClauseName();
                    _consent.getConsentAssocs().get(j).setClauseName(clauseName);
                }
            }
            // Mapping Response - status, success
            consentResponse.setConsent(_consent);
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } else {
            // Mapping Response - status, any errors
            consentResponse.setStatus("NOT FOUND");
            return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteConsent")
    public ResponseEntity<ConsentResponse> deleteConsent(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            long consentId = request.getConsent().getConsentId();
            // To Do (jika diperluka): Cek consent_id terdapat pada tabel consent atau tidak
            // Hapus consent
            consentRepository.deleteById(consentId);
            // Mapping Response - status, success
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addConsentAssoc")
    public ResponseEntity<ConsentResponse> addConsentAssoc(@RequestBody ConsentRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            Date date = calendar.getTime();

            // Melakukan pengecekan terhadap clause_code yang diinput terdapat atau tidak pada tabel CdConsentTp
            long consentId = request.getConsent().getConsentId();
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
                    // Mapping Response - status, invalid clause code
                    consentResponse.setStatus("INVALID CLAUSE CODE");
                    return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
                }
            }
            // Melakukan pengecekan consent_id yang diinput terdapat atau tidak pada tabel Consent
            Optional<Consent> consentData = consentRepository.findById(consentId);
            if (consentData.isPresent()){
                // Menambahkan ConsentAssoc baru pada Consent tersebut
                consentData.get().getConsentAssocs().addAll(request.getConsent().getConsentAssocs());
                // Simpan Consent
                Consent _consent = consentRepository.save(consentData.get());
                // Mengambil data clause_name berdasarkan clause_code jika ada
                for(int i = 0; i<_consent.getConsentAssocs().size(); i++) {
                    long clauseCode = _consent.getConsentAssocs().get(i).getClauseCode();
                    Optional<CdConsentTp> cdConsentTpData = cdConsentTpRepository.findById(clauseCode);
                    if (cdConsentTpData.isPresent()) {
                        String clauseName = cdConsentTpData.get().getClauseName();
                        _consent.getConsentAssocs().get(i).setClauseName(clauseName);
                    }
                }
                // Mapping Response - consent
                consentResponse.setConsent(_consent);
            } else {
                // Mapping Response - status, consent_id not found
                consentResponse.setStatus("CONSENT ID NOT FOUND");
                return new ResponseEntity<>(consentResponse, HttpStatus.NOT_FOUND);
            }
            // Mapping Response - status, success
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteConsentAssoc")
    public ResponseEntity<ConsentResponse> deleteConsentAssoc(@RequestBody ConsentAssocRequest request) {
        ConsentResponse consentResponse = new ConsentResponse();
        try {
            long consentAssocId = request.getConsentAssoc().getConsentAssocId();
            // To Do (jika diperluka): Cek consent_assoc_id terdapat pada tabel consent atau tidak
            // Hapus ConsentAssoc
            consentAssocRepository.deleteById(consentAssocId);
            // Mapping Response - status, success
            consentResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(consentResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            consentResponse.setStatus("INTERNAL SERVER ERROR");
            return new ResponseEntity<>(consentResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListOfConsent")
    public ResponseEntity<CdConsentTpResponse> getListOfConsent() {
        CdConsentTpResponse cdConsentTpResponse = new CdConsentTpResponse();
        try {
            // Mengambil list of consent pada tabel CdConsentTp
            List<CdConsentTp> cdConsentTps = cdConsentTpRepository.findAllNotZero();
            // Mapping Response - status, success
            cdConsentTpResponse.setCdConsentTp(cdConsentTps);
            cdConsentTpResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(cdConsentTpResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            cdConsentTpResponse.setStatus("INTERNAL_SERVER_ERROR");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveListOfConsent")
    public ResponseEntity<CdConsentTpResponse> addListOfConsent(@RequestBody CdConsentTpRequest request) {
        CdConsentTpResponse cdConsentTpResponse = new CdConsentTpResponse();
        try {
            // Simpan list of consent
            List<CdConsentTp> _cdConsentTps = cdConsentTpRepository.saveAll(request.getCdConsentTp());
            // Mapping Response - status, success
            cdConsentTpResponse.setCdConsentTp(_cdConsentTps);
            cdConsentTpResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(cdConsentTpResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Mapping Response - status, any errors
            cdConsentTpResponse.setStatus("INTERNAL_SERVER_ERROR");
            return new ResponseEntity<>(cdConsentTpResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteListOfConsent")
    public ResponseEntity<CdConsentTpResponse> deleteListOfConsent(@RequestBody CdConsentTpRequest request) {
        CdConsentTpResponse cdConsentTpResponse = new CdConsentTpResponse();
        try {
            cdConsentTpRepository.deleteAll(request.getCdConsentTp());
            cdConsentTpResponse.setStatus("SUCCESS");
            return new ResponseEntity<>(cdConsentTpResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            cdConsentTpResponse.setStatus("INTERNAL_SERVER_ERROR");
            return new ResponseEntity<>(cdConsentTpResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
