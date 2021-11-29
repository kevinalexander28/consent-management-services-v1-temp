package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.entity.Clause;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClauseRequest {
    /*
    @JsonProperty -> change the name of a field to map to another JSON property
    */

    @JsonProperty("Clause")
    private Clause clause;
}
