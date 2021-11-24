package com.mdm.consent.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CONSENT")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consentId")
    private long consentId;

    @Column(name = "cifId")
    private String cifId;

    @Column(name = "idType")
    private String idType;

    @Column(name = "idNumber")
    private String idNumber;

    @Column(name = "clauseRenewalPeriod")
    private String clauseRenewalPeriod;

    @Column(name = "sourceSystem")
    private String sourceSystem;

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

    @Column(name = "branchCode")
    private String branchCode;

    @OneToMany(targetEntity = ConsentAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consentId",referencedColumnName = "consentId")
    private List<ConsentAssoc> consentAssocs;
}
