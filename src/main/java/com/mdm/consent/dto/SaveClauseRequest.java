package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaveClauseRequest {
    /*
    @JsonProperty -> change the name of a field to map to another JSON property
    */

    @NotNull(message = "ClauseCode can't be Null")
    @JsonProperty("ClauseCode")
    private Long clauseCode;

    @NotBlank(message = "ClauseCategory can't be Blank")
    @JsonProperty("ClauseCategory")
    private String clauseCategory;

    @NotBlank(message = "ClauseName can't be Blank")
    @JsonProperty("ClauseName")
    @Length(min = 3, message = "ClauseName min. Length = 3")
    private String clauseName;

    @NotNull(message = "ClauseRenewalPeriod can't be Null")
    @JsonProperty("ClauseRenewalPeriod")
    private Integer clauseRenewalPeriod;

    @NotBlank(message = "CreateUser can't be Blank")
    @JsonProperty("CreateUser")
    private String createUser;
}
