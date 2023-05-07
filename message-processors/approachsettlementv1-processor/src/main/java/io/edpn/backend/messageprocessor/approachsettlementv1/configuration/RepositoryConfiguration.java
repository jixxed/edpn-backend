package io.edpn.backend.messageprocessor.approachsettlementv1.configuration;

import io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository.ApproachsettlementRepository;
import io.edpn.backend.messageprocessor.approachsettlementv1.infrastructure.persistence.mappers.ApproachsettlementEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public ApproachsettlementRepository approachsettlementRepository(ApproachsettlementEntityMapper approachsettlementEntityMapper) {
        return new io.edpn.backend.messageprocessor.approachsettlementv1.infrastructure.persistence.repository.ApproachsettlementRepository(approachsettlementEntityMapper);
    }
}
