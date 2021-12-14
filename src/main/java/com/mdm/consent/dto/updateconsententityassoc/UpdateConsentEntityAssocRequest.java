package com.mdm.consent.dto.updateconsententityassoc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mdm.consent.dto.addconsententityassoc.AddConsentEntityAssocRequest;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@ToString
public class UpdateConsentEntityAssocRequest {

    @NotNull(message = "ConsentEntityAssocId can't be Null")
    @JsonProperty("ConsentEntityAssocId")
    private Long consentEntityAssocId;
}