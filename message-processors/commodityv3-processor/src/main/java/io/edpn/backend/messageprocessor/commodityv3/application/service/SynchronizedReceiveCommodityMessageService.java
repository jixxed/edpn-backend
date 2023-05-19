package io.edpn.backend.messageprocessor.commodityv3.application.service;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.eddn.CommodityMessage;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.CommodityEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.HistoricStationCommodityMarketDatumEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.StationEconomyProportionEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.StationEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.usecase.ReceiveCommodityMessageUseCase;
import io.edpn.backend.messageprocessor.commodityv3.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static io.edpn.backend.messageprocessor.domain.util.CollectionUtil.toList;

@RequiredArgsConstructor
@Slf4j
public class SynchronizedReceiveCommodityMessageService implements ReceiveCommodityMessageUseCase {
    private final SystemRepository systemRepository;
    private final StationRepository stationRepository;
    private final CommodityRepository commodityRepository;
    private final EconomyRepository economyRepository;
    private final HistoricStationCommodityMarketDatumRepository historicStationCommodityMarketDatumRepository;
    private final StationProhibitedCommodityRepository stationProhibitedCommodityRepository;
    private final StationEconomyProportionRepository stationEconomyProportionRepository;

    @Override
    @Transactional
    public synchronized void receive(CommodityMessage.V3 commodityMessage) {
        long start = System.nanoTime();
        if (log.isDebugEnabled()) {
            log.debug("ReceiveCommodityMessageService.receive -> commodityMessage: " + commodityMessage);
        }

        var updateTimestamp = commodityMessage.getMessageTimeStamp();

        CommodityMessage.V3.Message payload = commodityMessage.getMessage();
        CommodityMessage.V3.Commodity[] commodities = payload.getCommodities();
        CommodityMessage.V3.Economy[] economies = payload.getEconomies();
        long marketId = payload.getMarketId();
        String systemName = payload.getSystemName();
        String stationName = payload.getStationName();
        String[] prohibitedCommodities = payload.getProhibited();


        // find system, if not found create
        var system = systemRepository.findOrCreateByName(systemName);

        // find station, if not found create
        var station = stationRepository.findByMarketId(marketId).orElseGet(() -> {
            var tempStation = stationRepository.findOrCreateBySystemIdAndStationName(system.getId(), stationName);
            tempStation.setEdMarketId(marketId);
            return tempStation;
        });

        // update station
        station.setMarketUpdatedAt(updateTimestamp);
        station.setHasCommodities(true);
        stationRepository.update(station);

        //remove old prohibited commodities and save new
        Collection<UUID> prohibitedCommodityIds = getProhibitedCommodityIds(prohibitedCommodities);
        stationProhibitedCommodityRepository.deleteByStationId(station.getId());
        stationProhibitedCommodityRepository.insert(station.getId(), prohibitedCommodityIds);

        //remove old economyProportions and save new
        List<StationEconomyProportionEntity> stationEconomyProportionEntities = getEconomyEntityIdProportions(station.getId(), economies);
        stationEconomyProportionRepository.deleteByStationId(station.getId());
        stationEconomyProportionRepository.insert(stationEconomyProportionEntities);

        //save market data
        saveCommodityMarketData(updateTimestamp, commodities, station);

        if (log.isDebugEnabled()) {
            log.debug("ReceiveCommodityMessageService.receive -> station: " + station);
        }
        if (log.isTraceEnabled()) {
            log.trace("ReceiveCommodityMessageService.receive -> took " + (System.nanoTime() - start) + " nanosecond");
        }

        log.info("ReceiveCommodityMessageService.receive -> the message has been processed");
    }

    private void saveCommodityMarketData(LocalDateTime updateTimestamp, CommodityMessage.V3.Commodity[] commodities, StationEntity station) {
        if (Objects.nonNull(commodities)) {
            Arrays.stream(commodities)
                    .forEach(commodity -> {
                        UUID commodityId = commodityRepository.findOrCreateByName(commodity.getName()).getId();
                        UUID stationId = station.getId();

                        if (historicStationCommodityMarketDatumRepository.getByStationIdAndCommodityIdAndTimestamp(stationId, commodityId, updateTimestamp).isEmpty()) {
                            var hsce = HistoricStationCommodityMarketDatumEntity.builder()
                                    .stationId(stationId)
                                    .commodityId(commodityId)
                                    .timestamp(updateTimestamp)
                                    .meanPrice(commodity.getMeanPrice())
                                    .buyPrice(commodity.getBuyPrice())
                                    .sellPrice(commodity.getSellPrice())
                                    .stock(commodity.getStock())
                                    .stockBracket(commodity.getStockBracket())
                                    .demand(commodity.getDemand())
                                    .demandBracket(commodity.getDemandBracket())
                                    .statusFlags(toList(commodity.getStatusFlags()))
                                    .build();

                            historicStationCommodityMarketDatumRepository.create(hsce);
                        }

                        //data cleanup
                        historicStationCommodityMarketDatumRepository.cleanupRedundantData(stationId, commodityId);
                    });
        }
    }

    private List<StationEconomyProportionEntity> getEconomyEntityIdProportions(UUID stationId, CommodityMessage.V3.Economy[] economies) {
        return Optional.ofNullable(economies)
                .map(arr -> Arrays.stream(arr)
                        .map(economy -> {
                            UUID economyId = economyRepository.findOrCreateByName(economy.getName()).getId();
                            double proportion = economy.getProportion();
                            return new StationEconomyProportionEntity(stationId, economyId, proportion);
                        })
                        .toList())
                .orElse(Collections.emptyList());
    }

    private Collection<UUID> getProhibitedCommodityIds(String[] prohibitedCommodities) {
        return Optional.ofNullable(prohibitedCommodities)
                .map(arr -> Arrays.stream(arr)
                        .map(commodityRepository::findOrCreateByName)
                        .map(CommodityEntity::getId)
                        .toList())
                .orElse(Collections.emptyList());
    }
}
