package io.edpn.edpnbackend.infrastructure.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.edpn.backend.messageprocessor.infrastructure.kafka.processor.EddnMessageProcessor;
import io.edpn.edpnbackend.application.dto.eddn.JournalMessage;
import io.edpn.edpnbackend.application.usecase.ReceiveJournalMessageUseCase;

import java.util.concurrent.Semaphore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
public class JournalV1MessageProcessor implements EddnMessageProcessor<JournalMessage.V1> {

    private final ReceiveJournalMessageUseCase receiveJournalMessageUsecase;
    private final ObjectMapper objectMapper;
    private final Semaphore semaphore = new Semaphore(1); // Change the number of permits if needed

    @Override
    @KafkaListener(topics = "https___eddn.edcd.io_schemas_journal_1", groupId = "journal", containerFactory = "eddnJournalKafkaListenerContainerFactory")
    public void listen(JsonNode json) throws JsonProcessingException, InterruptedException {
        semaphore.acquire(); // Acquire a permit before processing the message
        try {
            handle(processJson(json));
        } catch (UnrecognizedPropertyException ex)
        {
            String[] split = ex.getMessage().split(" ");
            String replaced = split[2].replace("\"", "");

            System.out.println("Unable to parse " + replaced + " in " + json.get("message").get("event").asText() + " event");
        } catch (Exception ex)
        {
            String message = ex.getMessage();
            System.out.println(message);
        }
        finally {
            semaphore.release(); // Release the permit after processing is complete
        }
    }

    @Override
    public void handle(JournalMessage.V1 message) {
        receiveJournalMessageUsecase.receive(message);
    }

    @Override
    public JournalMessage.V1 processJson(JsonNode json) throws JsonProcessingException {
        return objectMapper.treeToValue(json, JournalMessage.V1.class);
    }
}
