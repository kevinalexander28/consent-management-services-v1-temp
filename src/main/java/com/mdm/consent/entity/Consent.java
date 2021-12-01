package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CONSENT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Consent {
    /*
    @Id -> declare primary key
    @GeneratedValue -> generate value of the field
    @JsonProperty -> change the name of a field to map to another JSON property
    @Column -> specify the mapped column for a persistent property or field
    @JsonFormat -> specify how to format fields and/or properties for JSON output
    @Temporal -> indicate a specific mapping of java.util.Date or java.util.Calendar
    */

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
    @Column(name = "consent_id")
    private long consentId;

    @JsonProperty("CIF")
    @Column(name = "cif_id", nullable = false)
    private String cifId;

    @JsonProperty("IdType")
    @Column(name = "id_type", nullable = false)
    private String idType;

    @JsonProperty("IdNumber")
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @JsonProperty("TenantType")
    @Column(name = "tenant_tp_cd", nullable = false)
    private long tenantType;

    @JsonProperty("ProcPurpId")
    @Column(name = "proc_purp_id", nullable = false)
    private long procPurpId;

    @JsonProperty("AgreeInd")
    @Column(name = "agree_ind", nullable = false)
    private long agreeInd;

    @JsonProperty("ConsentGiverId")
    @Column(name = "consent_giver_id", nullable = false)
    private String consentGiverId;

    @JsonProperty("BranchCode")
    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("CreateDate")
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("StartDate")
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("EndDate")
    @Column(name = "end_date")
    private Date endDate;

    @JsonProperty("EndReasonType")
    @Column(name = "end_reason_tp_cd")
    private Long endReasonType;

    @JsonProperty("LastUpdateUser")
    @Column(name = "last_update_user", nullable = false)
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("LastUpdateDate")
    @Column(name = "last_update_date", nullable = false)
    private Date lastUpdateDate;

    @OneToMany(targetEntity = ConsentEntityAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consent_id",referencedColumnName = "consent_id")
    @JsonProperty("ConsentEntityAssocs")
    @ToString.Exclude
    private List<ConsentEntityAssoc> consentEntityAssocs;
}
