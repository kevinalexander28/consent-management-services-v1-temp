package com.mdm.consent.service;

import com.mdm.consent.dto.DeleteClauseRequestWrapper;
import com.mdm.consent.dto.ResponseWrapper;
import com.mdm.consent.dto.SaveClauseRequestWrapper;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.repository.ClauseRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ClauseService {

    @Autowired
    private ClauseRepository clauseRepository;

    public void saveClause(SaveClauseRequestWrapper request) {
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
    }

    public List<Clause> getAllClauses() {
        // Return all clauses in CLAUSE table
        return clauseRepository.findAll();
    }

    public boolean deleteClause(DeleteClauseRequestWrapper request) {
        // Check if clause exists
        long clauseCode = request.getClause().getClauseCode();

        if (clauseRepository.existsById(clauseCode)) {
            // Delete clause by ClauseCode
            clauseRepository.deleteById(clauseCode);
            return true;
        } else return false;
    }
}
