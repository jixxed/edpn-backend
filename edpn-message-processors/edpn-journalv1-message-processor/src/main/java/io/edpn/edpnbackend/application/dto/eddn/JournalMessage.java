package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.edpn.backend.messageprocessor.application.dto.eddn.Common;
import io.edpn.backend.messageprocessor.application.dto.eddn.withMessageTimestamp;
import io.edpn.backend.messageprocessor.domain.util.TimestampConverter;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface JournalMessage {
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
            @JsonProperty("timestamp")
            private String timestamp;
            @JsonProperty("event")
            private String event;
            @JsonProperty("horizons")
            private Boolean horizons;
            @JsonProperty("odyssey")
            private Boolean odyssey;
            @JsonProperty("StarSystem")
            private String starSystem;
            @JsonProperty("StarPos")
            private Double[] starPos;
            @JsonProperty("SystemAddress")
            private Long systemAddress;
        }
    }
}
