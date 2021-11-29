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
    /*
    @JsonProperty -> change the name of a field to map to another JSON property
    */

    @JsonProperty("Consent")
    private Consent consent;
}
