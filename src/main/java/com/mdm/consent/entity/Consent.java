package com.mdm.consent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
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

    @Column(name = "cif_id", nullable = false)
    private String cifId;

    @Column(name = "id_type", nullable = false)
    private String idType;

    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @Column(name = "clause_renewal_period", nullable = false)
    private String clauseRenewalPeriod;

    @Column(name = "source_system", nullable = false)
    private String sourceSystem;

    @Column(name = "created_user", nullable = false)
    private String createdUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "last_update_user", nullable = false)
    private String lastUpdateUser;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_update_date", nullable = false)
    private Date lastUpdateDate;

    @Column(name = "branch_code", nullable = false)
    private String branchCode;

    @OneToMany(targetEntity = ConsentAssoc.class,cascade = CascadeType.ALL)
    @JoinColumn(name ="consent_id",referencedColumnName = "consent_id")
    @ToString.Exclude
    private List<ConsentAssoc> consentAssocs;
}
