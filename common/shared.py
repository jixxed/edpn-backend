from config import config
from db.db import EDPNDB
from util.db import get_db_uri

edpn_db = EDPNDB(get_db_uri(config.db))
