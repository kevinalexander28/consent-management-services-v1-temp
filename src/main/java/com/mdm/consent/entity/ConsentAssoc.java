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