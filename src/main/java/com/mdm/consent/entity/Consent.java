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
@Table(name = "CONSENT")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consent_id")
    private long consentId;

    @Column(name = "cif_id")
    private String cifId;

    @Column(name = "id_type")
    private String idType;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "clause_renewal_period")
    private String clauseRenewalPeriod;

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "created_user")
    private String createdUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_update_user")
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Column(name = "branch_code")
    private String branchCode;

    @OneToMany(targetEntity = ConsentAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consent_id",referencedColumnName = "consent_id")
    @ToString.Exclude
    private List<ConsentAssoc> consentAssocs;
}
