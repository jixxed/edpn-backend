# This file is here just to fill some data to DB
# It should be completely rewritten, and I mean it
# I'll just leave it as is because it does the job for the prototype purposes

import logging

from common.shared import edpn_db

log = logging.getLogger(__name__)

IMPORTANT_KEYS = [
    'StationType', 'DistFromStarLS', 'MarketID',
]


class ValidationError(Exception):
    pass


def _validate_message(message):
    for field in IMPORTANT_KEYS:
        if field not in message:
            log.error(f'NO FIELD {field}')
            log.debug(message)
            raise ValidationError


def _process_location(message):
    # For now, I'm using it to parse station data and fill it to DB only
    if message['BodyType'] != 'Station':
        # Not a station data, skipping
        log.debug(f'The body is not station: {message["BodyType"]}, skipping for now')
        return

    try:
        _validate_message(message)
    except ValidationError:
        return

    system_name = message['StarSystem']
    station_name = message['Body']
    station = edpn_db.get_station(station_name, system_name)
    if station is not None:
        # FIXME station already in DB, update is not implemented yet
        return

    station_data = {
        # 'allegiance': message['SystemAllegiance'],
        # 'body': None,  # FIXME Is this only bodyid for planetary stations?
        'name': station_name,
        # 'faction': None,  # FIXME Update later
        'government': message.get('StationGovernment'),  # I have no idea why EDMC sometimes skip this field
        'station_type': message['StationType'],
        'distance_to_star': message['DistFromStarLS'],
        'ed_market_id': message['MarketID'],
        'services_bm': 0,  # TODO Implement
        # 'is_planetary': None,  # TODO Can infer from station type I guess
        'last_updated': message['timestamp'],
        # 'market_updated': '',
        # 'outfitting_updated': '',
        # 'shipyard_updated': '',
        # 'landing_pad': '',
    }

    system_obj = edpn_db.get_system(system_name)
    if system_obj is None:
        # FIXME Probably need to create system entry from different event
        x, y, z = message['StarPos']
        system_data = {
            'name': system_name,
            'allegiance': message['SystemAllegiance'],
            # 'controlling_minor_faction': '',
            'government': message['SystemGovernment'],
            # 'power': '',
            # 'power_state': '',
            # 'primary_economy': '',
            # 'reserve_type': '',  # ??????
            'security': message['SystemSecurity'],
            'x': x,
            'y': y,
            'z': z,
            'ed_system_address': message['SystemAddress'],
            # 'last_updated': '',
            # 'needs_permit': '',
            # 'population': '',
        }

        system_id = edpn_db.create_system(**system_data)
    else:
        system_id = system_obj.id
    station_data['system_id'] = system_id
    edpn_db.create_station(**station_data)


def _process_docked(message):
    system_name = message['StarSystem']
    station_name = message['StationName']
    station = edpn_db.get_station(station_name, system_name)

    try:
        _validate_message(message)
    except ValidationError:
        return

    if station is not None:
        # FIXME station already in DB, update is not implemented yet
        return

    station_data = {
        # 'allegiance': Docked event doesn't have allegiance
        # 'body': None,  # FIXME Is this only bodyid for planetary stations?
        'name': station_name,
        # 'faction': '',  # FIXME Update later
        'government': message['StationGovernment'],
        'station_type': message['StationType'],
        'distance_to_star': message['DistFromStarLS'],
        'ed_market_id': message['MarketID'],
        'services_bm': 0,  # TODO Implement
        # 'is_planetary': '',  # TODO Can infer from station type I guess
        'last_updated': message['timestamp']
    }

    system_obj = edpn_db.get_system(system_name)
    if system_obj is None:
        # FIXME Probably need to create system entry from different event
        x, y, z = message['StarPos']
        system_data = {
            'name': system_name,
            # 'alegiance': '',
            # 'controlling_minor_faction': '',
            # 'government': '',
            # 'power': '',
            # 'power_state': '',
            # 'primary_economy': '',
            # 'reserve_type': '',  # ??????
            # 'security': '',
            'x': x,
            'y': y,
            'z': z,
            'ed_system_address': message['SystemAddress'],
            # 'last_updated': '',
            # 'needs_permit': '',
            # 'population': '',
        }

        system_id = edpn_db.create_system(**system_data)
    else:
        system_id = system_obj.id
    station_data['system_id'] = system_id
    edpn_db.create_station(**station_data)


def process(_, message):
    event_handlers = {
        'Location': _process_location,
        'Docked': _process_docked
    }

    event = message.get('event')
    if not event:
        return
    event_handler = event_handlers.get(event)
    if not event_handler:
        return

    event_handler(message)

