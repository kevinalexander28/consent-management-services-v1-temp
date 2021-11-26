package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "CONSENT")
public class Consent {

    //@Id -> declare primary key
    //@GeneratedValue -> generate value of the field
    //@JsonProperty -> change the name of a field to map to another JSON property
    //@Column -> specify the mapped column for a persistent property or field
    //@JsonFormat -> specify how to format fields and/or properties for JSON output
    //@Temporal -> indicate a specific mapping of java.util.Date or java.util.Calendar

    /*
    CREATE TABLE CONSENT (
        CONSENT_ID BIGINT NOT NULL,
        CIF_ID VARCHAR(255) NOT NULL,
        ID_TYPE VARCHAR(255) NOT NULL,
        ID_NUMBER VARCHAR(255) NOT NULL,
        TENANT_TP_CD BIGINT NOT NULL,
        PROC_PURP_ID BIGINT NOT NULL,
        AGREE_IND BIGINT NOT NULL,
        CONSENT_GIVER_ID VARCHAR(255) NOT NULL,
        CREATE_DATE DATETIME2 NOT NULL,
        START_DT DATETIME2 NOT NULL,
        END_DT DATETIME2 NOT NULL,
        END_REASON_TP_CD BIGINT NOT NULL,
        LAST_UPDATE_USER VARCHAR(255) NOT NULL,
        LAST_UPDATE_DT DATETIME2 NOT NULL,
        PRIMARY KEY (CONSENT_ID)
    )
    */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("ConsentId")
    @Column(name = "CONSENT_ID")
    private long consentId;

    @JsonProperty("CIF")
    @Column(name = "CIF_ID", nullable = false)
    private String cifId;

    @JsonProperty("IdType")
    @Column(name = "ID_TYPE", nullable = false)
    private String idType;

    @JsonProperty("IdNumber")
    @Column(name = "ID_NUMBER", nullable = false)
    private String idNumber;

    @JsonProperty("TenantType")
    @Column(name = "TENANT_TP_CD", nullable = false)
    private long tenantType;

    @JsonProperty("ProcPurpId")
    @Column(name = "PROC_PURP_ID", nullable = false)
    private long procPurpId;

    @JsonProperty("AgreeInd")
    @Column(name = "AGREE_IND", nullable = false)
    private long agreeInd;

    @JsonProperty("ConsentGiverId")
    @Column(name = "CONSENT_GIVER_ID", nullable = false)
    private String consentGiverId;

    @JsonProperty("BranchCode")
    @Column(name = "BRANCH_CODE", nullable = false)
    private String branchCode;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("CreateDate")
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("StartDate")
    @Column(name = "START_DT", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("EndDate")
    @Column(name = "END_DT", nullable = false)
    private Date endDate;

    @JsonProperty("EndReasonType")
    @Column(name = "END_REASON_TP_CD")
    private long endReason;

    @JsonProperty("LastUpdateUser")
    @Column(name = "LAST_UPDATE_USER", nullable = false)
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("LastUpdateDate")
    @Column(name = "LAST_UPDATE_DT", nullable = false)
    private Date lastUpdateDate;
}
