package io.edpn.backend.rest.domain.model.station;

import lombok.Builder;

@Builder
public record Type(Long id, String name) {
}