package com.mdm.consent.dto.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.entity.Clause;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class ClauseListResponse {

    @JsonProperty("Status")
    private int status;

    @JsonProperty("ResponseMessage")
    private String responseMessage;

    @JsonProperty("Errors")
    private List<String> errors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Clause")
    private List<Clause> clause;
}
