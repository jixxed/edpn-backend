package io.edpn.edpnbackend.application.dto.persistence;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EconomyEntity {

    private UUID id;
    private String name;

}


