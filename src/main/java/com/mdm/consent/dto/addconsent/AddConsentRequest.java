package com.mdm.consent.dto.addconsent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.dto.object.ConsentEntityAssocDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class AddConsentRequest {

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
    @Min(value = 0, message = "Minimal value of AgreeInd is 0")
    @Max(value = 2, message = "Maximal value of AgreeInd is 2")
    @JsonProperty("AgreeInd")
    private Long agreeInd;

    @NotBlank(message = "ConsentGiverId can't be Blank")
    @JsonProperty("ConsentGiverId")
    private String consentGiverId;

    @NotBlank(message = "BranchCode can't be Blank")
    @JsonProperty("BranchCode")
    private String branchCode;

    @NotNull(message = "ConsentEntityAssocs can't be Null")
    @NotEmpty(message = "ConsentEntityAssocs can't be Empty")
    @JsonProperty("ConsentEntityAssocs")
    @Valid
    @ToString.Exclude
    private List<ConsentEntityAssocDto> consentEntityAssocs;
}
