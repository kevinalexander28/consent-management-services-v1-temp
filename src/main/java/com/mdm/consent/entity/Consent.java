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
    private Date clauseRenewalPeriod;

    @Column(name = "sourceSystem")
    private String sourceSystem;

    @Column(name = "lastUpdateUser")
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "lastUpdateDt")
    private Date lastUpdateDt;

    @Column(name = "branchCode")
    private String branchCode;

    @OneToMany(targetEntity = ConsentAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consent_id_fk",referencedColumnName = "consentId")
    private List<ConsentAssoc> consentAssocs;
}
