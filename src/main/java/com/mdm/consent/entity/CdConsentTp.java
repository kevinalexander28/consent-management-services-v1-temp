package com.mdm.consent.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CDCONSENTTP")
public class CdConsentTp {
    @Id
    @Column(name = "clause_code")
    private long clauseCode;

    @Column(name = "clause_category")
    private String clauseCategory;

    @Column(name = "clause_name")
    private String clauseName;

}
