package com.mdm.consent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class AddConsentEntityAssocRequest {

    @NotNull(message = "ConsentId can't be Null")
    @JsonProperty("ConsentId")
    private Long consentId;

    @NotBlank(message = "ConsentGiverId can't be Blank")
    @JsonProperty("ConsentGiverId")
    private String consentGiverId;

    @NotNull(message = "ConsentEntityAssocs can't be Null")
    @NotEmpty(message = "ConsentEntityAssocs can't be Empty")
    @JsonProperty("ConsentEntityAssocs")
    @Valid
    private List<ConsentEntityAssocDto> consentEntityAssocs;
}
