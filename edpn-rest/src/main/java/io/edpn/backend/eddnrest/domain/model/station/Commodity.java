package io.edpn.backend.eddnrest.domain.model.station;

import lombok.Builder;

@Builder
public record Commodity(Long id, String name){
}