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
    @Column(name = "consentAssocId")
    private long consentAssocId;

    @Column(name = "clauseCode")
    private long clauseCode;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "createDate")
    private Date createdDate;

    @Column(name = "createUser")
    private String createdUser;

    @Column(name = "lastUpdateUser")
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "lastUpdateDate")
    private Date lastUpdateDate;
}