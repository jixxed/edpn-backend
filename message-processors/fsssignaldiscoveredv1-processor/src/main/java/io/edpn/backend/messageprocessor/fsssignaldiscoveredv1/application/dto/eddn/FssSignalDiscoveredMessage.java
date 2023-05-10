package io.edpn.backend.messageprocessor.fsssignaldiscoveredv1.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.edpn.backend.messageprocessor.application.dto.eddn.Common;
import io.edpn.backend.messageprocessor.application.dto.eddn.withMessageTimestamp;
import io.edpn.backend.messageprocessor.domain.util.TimestampConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public interface FssSignalDiscoveredMessage {
    @Data
    @NoArgsConstructor
    class V1 implements withMessageTimestamp {
        @JsonProperty("$schemaRef")
        private String schemaRef;
        @JsonProperty("header")
        private Common.EddnMessageHeader header;
        @JsonProperty("message")
        private Message message;

        @Override
        public LocalDateTime getMessageTimeStamp() {
            return TimestampConverter.convertToLocalDateTime(message.getTimestamp());
        }

        @Data
        @NoArgsConstructor
        public static class Message {
            @JsonProperty("StarPos")
            private Double[] starPos;
            @JsonProperty("StarSystem")
            private String starSystem;
            @JsonProperty("SystemAddress")
            private Long systemAddress;
            @JsonProperty("event")
            private String event;
            @JsonProperty("horizons")
            private boolean horizons;
            @JsonProperty("odyssey")
            private boolean odyssey;
            @JsonProperty("signals")
            private List<Signal> signals;
            @JsonProperty("timestamp")
            private String timestamp;
        }

        @Data
        @NoArgsConstructor
        public static class Signal {
            @JsonProperty("IsStation")
            private String isStation;
            @JsonProperty("SignalName")
            private String signalName;
            @JsonProperty("SpawningFaction")
            private String spawningFaction;
            @JsonProperty("SpawningState")
            private String spawningState;
            @JsonProperty("ThreatLevel")
            private int threatLevel;
            @JsonProperty("USSType")
            private String ussType;
            @JsonProperty("timestamp")
            private String timestamp;

        }
    }
}