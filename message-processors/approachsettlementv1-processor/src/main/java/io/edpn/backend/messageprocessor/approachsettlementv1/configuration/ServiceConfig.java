package io.edpn.backend.messageprocessor.approachsettlementv1.configuration;

import io.edpn.backend.messageprocessor.approachsettlementv1.application.service.SynchronizedReceiveApproachsettlementMessageService;
import io.edpn.backend.messageprocessor.approachsettlementv1.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository.ApproachsettlementRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ReceiveApproachsettlementMessageUseCase receiveApproachsettlementMessageUsecase(
            ApproachsettlementRepository approachsettlementRepository) {
        return new SynchronizedReceiveApproachsettlementMessageService(approachsettlementRepository);
    }
}
