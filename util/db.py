import logging
import urllib

from sqlalchemy.sql import and_, text

from util.funcs import repr_args


log = logging.getLogger(__name__)


def get_db_uri(cfg):
    netloc = f'{cfg.user}:{cfg.password}@' \
             f'{cfg.host}' if cfg.socket is None \
        else f'{urllib.parse.quote(cfg.socket, safe="")}'
    return urllib.parse.urlunparse(
        ('postgresql', netloc, cfg.name, None, None, None)
    )


class ColumnOperation:
    def __init__(self, op, *args, **kwargs):
        self.op = op
        self.args = args
        self.kwargs = kwargs

    def __call__(self, *args, **kwargs):
        return ColumnOperation(self.op, *args, **kwargs)

    def apply(self, column):
        return getattr(column, self.op)(*self.args, **self.kwargs)

    def __repr__(self):
        return '{}({})'.format(self.op, repr_args(*self.args, **self.kwargs))


in_ = ColumnOperation('in_')
eq = ColumnOperation('__eq__')
lt = ColumnOperation('__lt__')
gt = ColumnOperation('__gt__')
ge = ColumnOperation('__ge__')
desc = ColumnOperation('desc')
asc = ColumnOperation('asc')


class DBBase:

    def __init__(self, engine):
        self.engine = engine

    def execute(self, *args, **kwargs):
        with self.engine.begin() as conn:
            return conn.execute(*args, **kwargs)

    def get(self, table, *fields, **kwargs):
        order_by = kwargs.pop('order_by', None)
        result = table.select().where(kwargs_to_query(table, **kwargs))
        if fields:
            result = result.with_only_columns(map(text, fields))
        if order_by:
            def parse_str(col):
                return (getattr(table.c, col).asc()
                        if isinstance(col, str) else col)
            order_by = map(parse_str, order_by)
            result = result.order_by(*order_by)
        return self.execute(result)

    def create(self, table, **kwargs):
        res = self.execute(table.insert().values(**kwargs))
        return res.inserted_primary_key[0]

    def update(self, table, id, **kwargs):
        return self.execute(
            table.update().where(table.c.id == id).values(**kwargs))

    def delete(self, table, **kwargs):
        return self.execute(
            table.delete().where(kwargs_to_query(table, **kwargs)))


def kwargs_to_query(table, **kwargs):
    ensure_op = lambda x: x if isinstance(x, ColumnOperation) else eq(x)
    return and_(*[ensure_op(value).apply(getattr(table.c, name))
                for name, value in kwargs.items()])
