package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class GetConsentByCifOrIdNumberRequest {

    @JsonProperty("CIF")
    private String cifId;

    @JsonProperty("IdType")
    private String idType;

    @JsonProperty("IdNumber")
    private String idNumber;
}
