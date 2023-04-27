import asyncio
import logging
import zlib
import zmq
import zmq.asyncio
import json

from config import config
from util.log import init_logging

from .processors import commodity, default, journal

log = logging.getLogger(__name__)

EDDNRelay = 'tcp://eddn.edcd.io:9500'
EDDNTimeout = 600000

# authorized_software = [
#     "E:D Market Connector",
#     "EDDiscovery",
#     "EDDI",
#     "EDCE",
#     "ED-TD.SPACE",
#     "EliteOCR",
#     "Maddavo's Market Share",
#     "RegulatedNoise",
#     "RegulatedNoise__DJ"
# ]


MESSAGE_PROCESSORS = {
    # TODO Implement transition from commodity 1
    'https://eddn.edcd.io/schemas/commodity/3': commodity,
    'https://eddn.edcd.io/schemas/journal/1': journal,
    'default': default,
}

ctx = zmq.asyncio.Context().instance()


async def process_eddn():
    sock = ctx.socket(zmq.SUB)
    sock.setsockopt(zmq.SUBSCRIBE, b'')  # TODO Can we subscribe for specific schema only?
    sock.setsockopt(zmq.RCVTIMEO, EDDNTimeout)

    while True:
        sock.connect(EDDNRelay)
        msg = await sock.recv()
        msg = zlib.decompress(msg)
        msg_json = json.loads(msg)
        schema = msg_json['$schemaRef']

        if config.eddn.json_file:
            with open(config.eddn.json_file, 'a') as f:
                f.write(json.dumps(msg_json, indent=4) + '\n')
        message_processor = MESSAGE_PROCESSORS.get(schema, default)
        message_processor.process(schema, msg_json['message'])


if __name__ == '__main__':
    init_logging()

    import os
    try:
        if config.eddn.json_file:
            os.remove(config.eddn.json_file)
    except OSError:
        pass
    asyncio.run(process_eddn())
