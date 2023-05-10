package io.edpn.edpnbackend.configuration;

import io.edpn.edpnbackend.application.service.SynchronizedReceiveJournalMessageService;
import io.edpn.edpnbackend.application.usecase.ReceiveJournalMessageUseCase;
import io.edpn.edpnbackend.domain.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ReceiveJournalMessageUseCase receiveCommodityMessageUsecase(
            JournalRepository journalRepository,
            SchemaLatestTimestampRepository schemaLatestTimestampRepository) {
        return new SynchronizedReceiveJournalMessageService(journalRepository, schemaLatestTimestampRepository);
    }
}
