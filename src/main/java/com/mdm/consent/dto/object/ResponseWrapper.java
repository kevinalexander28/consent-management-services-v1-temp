package com.mdm.consent.dto.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class ResponseWrapper {

    @JsonProperty("Status")
    private int status;

    @JsonProperty("ResponseMessage")
    private String responseMessage;

    @JsonProperty("Errors")
    private List<String> errors;
}
