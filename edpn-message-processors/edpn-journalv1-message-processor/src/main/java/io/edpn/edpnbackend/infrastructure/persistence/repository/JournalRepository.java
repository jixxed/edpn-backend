package io.edpn.edpnbackend.infrastructure.persistence.repository;

import io.edpn.edpnbackend.application.dto.persistence.JournalEntity;
import io.edpn.edpnbackend.infrastructure.persistence.mappers.JournalEntityMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JournalRepository implements io.edpn.edpnbackend.domain.repository.JournalRepository {

    private final JournalEntityMapper journalEntityMapper;

    @Override
    public JournalEntity findOrCreateByName(String name) {
        return journalEntityMapper.findByName(name)
                .orElseGet(() -> {
                    JournalEntity s = JournalEntity.builder()
                            .name(name)
                            .build();
                    return create(s);
                });
    }

    @Override
    public JournalEntity update(JournalEntity journal) {
        journalEntityMapper.update(journal);
        return findById(journal.getId())
                .orElseThrow(() -> new RuntimeException("journal with id: " + journal.getId() + " could not be found after update"));
    }

    @Override
    public JournalEntity create(JournalEntity entity) {
        entity.setId(UUID.randomUUID());
        journalEntityMapper.insert(entity);
        return findById(entity.getId())
                .orElseThrow(() -> new RuntimeException("commodity with id: " + entity.getId() + " could not be found after create"));
    }

    @Override
    public Optional<JournalEntity> findById(UUID id) {
        return journalEntityMapper.findById(id);
    }
}
