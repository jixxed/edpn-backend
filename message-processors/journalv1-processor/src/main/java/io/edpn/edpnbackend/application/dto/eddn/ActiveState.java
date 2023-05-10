package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActiveState {
    @JsonProperty("State")
    private String state;
    @JsonProperty("Trend")
    private String trend;
}
