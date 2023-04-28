import logging

from aiohttp import web
from aiohttp_middlewares import cors_middleware

routes = web.RouteTableDef()

log = logging.getLogger(__file__)


@routes.get('/api/hello')
def hello(request):
    return web.json_response({'hello': 'world'})


def create_app():
    app = web.Application(
        middlewares=[cors_middleware(allow_all=True)]
    )
    app.add_routes(routes)
    return app
