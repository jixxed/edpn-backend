package io.edpn.backend.messageprocessor.approachsettlementv1.application.usecase;

import io.edpn.backend.messageprocessor.approachsettlementv1.application.dto.eddn.ApproachsettlementMessage;

public interface ReceiveApproachsettlementMessageUseCase {

    void receive(ApproachsettlementMessage.V1 message);
}