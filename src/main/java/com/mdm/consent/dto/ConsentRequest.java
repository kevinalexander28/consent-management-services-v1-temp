package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.entity.Consent;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsentRequest {
    @JsonProperty("Consent")
    private Consent consent;
}
