"""
Main entry point to the app
"""
import asyncio
import logging
from dataclasses import asdict
from pprint import pformat

from config import config
from common.shared import edpn_db
from db.schema import metadata
from util.log import init_logging

log = logging.getLogger(__name__)


if __name__ == '__main__':
    init_logging()
    metadata.create_all(bind=edpn_db.db.engine)
    log.info('Starting')
    log.info('Config:\n%s', pformat(asdict(config)))
    loop = asyncio.get_event_loop()

    try:
        loop.run_forever()
    except KeyboardInterrupt:
        log.info('Stopping')
