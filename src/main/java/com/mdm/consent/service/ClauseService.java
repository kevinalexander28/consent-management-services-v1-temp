package com.mdm.consent.service;

import com.mdm.consent.dto.DeleteClauseRequestWrapper;
import com.mdm.consent.dto.SaveClauseRequestWrapper;
import com.mdm.consent.entity.Clause;
import com.mdm.consent.repository.ClauseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ClauseService {

    private static final Logger logger = LoggerFactory.getLogger(ClauseService.class);

    @Autowired
    private ClauseRepository clauseRepository;

    public void saveClause(SaveClauseRequestWrapper request) {
        logger.info("SaveClause Service Called");

        Date currentDate = Calendar.getInstance().getTime();
        Clause clause = new Clause();
        clause.setClauseCode(request.getClause().getClauseCode());
        clause.setClauseName(request.getClause().getClauseName());
        clause.setClauseCategory(request.getClause().getClauseCategory());
        clause.setClauseRenewalPeriod(request.getClause().getClauseRenewalPeriod());
        clause.setCreateUser(request.getClause().getCreateUser());
        clause.setCreateDate(currentDate);

        logger.debug("Save this Clause = {}", clause);
        clauseRepository.save(clause);
    }

    public List<Clause> getAllClauses() {
        logger.info("GetAllClause Service Called");
        return clauseRepository.findAll();
    }

    public boolean deleteClause(DeleteClauseRequestWrapper request) {
        logger.info("DeleteClause Service Called");
        long clauseCode = request.getClause().getClauseCode();
        if (clauseRepository.existsById(clauseCode)) {
            logger.debug("ClauseCode {} Found", clauseCode);
            logger.debug("Delete Clause where ClauseCode = {}", clauseCode);
            clauseRepository.deleteById(clauseCode);
            return true;
        } else {
            logger.debug("ClauseCode {} Not Found", clauseCode);
            return false;
        }
    }
}
