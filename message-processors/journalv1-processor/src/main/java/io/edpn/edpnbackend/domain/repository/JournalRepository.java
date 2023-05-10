package io.edpn.edpnbackend.domain.repository;

import io.edpn.edpnbackend.application.dto.persistence.JournalEntity;

import java.util.Optional;
import java.util.UUID;

public interface JournalRepository {
    JournalEntity findOrCreateByName(String name);

    JournalEntity update(JournalEntity entity);

    JournalEntity create(JournalEntity entity);

    Optional<JournalEntity> findById(UUID id);
}
