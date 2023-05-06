package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtmosphereComposition {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Percent")
    private double percent;
}
