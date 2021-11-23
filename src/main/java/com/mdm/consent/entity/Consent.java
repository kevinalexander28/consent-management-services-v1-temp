package com.mdm.consent.entity;

import javax.persistence.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Column(name = "lastUpdateUser")
    private String lastUpdateUser;
    @Column(name = "lastUpdateDt")
    private String lastUpdateDt;
    @Column(name = "branchCode")
    private String branchCode;
    @OneToMany(targetEntity = ConsentAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consent_id_fk",referencedColumnName = "consentId")
    private List<ConsentAssoc> consentAssocs;
}
