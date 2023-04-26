def repr_args(*args, **kwargs):
    return ', '.join(
        [*map(repr, args)] +
        ['='.join((str(k), repr(v))) for k, v in kwargs.items()]
    )
