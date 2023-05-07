package io.edpn.backend.messageprocessor.approachsettlementv1.infrastructure.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.eddn.ApproachsettlementMessage;
import io.edpn.backend.messageprocessor.approachsettlementv1.application.usecase.ReceiveApproachsettlementMessageUseCase;
import io.edpn.backend.messageprocessor.infrastructure.kafka.processor.EddnMessageProcessor;

import java.util.concurrent.Semaphore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class ApproachsettlementV1MessageProcessor implements EddnMessageProcessor<ApproachsettlementMessage.V1> {

    private final ReceiveApproachsettlementMessageUseCase receiveApproachsettlementMessageUsecase;
    private final ObjectMapper objectMapper;
    private final Semaphore semaphore = new Semaphore(1); // Change the number of permits if needed

    @Override
    @KafkaListener(topics = "https___eddn.edcd.io_schemas_approachsettlement_1", groupId = "approachsettlement", containerFactory = "eddnApproachsettlementKafkaListenerContainerFactory")
    public void listen(JsonNode json) throws JsonProcessingException, InterruptedException {
        semaphore.acquire(); // Acquire a permit before processing the message
        try {
            handle(processJson(json));
        } finally {
            semaphore.release(); // Release the permit after processing is complete
        }
    }

    @Override
    public void handle(ApproachsettlementMessage.V1 message) {
        receiveApproachsettlementMessageUsecase.receive(message);
    }

    @Override
    public ApproachsettlementMessage.V1 processJson(JsonNode json) throws JsonProcessingException {
        return objectMapper.treeToValue(json, ApproachsettlementMessage.V1.class);
    }
}
