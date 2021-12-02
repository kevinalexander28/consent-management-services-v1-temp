package com.mdm.consent.controller;

import com.mdm.consent.dto.*;
import com.mdm.consent.entity.*;
import com.mdm.consent.service.ClauseService;
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
public class ClauseController {

    private static final Logger logger  = LoggerFactory.getLogger(ClauseService.class);

    @Autowired
    private ClauseService clauseService;

    @PostMapping("/saveClause")
    public ResponseEntity<ResponseWrapper> saveClause(@Valid @RequestBody SaveClauseRequestWrapper request, Errors errors) {

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

            logger.info("Call SaveClause Service");
            clauseService.saveClause(request);

            responseWrapper.setResponseMessage(HttpStatus.CREATED.name());
            responseWrapper.setStatus(HttpStatus.CREATED.value());
            logger.debug("Response Mapping for {} = {}", HttpStatus.CREATED.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.CREATED);
        } catch (Exception e) {
            errMessages.add(e.getMessage());
            responseWrapper.setErrors(errMessages);
            responseWrapper.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.BAD_REQUEST.value(), responseWrapper);
            return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllClauses")
    public ResponseEntity<ClauseListResponse> getAllClauses() {

        ClauseListResponse clauseListResponse = new ClauseListResponse();
        List<String> errMessages = new ArrayList<>();

        try {
            logger.info("Call GetAllClauses Service");
            List<Clause> clauseList = clauseService.getAllClauses();
            logger.debug("clauseList = {}", clauseList);

            clauseListResponse.setClause(clauseList);
            clauseListResponse.setResponseMessage(HttpStatus.OK.name());
            clauseListResponse.setStatus(HttpStatus.OK.value());
            logger.error("Response Mapping for {} = {}", HttpStatus.OK.value(), clauseListResponse);
            return new ResponseEntity<>(clauseListResponse, HttpStatus.OK);
        } catch (Exception e) {
            errMessages.add(e.getMessage());
            clauseListResponse.setErrors(errMessages);
            clauseListResponse.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
            clauseListResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("Error Response Mapping for {} = {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), clauseListResponse);
            return new ResponseEntity<>(clauseListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteClause")
    public ResponseEntity<ResponseWrapper> deleteClause(@Valid @RequestBody DeleteClauseRequestWrapper request, Errors errors) {

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

            logger.info("Call DeleteClause Service");
            boolean isDeleted = clauseService.deleteClause(request);
            logger.debug("isDeleted = {}", isDeleted);

            if (!isDeleted) {
                errMessages.add("ClauseCode " + request.getClause().getClauseCode() + " Not Found");
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
