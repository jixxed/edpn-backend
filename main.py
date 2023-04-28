"""
Main entry point to the app
"""
import asyncio
import logging
from dataclasses import asdict
from pprint import pformat

from aiohttp import web

from api.app import create_app
from config import config
from common.shared import edpn_db
from db.schema import metadata
from util.log import init_logging
from eddn.listener import process_eddn

log = logging.getLogger(__name__)


if __name__ == '__main__':
    init_logging()
    metadata.create_all(bind=edpn_db.db.engine)
    log.info('Starting')
    log.info('Config:\n%s', pformat(asdict(config)))
    loop = asyncio.get_event_loop()

    # Starting EDDN listener
    if config.eddn.enabled:
        eddn_listener = loop.create_task(process_eddn())
        log.info('EDDN: started')

    # Starting web-server
    if config.web.enabled:
        app = create_app()
        runner = web.AppRunner(app)
        loop.run_until_complete(runner.setup())
        site = web.TCPSite(runner, config.web.host, config.web.port)
        loop.run_until_complete(site.start())
        log.info('Web: started')

    try:
        loop.run_forever()
    except KeyboardInterrupt:
        log.info('Stopping')

        if config.eddn.enabled:
            eddn_listener.cancel()
            log.info('EDDN: stopped')
        if config.web.enabled:
            loop.run_until_complete(runner.cleanup())
            log.info('Web: stopped')
        log.info('Ciao')
