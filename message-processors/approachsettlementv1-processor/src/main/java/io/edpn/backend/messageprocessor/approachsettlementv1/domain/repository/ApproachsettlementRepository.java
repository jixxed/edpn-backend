package io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository;

import io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.persistence.ApproachsettlementEntity;

import java.util.Optional;
import java.util.UUID;

public interface ApproachsettlementRepository {
    ApproachsettlementEntity findOrCreateByName(String name);

    ApproachsettlementEntity update(ApproachsettlementEntity entity);

    ApproachsettlementEntity create(ApproachsettlementEntity entity);

    Optional<ApproachsettlementEntity> findById(UUID id);
}
