package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class AddConsentRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("CIF")
    private String cifId;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("IdType")
    private String idType;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("IdNumber")
    private String idNumber;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("TenantType")
    private long tenantType;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("ProcPurpId")
    private long procPurpId;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("AgreeInd")
    private long agreeInd;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("ConsentGiverId")
    private String consentGiverId;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("BranchCode")
    private String branchCode;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    @JsonProperty("ConsentEntityAssocs")
    @ToString.Exclude
    private List<ConsentEntityAssocDto> consentEntityAssocs;
}
