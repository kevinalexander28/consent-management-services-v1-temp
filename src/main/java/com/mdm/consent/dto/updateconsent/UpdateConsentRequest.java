package com.mdm.consent.dto.updateconsent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class UpdateConsentRequest {

    @NotNull(message = "ConsentId can't be Null")
    @JsonProperty("ConsentId")
    private Long consentId;

    @NotBlank(message = "CIF can't be Blank")
    @JsonProperty("CIF")
    private String cifId;

    @NotBlank(message = "IdType can't be Blank")
    @JsonProperty("IdType")
    private String idType;

    @NotBlank(message = "IdNumber can't be Blank")
    @JsonProperty("IdNumber")
    private String idNumber;

    @NotNull(message = "TenantType can't be Null")
    @JsonProperty("TenantType")
    private Long tenantType;

    @NotNull(message = "ProcPurpId can't be Null")
    @JsonProperty("ProcPurpId")
    private Long procPurpId;

    @NotNull(message = "AgreeInd can't be Null")
    @JsonProperty("AgreeInd")
    private Long agreeInd;

    @NotBlank(message = "ConsentGiverId can't be Blank")
    @JsonProperty("ConsentGiverId")
    private String consentGiverId;

    @NotBlank(message = "BranchCode can't be Blank")
    @JsonProperty("BranchCode")
    private String branchCode;

    @NotNull(message = "EndReasonType can't be Null")
    @JsonProperty("EndReasonType")
    private Long endReasonType;
}
