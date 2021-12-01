package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseWrapper {
    /*
    @JsonProperty -> change the name of a field to map to another JSON property
    */

    @JsonProperty("Status")
    private String status;
}
