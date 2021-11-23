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
@Table(name = "CONSENTASSOC")
public class ConsentAssoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consentAssocId")
    private long consentAssocId;
    @Column(name = "clauseCode")
    private String clauseCode;
    @Column(name = "lastUpdateUser")
    private String lastUpdateUser;
    @Column(name = "lastUpdateDt")
    private String lastUpdateDt;

}
