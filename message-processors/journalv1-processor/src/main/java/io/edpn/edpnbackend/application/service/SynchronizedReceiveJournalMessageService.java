package io.edpn.edpnbackend.application.service;

import io.edpn.edpnbackend.application.dto.eddn.JournalMessage;
import io.edpn.edpnbackend.application.dto.persistence.*;
import io.edpn.edpnbackend.application.usecase.ReceiveJournalMessageUseCase;
import io.edpn.edpnbackend.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.edpn.backend.messageprocessor.domain.util.CollectionUtil.toList;

@RequiredArgsConstructor
public class SynchronizedReceiveJournalMessageService implements ReceiveJournalMessageUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedReceiveJournalMessageService.class);

    private final JournalRepository journalRepository;
    private final SchemaLatestTimestampRepository schemaLatestTimestampRepository;

    @Override
    @Transactional
    public synchronized void receive(JournalMessage.V1 journalMessage) {
        long start = System.nanoTime();
        LOGGER.debug("ReceiveJournalMessageService.receive -> journalMessage: " + journalMessage);
    }
}
