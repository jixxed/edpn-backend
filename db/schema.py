from functools import partial

from sqlalchemy import Table, Column, Integer, ForeignKey, MetaData, String, \
    SmallInteger, Text, PickleType, Double, BigInteger, DateTime, Boolean
from sqlalchemy.dialects.postgresql import HSTORE

metadata = MetaData()

pkey_column = partial(Column, 'id', Integer, primary_key=True)
name_column = partial(Column, 'name', String)


def id_name_column(name):
    return partial(Table, name, metadata, pkey_column(), name_column())


power_state = id_name_column('power_state')()
economy = id_name_column('economy')()
government = id_name_column('government')()
reserve_type = id_name_column('reserve_type')()
faction = id_name_column('faction')()
allegiance = id_name_column('allegiance')()
security = id_name_column('security')()
power = id_name_column('power')()
station_type = id_name_column('station_type')()
module = id_name_column('module')()
ship = id_name_column('ship')()
commodity = id_name_column('commodity')()
landing_pad = Table(
    'landing_pad', metadata,
    pkey_column(),
    Column('size', SmallInteger)  # We have only three...
)

system = Table(
    'system', metadata,
    pkey_column(),
    name_column(),
    Column('allegiance_id', Integer, ForeignKey('allegiance.id')),
    Column('controlling_minor_faction_id', Integer, ForeignKey('faction.id')),
    Column('government_id', Integer, ForeignKey('government.id')),
    Column('power_id', Integer, ForeignKey('power.id')),
    Column('power_state_id', Integer, ForeignKey('power_state.id')),  # FIXME!!!!!
    Column('primary_economy_id', Integer, ForeignKey('economy.id')),
    Column('reserve_type_id', Integer, ForeignKey('reserve_type.id')),
    Column('security_id', Integer, ForeignKey('security.id')),
    Column('x', Double),
    Column('y', Double),
    Column('z', Double),
    Column('ed_system_address', BigInteger),  # Is it int64?
    Column('last_updated', DateTime),
    Column('needs_permit', Boolean),
    Column('population', BigInteger),
)

body = Table(
    'body', metadata,
    pkey_column(),
    name_column(),
    Column('system_id', Integer, ForeignKey('system.id')),
    Column('arg_of_periapsis', Double),
    Column('atmosphere_composition', HSTORE),  # Will we ever need this as data?
    Column('atmosphere_type', String),
    Column('axial_tilt', Double),
    Column('distance_to_arrival', BigInteger),
    Column('earth_masses', Double),
    Column('ed_body_address', BigInteger),  # Is this int64 id?
    Column('gravity', Double),
    Column('is_landable', Boolean),
    Column('materials', HSTORE),
    Column('orbital_eccentricity', Double),
    Column('orbital_inclination', Double),
    Column('orbital_period', Double),
    Column('radius', Double),
    Column('rotational_period', Double),
    Column('tidally_locked', Boolean),
    Column('semi_major_axis', Double),
    Column('solid_composition', HSTORE),
    Column('sub_type', String),
    Column('surface_pressure', Double),
    Column('surface_temperature', Integer),
    Column('terraforming_state', String),  #  Shouldn't we need boolean here?
    Column('type', String),
    Column('volcanism_type', String),
)

station = Table(
    'station', metadata,
    pkey_column(),
    name_column(),
    Column('allegiance_id', Integer, ForeignKey('allegiance.id')),
    Column('body_id', Integer, ForeignKey('body.id')),
    Column('faction_id', Integer, ForeignKey('faction.id')),
    Column('government_id', Integer, ForeignKey('government.id')),
    Column('landing_pad_id', Integer, ForeignKey('landing_pad.id')),
    Column('station_type_id', Integer, ForeignKey('station_type.id')),
    Column('distance_to_star', BigInteger),  # Do we need bigint here?
    Column('ed_market_id', Integer),  # what's this?
    Column('services_bm', Integer),  # Can we have a bitmask instead of 14 boolean fields?
    Column('is_planetary', Boolean),
    Column('last_updated', DateTime),
    Column('market_updated', DateTime),
    Column('outfitting_updated', DateTime),
    Column('shipyard_updated', DateTime),
)


power_state_system_map = Table(
    'power_state_system_map', metadata,
    pkey_column(),
    Column('power_state_id', Integer, ForeignKey('power_state.id')),
    Column('system_id', Integer, ForeignKey('system.id'))
)

reserve_type_system_map = Table(
    'reserve_type_system_map', metadata,
    pkey_column(),
    Column('reserve_type_id', Integer, ForeignKey('reserve_type.id')),
    Column('system_id', Integer, ForeignKey('system.id'))
)

security_system_map = Table(
    'security_system_map', metadata,
    pkey_column(),
    Column('security_id', Integer, ForeignKey('security.id')),
    Column('system_id', Integer, ForeignKey('system.id'))
)

power_system_map = Table(
    'power_system_map', metadata,
    pkey_column(),
    Column('power_id', Integer, ForeignKey('power.id')),
    Column('system_id', Integer, ForeignKey('system.id'))
)

station_economy_map = Table(
    'station_economy_map', metadata,
    pkey_column(),
    Column('economy_id', Integer, ForeignKey('economy.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)

station_export_commodities = Table(
    'station_export_commodities', metadata,
    pkey_column(),
    Column('commodity_id', Integer, ForeignKey('commodity.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)

station_import_commodities = Table(
    'station_import_commodities', metadata,
    pkey_column(),
    Column('commodity_id', Integer, ForeignKey('commodity.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)

station_prohibited_commodities = Table(
    'station_prohibited_commodities', metadata,
    pkey_column(),
    Column('commodity_id', Integer, ForeignKey('commodity.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)

station_module_map = Table(
    'station_module_map', metadata,
    pkey_column(),
    Column('module_id', Integer, ForeignKey('module.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)

station_ship_map = Table(
    'station_ship_map', metadata,
    pkey_column(),
    Column('ship_id', Integer, ForeignKey('ship.id')),
    Column('station_id', Integer, ForeignKey('station.id'))
)
