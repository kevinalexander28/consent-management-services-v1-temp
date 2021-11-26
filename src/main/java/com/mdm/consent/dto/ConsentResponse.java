package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.entity.Consent;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsentResponse {
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Consent")
    private Consent consent;
}
