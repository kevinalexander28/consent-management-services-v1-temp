package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CONSENTENTITYASSOC")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentEntityAssoc {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("ConsentEntityAssocId")
    @Column(name = "consent_entity_assoc_id")
    private long consentEntityAssocId;

    @JsonProperty("ClauseCode")
    @Column(name = "clause_code", nullable = false)
    private long clauseCode;

    @Transient
    @JsonProperty("ClauseName")
    private String clauseName;

    @JsonProperty("CreateUser")
    @Column(name = "create_user", nullable = false)
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("StartDate")
    @Column(name = "start_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("EndDate")
    @Column(name = "end_date")
    private Date endDate;
}

