import os
import logging

from config import config

LOG_FORMAT = '%(asctime)s - %(processName)s->%(filename)s:%(lineno)d-' \
             ' %(levelname)s - %(message)s'


def init_logging(loglevel=None):
    os.makedirs(config.general.logdir, exist_ok=True)
    logging.basicConfig(format=LOG_FORMAT)
    loglevel = loglevel or config.general.loglevel
    formatter = logging.Formatter(LOG_FORMAT)
    fh = logging.FileHandler(
        os.path.join(config.general.logdir, config.general.logfile)
    )
    fh.setLevel(loglevel)
    fh.setFormatter(formatter)
    logger = logging.getLogger()
    logger.addHandler(fh)
    logger.setLevel(loglevel)
