package io.eddb.eddb2backend.domain.model;

import lombok.Builder;

@Builder
public record Faction(Long id, String name) {
}