package io.edpn.backend.messageprocessor.approachsettlementv1.configuration;

import io.edpn.backend.messageprocessor.approachsettlementv1.application.service.SynchronizedReceiveApproachsettlementMessageService;
import io.edpn.backend.messageprocessor.approachsettlementv1.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository.ApproachsettlementRepository;
import io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository.SchemaLatestTimestampRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ReceiveApproachsettlementMessageUseCase receiveApproachsettlementMessageUsecase(
            ApproachsettlementRepository approachsettlementRepository,
            SchemaLatestTimestampRepository schemaLatestTimestampRepository) {
        return new SynchronizedReceiveApproachsettlementMessageService(approachsettlementRepository, schemaLatestTimestampRepository);
    }
}
