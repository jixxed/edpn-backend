package io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.ApproachsettlementEntity;
import io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.mappers.ApproachsettlementEntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ApproachsettlementRepository implements io.edpn.backend.messageprocessor.commodityv3.domain.repository.ApproachsettlementRepository {

    private final ApproachsettlementEntityMapper approachsettlementEntityMapper;

    @Override
    public ApproachsettlementEntity findOrCreateByName(String name) {
        return approachsettlementEntityMapper.findByName(name)
                .orElseGet(() -> {
                    ApproachsettlementEntity s = ApproachsettlementEntity.builder()
                            .name(name)
                            .build();
                    return create(s);
                });
    }

    @Override
    public ApproachsettlementEntity update(ApproachsettlementEntity approachsettlement) {
        approachsettlementEntityMapper.update(approachsettlement);
        return findById(approachsettlement.getId())
                .orElseThrow(() -> new RuntimeException("approachsettlement with id: " + approachsettlement.getId() + " could not be found after update"));
    }

    @Override
    public ApproachsettlementEntity create(ApproachsettlementEntity entity) {
        entity.setId(UUID.randomUUID());
        approachsettlementEntityMapper.insert(entity);
        return findById(entity.getId())
                .orElseThrow(() -> new RuntimeException("approachsettlement with id: " + entity.getId() + " could not be found after create"));
    }

    @Override
    public Optional<ApproachsettlementEntity> findById(UUID id) {
        return approachsettlementEntityMapper.findById(id);
    }
}
