package com.mdm.consent.dto;

import com.mdm.consent.entity.CdConsentTp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CdConsentTpRequest {
    private List<CdConsentTp> cdConsentTp;
}
