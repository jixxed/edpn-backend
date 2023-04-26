from sqlalchemy import create_engine

from util.db import DBBase


class EDPNDB:
    def __init__(self, uri, **kwargs):
        self.db = DBBase(create_engine(uri, **kwargs))
