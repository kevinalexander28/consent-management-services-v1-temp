package com.mdm.consent.dto;

import com.mdm.consent.entity.Consent;
import com.mdm.consent.entity.ConsentAssoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsentAssocRequest {
    private ConsentAssoc consentAssoc;
}
