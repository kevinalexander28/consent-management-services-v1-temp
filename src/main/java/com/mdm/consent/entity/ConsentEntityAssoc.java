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
    /*
    @Id -> declare primary key
    @GeneratedValue -> generate value of the field
    @JsonProperty -> change the name of a field to map to another JSON property
    @Column -> specify the mapped column for a persistent property or field
    @JsonFormat -> specify how to format fields and/or properties for JSON output
    @Temporal -> indicate a specific mapping of java.util.Date or java.util.Calendar
    */

    /*
    CREATE TABLE CONSENTENTITYASSOC (
        CONSENT_ENTITY_ASSOC_ID BIGINT NOT NULL,
        CLAUSE_CODE BIGINT NOT NULL,
        CREATE_DATE DATETIME2 NOT NULL,
        CREATE_USER VARCHAR(255) NOT NULL,
        LAST_UPDATE_DATE DATETIME2 NOT NULL,
        LAST_UPDATE_USER VARCHAR(255) NOT NULL,
        CONSENT_ID BIGINT,
        PRIMARY KEY (CONSENT_ENTITY_ASSOC_ID)
    )

    ALTER TABLE CONSENTENTITYASSOC ADD CONSTRAINT FK23O2D7U586QW891O3BXGWX63S FOREIGN KEY (CONSENT_ID) REFERENCES CONSENT
    */

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
    @JsonProperty("CreateDate")
    @Column(name = "create_date", nullable = false)
    private Date createDate;
}
