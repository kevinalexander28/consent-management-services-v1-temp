package com.mdm.consent.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CONSENTASSOC")
public class ConsentAssoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consent_assoc_id")
    private long consentAssocId;

    @Column(name = "clause_code")
    private long clauseCode;

    @Column(name = "create_user")
    private String createdUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "create_date")
    private Date createdDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
}