package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
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

    @Column(name = "clause_code", nullable = false)
    private long clauseCode;

    @Transient
    private String clauseName;

    @Column(name = "create_user", nullable = false)
    private String createdUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "create_date", nullable = false)
    private Date createdDate;

    @Column(name = "last_update_user", nullable = false)
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_update_date", nullable = false)
    private Date lastUpdateDate;
}