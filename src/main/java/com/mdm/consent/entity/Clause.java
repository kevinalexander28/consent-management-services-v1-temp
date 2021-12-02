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
