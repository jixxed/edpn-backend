package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Parent {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Type")
    private String type;
}
