package io.edpn.backend.messageprocessor.approachsettlementv1.application.service;

import io.edpn.backend.messageprocessor.approachsettlementv1.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.approachsettlementv1.domain.repository.ApproachsettlementRepository;
import io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.eddn.ApproachsettlementMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class SynchronizedReceiveApproachsettlementMessageService implements ReceiveApproachsettlementMessageUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedReceiveApproachsettlementMessageService.class);

    private final ApproachsettlementRepository approachsettlementRepository;

    @Override
    @Transactional
    public synchronized void receive(ApproachsettlementMessage.V1 approachsettlementMessage) {
        long start = System.nanoTime();
        LOGGER.debug("ReceiveApproachsettlementMessageService.receive -> approachsettlementMessage: " + approachsettlementMessage);

        var updateTimestamp = approachsettlementMessage.getMessageTimeStamp();
        String schemaRef = approachsettlementMessage.getSchemaRef();

        //check if we should skip
        boolean isLatest;
        isLatest = isLatestMessageAndUpdateTimestamp(updateTimestamp, schemaRef);

        if (!isLatest) {
            LOGGER.info("ReceiveApproachsettlementMessageService.receive -> the message is not newer than what we already processed, skipping");
            LOGGER.trace("ReceiveApproachsettlementMessageService.receive -> took " + (System.nanoTime() - start) + " nanosecond");
            return;
        }
        LOGGER.info("ReceiveApproachsettlementMessageService.receive -> the message is newer than what we already processed, starting processing");

        LOGGER.debug("ReceiveApproachsettlementMessageService.receive -> station: " + station);
        LOGGER.info("ReceiveApproachsettlementMessageService.receive -> the message is has been processed");
        LOGGER.trace("ReceiveApproachsettlementMessageService.receive -> took " + (System.nanoTime() - start) + " nanosecond");
    }

    private boolean isLatestMessageAndUpdateTimestamp(LocalDateTime updateTimestamp, String schemaRef) {
        boolean isLatest;
        if (!schemaLatestTimestampRepository.isAfterLatest(schemaRef, updateTimestamp)) {
            isLatest = false; // the message is not newer than what we already processed
        } else {
            // the message is newer than what we already processed, update ore create the timestamp
            schemaLatestTimestampRepository.createOrUpdate(SchemaLatestTimestampEntity.builder()
                    .schema(schemaRef)
                    .timestamp(updateTimestamp)
                    .build());
            isLatest = true;
        }
        return isLatest;
    }
}
