package io.edpn.backend.messageprocessor.infrastructure.kafka.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.edpn.backend.messageprocessor.application.dto.eddn.withMessageTimestamp;

public interface EddnMessageProcessor<T extends withMessageTimestamp> {

    void listen(JsonNode json) throws JsonProcessingException, InterruptedException;

    void handle(T message);


    T processJson(JsonNode json) throws JsonProcessingException;
}
