from sqlalchemy import create_engine

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

    def get_system(self, name):
        return self.db.get(system, name=name).first()

    def create_station(self, **kwargs):
        return self.db.create(station, **kwargs)

    def create_system(self, **kwargs):
        return self.db.create(system, **kwargs)

    def update_station_commodity(self, station_id, **kwargs):
        self.db.update(station, id=station_id, **kwargs)
