package io.edpn.edpnbackend.application.usecase;

import io.edpn.edpnbackend.application.dto.eddn.JournalMessage;

public interface ReceiveJournalMessageUseCase {

    void receive(JournalMessage.V1 message);
}
