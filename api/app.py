import logging

import json
from functools import partial

from aiohttp import web
from aiohttp_middlewares import cors_middleware

from api.find_route import route_finder
from common.shared import edpn_db

routes = web.RouteTableDef()

log = logging.getLogger(__file__)


@routes.get('/api/hello')
async def hello(request):
    return web.json_response({'hello': 'world'})


@routes.get('/api/station')
async def get_station(request):
    return web.json_response(
        list(map(lambda x: x._asdict(), edpn_db.get_stations())),
        dumps=partial(json.dumps, default=str)
    )


@routes.post('/api/find_route')
async def find_route(request):
    data = await request.json()
    result = route_finder(route_type=data['route_type'],
                          reference_station=data['referenceStation'],
                          radius=data['radius'])
    return web.json_response(data=result, status=200)


def create_app():
    app = web.Application(
        middlewares=[cors_middleware(allow_all=True)]
    )
    app.add_routes(routes)
    return app
