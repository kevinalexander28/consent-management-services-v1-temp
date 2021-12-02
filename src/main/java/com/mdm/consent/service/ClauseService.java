package com.mdm.consent.service;

import com.mdm.consent.dto.ResponseWrapper;
import com.mdm.consent.dto.SaveClauseRequestWrapper;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.repository.ClauseRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

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
}
