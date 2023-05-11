package io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.edpn.backend.messageprocessor.application.dto.eddn.Common;
import io.edpn.backend.messageprocessor.application.dto.eddn.withMessageTimestamp;
import io.edpn.backend.messageprocessor.domain.util.TimestampConverter;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface ApproachsettlementMessage {
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
            private boolean horizons;
            @JsonProperty("odyssey")
            private boolean odyssey;
            @JsonProperty("StarSystem")
            private String starSystem;
            @JsonProperty("StarPos")
            private double[] starPos;
            @JsonProperty("SystemAddress")
            private long systemAddress;
            @JsonProperty("Name")
            private String name;
            @JsonProperty("MarketID")
            private long marketID;
            @JsonProperty("BodyID")
            private long bodyID;
            @JsonProperty("BodyName")
            private String bodyName;
            @JsonProperty("Latitude")
            private double latitude;
            @JsonProperty("Longitude")
            private double longitude;
        }
    }
}
