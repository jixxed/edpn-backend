package io.edpn.backend.messageprocessor.commodityv3.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.messageprocessor.commodityv3.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.commodityv3.infrastructure.kafka.processor.ApproachsettlementV1MessageProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EddnMessagesProcessorBeanConfig {
    @Bean
    public ApproachsettlementV1MessageProcessor approachsettlementV1MessageProcessor(ReceiveApproachsettlementMessageUseCase receiveApproachsettlementMessageUsecase, ObjectMapper objectMapper) {
        return new ApproachsettlementV1MessageProcessor(receiveApproachsettlementMessageUsecase, objectMapper);
    }
}
