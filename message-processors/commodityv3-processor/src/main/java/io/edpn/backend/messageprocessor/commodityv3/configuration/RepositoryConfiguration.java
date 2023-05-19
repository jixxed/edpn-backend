package io.edpn.backend.messageprocessor.commodityv3.configuration;

import io.edpn.backend.messageprocessor.commodityv3.domain.repository.*;
import io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.mappers.*;
import io.edpn.backend.messageprocessor.domain.util.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CommodityRepository commodityRepository(IdGenerator idGenerator, CommodityEntityMapper commodityEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.CommodityRepository(idGenerator, commodityEntityMapper);
    }

    @Bean
    public EconomyRepository economyRepository(IdGenerator idGenerator, EconomyEntityMapper economyEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.EconomyRepository(idGenerator, economyEntityMapper);
    }

    @Bean
    public HistoricStationCommodityMarketDatumRepository historicStationCommodityRepository(IdGenerator idGenerator, HistoricStationCommodityMarketDatumEntityMapper historicStationCommodityMarketDatumEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.HistoricStationCommodityMarketDatumRepository(idGenerator, historicStationCommodityMarketDatumEntityMapper);
    }

    @Bean
    public StationEconomyProportionRepository stationEconomyProportionRepository(StationEconomyProportionEntityMapper stationEconomyProportionEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.StationEconomyProportionRepository(stationEconomyProportionEntityMapper);
    }

    @Bean
    public StationRepository stationRepository(IdGenerator idGenerator, StationEntityMapper stationEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.StationRepository(idGenerator, stationEntityMapper);
    }

    @Bean
    public SystemRepository systemRepository(IdGenerator idGenerator, SystemEntityMapper systemEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.SystemRepository(idGenerator, systemEntityMapper);
    }

    @Bean
    public StationProhibitedCommodityRepository stationProhibitedCommodityRepository(StationProhibitedCommodityEntityMapper stationProhibitedCommodityEntityMapper) {
        return new io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.repository.StationProhibitedCommodityRepository(stationProhibitedCommodityEntityMapper);
    }
}
