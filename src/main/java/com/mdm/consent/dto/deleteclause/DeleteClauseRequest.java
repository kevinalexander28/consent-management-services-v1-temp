package com.mdm.consent.dto.deleteclause;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class DeleteClauseRequest {

    @NotNull(message = "ClauseCode can't be Null")
    @JsonProperty("ClauseCode")
    private Long clauseCode;
}
