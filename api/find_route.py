
import numpy as np

from common.shared import edpn_db


def single_route_finder(reference_station: int,
                        radius: float) -> dict:  # FIXME return type
    stations = edpn_db.get_stations_with_coordinates()
    reference_station_coordinates = edpn_db.get_station_coordinates(
        reference_station)
    # IDK maybe it would be better just to search resulting station throughout
    # array
    stations_dict = {(item.x, item.y, item.z): item for item in stations}

    stations_np = np.array(list(stations_dict.keys()))
    reference_station_np = np.array([reference_station_coordinates])
    distances = np.linalg.norm(stations_np - reference_station_np, axis=1)

    filtered_coordinates = stations_np[distances <= radius]
    # Now we have the coordinates inside requested range. Let's do the job

    filtered_stations = [station for coordinate, station in
                         stations_dict.items() if
                         coordinate in filtered_coordinates]

    # We skip the cargo size for now and calculate profit per one
    buy_prices = {}
    sell_prices = {}
    # Maybe use static set since there are a limited amount of commodities
    commodities = set()

    # I think this whole loop should be done on DB side
    for station in filtered_stations:
        if station.import_commodities is not None:
            for commodity_name, commodity_info in station.import_commodities.items():
                buy_prices.setdefault(commodity_name, []).append(
                    (commodity_info['price'], station.id))
        if station.export_commodities is not None:
            for commodity_name, commodity_info in station.export_commodities.items():
                sell_prices.setdefault(commodity_name, []).append(
                    (commodity_info['price'], station.id))
                # Only adding commodities that are in sale
                commodities.add(commodity_name)

    # Find the best profits for each commodity
    profits = {}

    stations_by_id = {item.id: item.name for item in stations}
    for commodity in commodities:
        # Check that commodities are present in both lists
        if (commodity not in buy_prices) or (commodity not in buy_prices):
            continue
        best_buy = min(sell_prices[commodity])

        best_sell = max(buy_prices[commodity])
        profit = best_sell[0] - best_buy[0]

        # We're interested in positive profit only
        if profit <= 0:
            continue
        profits[commodity] = {
            'buy': stations_by_id[best_buy[1]],
            'buy_id': best_buy[1],
            'sell': stations_by_id[best_sell[1]],
            'sell_id': best_sell[1],
            'profit': profit,
        }

    return profits


def route_finder(route_type: str, reference_station: int,
                 radius: float) -> dict:
    routes = {
        'single': single_route_finder,
    }
    finder_func = routes[route_type]
    return finder_func(reference_station, radius)
