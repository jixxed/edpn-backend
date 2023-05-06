package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ring {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("RingClass")
    private String type;
    @JsonProperty("MassMT")
    private String massMT;
    @JsonProperty("InnerRad")
    private String innerRad;
    @JsonProperty("OuterRad")
    private String outerRad;
    @JsonProperty("id")
    private String id;
}
