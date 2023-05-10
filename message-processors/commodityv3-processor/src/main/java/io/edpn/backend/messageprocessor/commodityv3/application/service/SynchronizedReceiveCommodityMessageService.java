package io.edpn.backend.messageprocessor.commodityv3.application.service;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.eddn.CommodityMessage;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.CommodityEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.HistoricStationCommodityMarketDatumEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.StationEntity;
import io.edpn.backend.messageprocessor.commodityv3.application.usecase.ReceiveCommodityMessageUseCase;
import io.edpn.backend.messageprocessor.commodityv3.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.edpn.backend.messageprocessor.domain.util.CollectionUtil.toList;

@RequiredArgsConstructor
public class SynchronizedReceiveCommodityMessageService implements ReceiveCommodityMessageUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedReceiveCommodityMessageService.class);

    private final SystemRepository systemRepository;
    private final StationRepository stationRepository;
    private final CommodityRepository commodityRepository;
    private final EconomyRepository economyRepository;
    private final HistoricStationCommodityMarketDatumRepository historicStationCommodityMarketDatumRepository;

    @Override
    @Transactional
    public synchronized void receive(CommodityMessage.V3 commodityMessage) {
        long start = System.nanoTime();
        LOGGER.debug("ReceiveCommodityMessageService.receive -> commodityMessage: " + commodityMessage);

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
        Collection<UUID> prohibitedCommodityIds = getProhibitedCommodityIds(prohibitedCommodities);
        Map<UUID, Double> economyEntityIdProportionMap = getEconomyEntityIdProportionMap(economies);
        station.setMarketUpdatedAt(updateTimestamp);
        station.setHasCommodities(true);
        station.setProhibitedCommodityIds(prohibitedCommodityIds);
        station.setEconomyEntityIdProportionMap(economyEntityIdProportionMap);
        stationRepository.update(station);

        //save market data
        saveCommodityMarketData(updateTimestamp, commodities, station);

        LOGGER.debug("ReceiveCommodityMessageService.receive -> station: " + station);
        LOGGER.info("ReceiveCommodityMessageService.receive -> the message has been processed");
        LOGGER.trace("ReceiveCommodityMessageService.receive -> took " + (System.nanoTime() - start) + " nanosecond");
    }

    private void saveCommodityMarketData(LocalDateTime updateTimestamp, CommodityMessage.V3.Commodity[] commodities, StationEntity station) {
        if (Objects.nonNull(commodities)) {
            Arrays.stream(commodities)
                    .forEach(commodity -> {
                        UUID commodityId = commodityRepository.findOrCreateByName(commodity.getName()).getId();
                        UUID id = station.getId();

                        if (historicStationCommodityMarketDatumRepository.getByStationIdAndCommodityIdAndTimestamp(id, commodityId, updateTimestamp).isEmpty()) {
                            var hsce = HistoricStationCommodityMarketDatumEntity.builder()
                                    .id(UUID.randomUUID())
                                    .stationId(id)
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
                        historicStationCommodityMarketDatumRepository.cleanupRedundantData(id, commodityId);
                    });
        }
    }

    private Map<UUID, Double> getEconomyEntityIdProportionMap(CommodityMessage.V3.Economy[] economies) {
        return Optional.ofNullable(economies)
                .map(arr -> Arrays.stream(arr)
                        .map(economy -> {
                            UUID id = economyRepository.findOrCreateByName(economy.getName()).getId();
                            double proportion = economy.getProportion();
                            return new AbstractMap.SimpleEntry<>(id, proportion);
                        })
                        .filter(entry -> Objects.nonNull(entry.getKey()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue, (o1, o2) -> o1)))
                .orElse(Collections.emptyMap());
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
