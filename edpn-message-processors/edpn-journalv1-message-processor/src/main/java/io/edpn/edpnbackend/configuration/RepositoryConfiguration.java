package io.edpn.edpnbackend.configuration;

import io.edpn.edpnbackend.domain.repository.JournalRepository;
import io.edpn.edpnbackend.domain.repository.SchemaLatestTimestampRepository;
import io.edpn.edpnbackend.infrastructure.persistence.mappers.JournalEntityMapper;
import io.edpn.edpnbackend.infrastructure.persistence.mappers.SchemaLatestTimestampEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public JournalRepository journalRepository(JournalEntityMapper journalEntityMapper) {
        return new io.edpn.edpnbackend.infrastructure.persistence.repository.JournalRepository(journalEntityMapper);
    }

    @Bean
    public SchemaLatestTimestampRepository schemaLatestTimestampRepository(SchemaLatestTimestampEntityMapper schemaLatestTimestampEntityMapper) {
        return new io.edpn.edpnbackend.infrastructure.persistence.repository.SchemaLatestTimestampRepository(schemaLatestTimestampEntityMapper);
    }
}
