package com.mdm.consent.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;


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

    // Tidak boleh null
    //@Notnull
    @Column(name = "clause_category", nullable = false)
    private String clauseCategory;

    // Tidak boleh null
    // Length minimal 3
    //@Notnull
    @Column(name = "clause_name", nullable = false)
    @Length(min = 3)
    private String clauseName;
}
