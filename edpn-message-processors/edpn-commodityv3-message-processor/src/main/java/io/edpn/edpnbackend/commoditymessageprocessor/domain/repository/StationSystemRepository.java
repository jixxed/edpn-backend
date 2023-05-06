package io.edpn.edpnbackend.commoditymessageprocessor.domain.repository;

import io.edpn.edpnbackend.commoditymessageprocessor.application.dto.persistence.StationSystemEntity;

import java.util.Optional;
import java.util.UUID;

public interface StationSystemRepository {
    StationSystemEntity update(StationSystemEntity entity);

    StationSystemEntity create(StationSystemEntity entity);

    Optional<StationSystemEntity> findById(UUID id);

    void deleteById(UUID id);
}
