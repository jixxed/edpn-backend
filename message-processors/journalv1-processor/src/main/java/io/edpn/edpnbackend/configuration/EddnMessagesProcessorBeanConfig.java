package io.edpn.edpnbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.edpnbackend.application.usecase.ReceiveJournalMessageUseCase;
import io.edpn.edpnbackend.infrastructure.kafka.processor.JournalV1MessageProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EddnMessagesProcessorBeanConfig {
    @Bean
    public JournalV1MessageProcessor journalV1MessageProcessor(ReceiveJournalMessageUseCase receiveJournalMessageUsecase, ObjectMapper objectMapper) {
        return new JournalV1MessageProcessor(receiveJournalMessageUsecase, objectMapper);
    }
}
