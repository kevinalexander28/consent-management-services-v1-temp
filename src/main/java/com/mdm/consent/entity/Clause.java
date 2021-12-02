package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CLAUSE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Clause {
    /*
    @Id -> declare primary key
    @GeneratedValue -> generate value of the field
    @JsonProperty -> change the name of a field to map to another JSON property
    @Column -> specify the mapped column for a persistent property or field
    @JsonFormat -> specify how to format fields and/or properties for JSON output
    @Temporal -> indicate a specific mapping of java.util.Date or java.util.Calendar
    */

    /*
    CREATE TABLE CLAUSE (
        CLAUSE_CODE BIGINT NOT NULL,
        CLAUSE_CATEGORY VARCHAR(255) NOT NULL,
        CLAUSE_NAME VARCHAR(255) NOT NULL,
        PRIMARY KEY (CLAUSE_CODE)
    )
    */

    @Id
    @JsonProperty("ClauseCode")
    @Column(name = "clause_code")
    private long clauseCode;

    @JsonProperty("ClauseCategory")
    @Column(name = "clause_category", nullable = false)
    private String clauseCategory;

    @JsonProperty("ClauseName")
    @Column(name = "clause_name", nullable = false)
    @Length(min = 3)
    private String clauseName;

    @JsonProperty("ClauseRenewalPeriod")
    @Column(name = "clause_renewal_period", nullable = false)
    private int clauseRenewalPeriod;

    @JsonProperty("CreateUser")
    @Column(name = "create_user", nullable = false)
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("CreateDate")
    @Column(name = "create_date", nullable = false)
    private Date createDate;
}
