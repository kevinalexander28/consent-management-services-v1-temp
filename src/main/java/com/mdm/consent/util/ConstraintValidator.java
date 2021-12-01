package com.mdm.consent.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

public class ConstraintValidator {

    public List<String> checkError(Errors errors) {
        if (errors.hasErrors()) {
            List<String> errMessages = new ArrayList<>();
            for (int i=0; i<errors.getErrorCount(); i++) {
                errMessages.add(errors.getAllErrors().get(i).getDefaultMessage());
            }
            return errMessages;
        } else return null;
    }

}
