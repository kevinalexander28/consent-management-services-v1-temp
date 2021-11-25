package com.mdm.consent.dto;

import com.mdm.consent.entity.Consent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsentRequest {
    private Consent consent;
}
