package com.mdm.consent.dto.deleteconsententityassoc;

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
public class DeleteConsentEntityAssocRequestWrapper {

    @NotNull(message = "ConsentEntityAssoc can't be Null")
    @Valid
    @JsonProperty("ConsentEntityAssoc")
    private DeleteConsentEntityAssocRequest consentEntityAssoc;
}
