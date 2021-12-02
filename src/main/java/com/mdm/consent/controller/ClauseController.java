package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.repository.*;
import com.mdm.consent.service.ClauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/consent-management-services-v1")
public class ClauseController {

    @Autowired
    private ClauseRepository clauseRepository;

    @Autowired
    private ClauseService clauseService;

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
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            // Save Clause
            clauseService.saveClause(request);

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

    @GetMapping("/getAllClauses")
    public ResponseEntity<ClauseListResponse> getAllClauses() {

        ClauseListResponse clauseListResponse = new ClauseListResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            // Get all clauses in CLAUSE table
            List<Clause> clauseList = clauseService.getAllClauses();

            // Response Mapping
            clauseListResponse.setClause(clauseList);
            clauseListResponse.setResponseMessage(HttpStatus.OK.name());
            clauseListResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(clauseListResponse, HttpStatus.OK);
        } catch (Exception e) {
            // Response Mapping for Internal Server Error
            errMessages.add(e.getMessage());
            clauseListResponse.setErrors(errMessages);
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
                responseWrapper.setErrors(errMessages);
                responseWrapper.setResponseMessage(HttpStatus.BAD_REQUEST.name());
                responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
            }

            boolean isDeleted = clauseService.deleteClause(request);

            if (isDeleted) {
                // Response Mapping for Data Not Found
                errMessages.add("ClauseCode " + request.getClause().getClauseCode() + " Not Found");
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