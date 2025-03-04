package io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StationEntity {

    private UUID id;
    private String name;
    private Long edMarketId;
    private UUID StationTypeId;
    private LocalDateTime marketUpdatedAt;
    private boolean hasCommodities;
    private UUID systemId;
}
