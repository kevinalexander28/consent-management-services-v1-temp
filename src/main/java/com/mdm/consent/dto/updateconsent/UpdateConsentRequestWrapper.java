package com.mdm.consent.dto.updateconsent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateConsentRequestWrapper {

    @NotNull(message = "Consent can't be Null")
    @JsonProperty("Consent")
    @Valid
    private UpdateConsentRequest consent;
}
