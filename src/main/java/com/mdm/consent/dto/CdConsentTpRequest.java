package com.mdm.consent.dto;

import com.mdm.consent.entity.CdConsentTp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CdConsentTpRequest {
    private CdConsentTp cdConsentTp;
}
