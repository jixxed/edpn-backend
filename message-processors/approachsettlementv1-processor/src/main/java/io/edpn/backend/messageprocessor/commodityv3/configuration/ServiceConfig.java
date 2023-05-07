package io.edpn.backend.messageprocessor.commodityv3.configuration;

import io.edpn.backend.messageprocessor.commodityv3.application.service.SynchronizedReceiveApproachsettlementMessageService;
import io.edpn.backend.messageprocessor.commodityv3.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.commodityv3.domain.repository.*;
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
