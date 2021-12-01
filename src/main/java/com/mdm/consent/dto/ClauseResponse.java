package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.entity.Clause;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClauseResponse {
    /*
    @JsonProperty -> change the name of a field to map to another JSON property
    */

    @JsonProperty("Status")
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Clause")
    private Clause clause;
}
