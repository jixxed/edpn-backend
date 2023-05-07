package io.edpn.backend.messageprocessor.commodityv3.domain.repository;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.SchemaLatestTimestampEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface SchemaLatestTimestampRepository {
    Collection<SchemaLatestTimestampEntity> findAll();

    SchemaLatestTimestampEntity createOrUpdate(SchemaLatestTimestampEntity entity);

    Optional<SchemaLatestTimestampEntity> findBySchema(String Schema);

    boolean isAfterLatest(String schema, LocalDateTime timestamp);
}
