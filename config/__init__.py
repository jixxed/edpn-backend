from dataclasses import dataclass, field
from typing import Optional, Dict

import dacite
import yaml


DEFAULT_CONFIG = 'config.yml'


@dataclass(frozen=True)
class General:
    logdir: str = 'log'
    logfile: str = 'main.log'
    loglevel: str = 'INFO'


@dataclass(frozen=True)
class DB:
    password: Optional[str] = None
    socket: Optional[str] = None
    host: str = '127.0.0.1'
    name: str = 'edpn'
    user: str = 'edpn'


@dataclass(frozen=True)
class Config:
    db: DB = field(default_factory=DB)
    general: General = field(default_factory=General)


def parse_config(path_to_config: str) -> Dict:
    with open(path_to_config) as cfg_file:
        return yaml.load(cfg_file, Loader=yaml.FullLoader)


def generate_config():
    try:
        cfg = parse_config(DEFAULT_CONFIG)
    except FileNotFoundError:
        print(f'Config {DEFAULT_CONFIG} not found, processing with defaults')
        cfg = {}
    return dacite.from_dict(Config, cfg)


if __name__ == '__main__':
    import os
    from pprint import pprint
    os.chdir('..')
    config = generate_config()
    pprint(config)
else:
    config = generate_config()