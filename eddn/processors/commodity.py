# DemandBracket:
#   0: no demand,
#   1: critically low demand: red graph,
#   2: normal- no graph,
#   3: large demand: green graph
# StockBracket:
#   0: no supply,
#   1: red: critically low supply,
#   2: normal- no graph,
#   3: large supply: green graph

# This is another version that actually looks closer to truth
# Stockbracket
#   3 = [Not seen]
#   2 = No red bars
#   1 = 3 red bars
#   0 = Not for sale
#
# DemandBracket
#   3 = 3 green bars
#   2 = No green bars
#   1 = [Not seen]
#   0 = Not on listing (unless player has cargo in hold)

import logging

from common.shared import edpn_db

log = logging.getLogger(__name__)


def process(schema, message):
    # FIXME Process FleetCarrier because FleetCarrier name is unique
    import_commodities = {}
    export_commodities = {}

    # Process station
    station_name = message['stationName']
    system_name = message['systemName']

    station = edpn_db.get_station(name=station_name, system_name=system_name)
    if station is None:
        # Completely skipping processing commodities if station
        # isn't known to us
        log.debug(
            f'Station {station_name} in system {system_name} not found. Skipping')
        return

    # Process commodities
    for commodity in message['commodities']:
        commodity_name = commodity['name']
        if commodity['demandBracket'] != 0:
            # Gazelle said "you can ignore demand if the demandBracket is 0, it will not be shown in game"
            # https://discord.com/channels/164411426939600896/164413405472620546/367031328899727361
            import_commodities[commodity_name] = {
                'demand': commodity['demand'],
                'price': commodity['buyPrice'],
            }

        if commodity['stockBracket'] != 0:
            export_commodities[commodity_name] = {
                'stock': commodity['stock'],
                'price': commodity['sellPrice'],
            }

    # Process economies
    pass  # FIXME There are too many questions atm

    # Process prohibited ???
    prohibited_commodities = message.get('prohibited')

    # Process timestamp
    timestamp = message['timestamp']

    log.debug(f'Updating station {station_name}')
    edpn_db.update_station_commodity(
        station.id,
        import_commodities=import_commodities,
        export_commodities=export_commodities,
        prohibited_commodities=prohibited_commodities,
        market_updated=timestamp,
    )
