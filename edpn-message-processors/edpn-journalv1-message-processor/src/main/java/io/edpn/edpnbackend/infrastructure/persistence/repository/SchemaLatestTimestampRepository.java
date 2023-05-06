package io.edpn.edpnbackend.infrastructure.persistence.repository;


import io.edpn.edpnbackend.application.dto.persistence.SchemaLatestTimestampEntity;
import io.edpn.edpnbackend.infrastructure.persistence.mappers.SchemaLatestTimestampEntityMapper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;


@RequiredArgsConstructor
public class SchemaLatestTimestampRepository implements io.edpn.edpnbackend.domain.repository.SchemaLatestTimestampRepository {

    private final SchemaLatestTimestampEntityMapper schemaLatestTimestampEntityMapper;

    @Override
    public Collection<SchemaLatestTimestampEntity> findAll() {
        return schemaLatestTimestampEntityMapper.findAll();
    }

    @Override
    public SchemaLatestTimestampEntity createOrUpdate(SchemaLatestTimestampEntity entity) {
        if (schemaLatestTimestampEntityMapper.findBySchema(entity.getSchema()).isPresent()) {
            schemaLatestTimestampEntityMapper.update(entity);
        } else {
            schemaLatestTimestampEntityMapper.insert(entity);
        }
        return schemaLatestTimestampEntityMapper.findBySchema(entity.getSchema())
                .orElseThrow(() -> new RuntimeException("SchemaLatestTimestamp with schema: " + entity.getSchema() + " could not be found after createOrUpdate"));
    }

    @Override
    public Optional<SchemaLatestTimestampEntity> findBySchema(String Schema) {
        return schemaLatestTimestampEntityMapper.findBySchema(Schema);
    }

    @Override
    public boolean isAfterLatest(String schema, LocalDateTime timestamp) {
        return schemaLatestTimestampEntityMapper.findBySchema(schema)
                .map(entity -> entity.getTimestamp().isBefore(timestamp))
                .orElse(true);
    }
}