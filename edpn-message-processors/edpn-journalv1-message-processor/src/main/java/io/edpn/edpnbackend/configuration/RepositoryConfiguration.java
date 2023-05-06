package io.edpn.edpnbackend.configuration;

import io.edpn.edpnbackend.domain.repository.*;
import io.edpn.edpnbackend.infrastructure.persistence.mappers.JournalEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public JournalRepository journalRepository(JournalEntityMapper journalEntityMapper) {
        return new io.edpn.edpnbackend.infrastructure.persistence.repository.JournalRepository(journalEntityMapper);
    }
}
