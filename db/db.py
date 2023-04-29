from sqlalchemy import create_engine, select, func, type_coerce, Float, Tuple, \
    literal_column, cast
from sqlalchemy.dialects.postgresql import array
from sqlalchemy.orm import Bundle

from .schema import station, system
from util.db import DBBase


class EDPNDB:
    def __init__(self, uri, **kwargs):
        self.db = DBBase(create_engine(uri, **kwargs))

    def get_station(self, name, system_name):
        system_obj = self.get_system(system_name)
        if system_obj is not None:
            return self.db.get(station, name=name,
                               system_id=system_obj.id).first()

    # TODO This might be not good idea to return ALL stations but so far so good
    #      Consider doing server render
    def get_stations(self):
        query = (select(station.c.id, station.c.name.label('station_name'),
                        station.c.station_type,
                        system.c.name.label('system_name'), )
                 .select_from(station.join(system))
                 .order_by('station_name', ))
        return self.db.execute(query).fetchall()

    def get_stations_with_coordinates(self):
        query = (select(station.c.id, station.c.name,
                        system.c.x, system.c.y, system.c.z,
                        station.c.import_commodities,
                        station.c.export_commodities)
                 .select_from(station.join(system))
                 )

        return self.db.execute(query).fetchall()

    def get_station_coordinates(self, id):
        query = (select(system.c.x, system.c.y, system.c.z)
                 .select_from(station.join(system))
                 .where(station.c.id == id))
        return self.db.execute(query).first()

    def get_system(self, name):
        return self.db.get(system, name=name).first()

    def create_station(self, **kwargs):
        return self.db.create(station, **kwargs)

    def create_system(self, **kwargs):
        return self.db.create(system, **kwargs)

    def update_station_commodity(self, station_id, **kwargs):
        self.db.update(station, id=station_id, **kwargs)
