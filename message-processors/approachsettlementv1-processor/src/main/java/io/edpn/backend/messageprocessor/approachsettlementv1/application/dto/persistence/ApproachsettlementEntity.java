package io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.persistence;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApproachsettlementEntity {

    private UUID id;
    private String name;
}



