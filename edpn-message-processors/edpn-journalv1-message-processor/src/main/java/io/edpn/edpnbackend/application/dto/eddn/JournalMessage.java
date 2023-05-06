package io.edpn.edpnbackend.application.dto.eddn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.edpn.backend.eddnrest.domain.model.common.Faction;
import io.edpn.backend.messageprocessor.application.dto.eddn.Common;
import io.edpn.backend.messageprocessor.application.dto.eddn.withMessageTimestamp;
import io.edpn.backend.messageprocessor.domain.util.TimestampConverter;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.integration.annotation.Default;

public interface JournalMessage {
    @Data
    @NoArgsConstructor
    class V1 implements withMessageTimestamp {
        @JsonProperty("$schemaRef")
        public String schemaRef;
        @JsonProperty("header")
        public Common.EddnMessageHeader header;
        @JsonProperty("message")
        public Message message;

        @Override
        public LocalDateTime getMessageTimeStamp() {
            return TimestampConverter.convertToLocalDateTime(message.getTimestamp());
        }

        @Data
        @NoArgsConstructor
        public static class Message {
            @JsonProperty("timestamp")
            public String timestamp;
            @JsonProperty("event")
            public String event;
            @JsonProperty("horizons")
            public Boolean horizons;
            @JsonProperty("odyssey")
            public Boolean odyssey;
            @JsonProperty("StarSystem")
            public String starSystem;
            @JsonProperty("StarPos")
            public Double[] starPos;
            @JsonProperty("SystemAddress")
            public Long systemAddress;
            @JsonProperty("BodyID")
            public Long bodyID;
            @JsonProperty("Body")
            public String body;
            @JsonProperty("DistFromStarLS")
            public Double distFromStarLS;
            @JsonProperty("BodyName")
            public String bodyName;
            @JsonProperty("AscendingNode")
            public Double ascendingNode;
            @JsonProperty("Atmosphere")
            public String atmosphere;
            @JsonProperty("AxialTilt")
            public Double axialTilt;
            @JsonProperty("BodyType")
            public String bodyType;
            @JsonProperty("Carbon")
            public Boolean carbon;
            @JsonProperty("DistanceFromArrivalLS")
            public Double distanceFromArrivalLS;
            @JsonProperty("Eccentricity")
            public Double eccentricity;
            @JsonProperty("Landable")
            public Boolean landable;
            @JsonProperty("Luminosity")
            public String luminosity;
            @JsonProperty("MassEM")
            public Double massEM;
            @JsonProperty("MassMT")
            public Double massMT;
            @JsonProperty("OrbitalPeriod")
            public Double orbitalPeriod;
            @JsonProperty("Periapsis")
            public Double periapsis;
            @JsonProperty("PlanetClass")
            public String planetClass;
            @JsonProperty("Radius")
            public Double radius;
            @JsonProperty("RotationPeriod")
            public Double rotationPeriod;
            @JsonProperty("SemiMajorAxis")
            public Double semiMajorAxis;
            @JsonProperty("StarType")
            public String starType;
            @JsonProperty("StellarMass")
            public Double stellarMass;
            @JsonProperty("SurfaceGravity")
            public Double surfaceGravity;
            @JsonProperty("SurfacePressure")
            public Double surfacePressure;
            @JsonProperty("SurfaceTemperature")
            public Double surfaceTemperature;
            @JsonProperty("TerraformState")
            public String terraformState;
            @JsonProperty("TidalLock")
            public Boolean tidalLock;
            @JsonProperty("Volcanism")
            public String volcanism;
            @JsonProperty("WasDiscovered")
            public Boolean wasDiscovered;
            @JsonProperty("WasMapped")
            public Boolean wasMapped;
            @JsonProperty("AtmosphereType")
            public String atmosphereType;
            @JsonProperty("AtmosphereTypeID")
            public Long atmosphereTypeID;
            @JsonProperty("Composition")
            public Composition composition;
            @JsonProperty("AtmosphereComposition")
            public AtmosphereComposition[] atmosphereComposition;
            @JsonProperty("AbsoluteMagnitude")
            public Double absoluteMagnitude;
            @JsonProperty("Parents")
            public Parent[] parents;
            @JsonProperty("Materials")
            public Material[] materials;
            @JsonProperty("Rings")
            public Ring[] rings;
            @JsonProperty("ReserveLevel")
            public String reserveLevel;
            @JsonProperty("Name")
            public String name;
            @JsonProperty("Docked")
            public Boolean docked;
            @JsonProperty("Signals")
            public Signal[] signals;
            @JsonProperty("MeanAnomaly")
            public Double meanAnomaly;
            @JsonProperty("Genuses")
            public Genus[] genuses;
            @JsonProperty("Factions")
            public Faction[] factions;
            @JsonProperty("Population")
            public Long population;
//            @JsonProperty("Conflicts")
//            public Conflict[] conflicts;
            @JsonProperty("Ice")
            public Double ice;
            @JsonProperty("Age_MY")
            public Double ageMY;
            @JsonProperty("OnFoot")
            public Boolean onFoot;
            @JsonProperty("ActiveStates")
            public ActiveState[] activeStates;
            @JsonProperty("ScanType")
            public String scanType;
            @JsonProperty("MarketID")
            public Long marketID;
            @JsonProperty("Allegiance")
            public String allegiance;
        }
    }
}
