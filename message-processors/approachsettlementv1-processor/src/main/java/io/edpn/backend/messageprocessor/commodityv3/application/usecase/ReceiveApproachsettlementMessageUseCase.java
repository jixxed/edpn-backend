package io.edpn.backend.messageprocessor.commodityv3.application.usecase;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.eddn.ApproachsettlementMessage;

public interface ReceiveApproachsettlementMessageUseCase {

    void receive(ApproachsettlementMessage.V1 message);
}
