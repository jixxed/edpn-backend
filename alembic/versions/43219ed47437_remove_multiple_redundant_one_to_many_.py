"""Remove multiple redundant one_to_many tables

Revision ID: 43219ed47437
Revises: 86b681ed8a27
Create Date: 2023-04-27 00:05:40.499269

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import postgresql

# revision identifiers, used by Alembic.
revision = '43219ed47437'
down_revision = '86b681ed8a27'
branch_labels = None
depends_on = None


def upgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_table('station_import_commodities')
    op.drop_table('station_prohibited_commodities')
    op.drop_table('station_export_commodities')
    op.drop_table('commodity')
    op.add_column('station', sa.Column('export_commodities', postgresql.JSONB(astext_type=sa.Text()), nullable=True))
    op.add_column('station', sa.Column('import_commodities', postgresql.JSONB(astext_type=sa.Text()), nullable=True))
    op.add_column('station', sa.Column('prohibited_commodities', sa.ARRAY(sa.String()), nullable=True))
    # ### end Alembic commands ###


def downgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('station', 'prohibited_commodities')
    op.drop_column('station', 'import_commodities')
    op.drop_column('station', 'export_commodities')
    op.create_table('commodity',
    sa.Column('id', sa.INTEGER(), server_default=sa.text("nextval('commodity_id_seq'::regclass)"), autoincrement=True, nullable=False),
    sa.Column('name', sa.VARCHAR(), autoincrement=False, nullable=True),
    sa.PrimaryKeyConstraint('id', name='commodity_pkey'),
    postgresql_ignore_search_path=False
    )
    op.create_table('station_export_commodities',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('commodity_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('station_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('price', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('stock', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['commodity_id'], ['commodity.id'], name='station_export_commodities_commodity_id_fkey'),
    sa.ForeignKeyConstraint(['station_id'], ['station.id'], name='station_export_commodities_station_id_fkey'),
    sa.PrimaryKeyConstraint('id', name='station_export_commodities_pkey')
    )
    op.create_table('station_prohibited_commodities',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('commodity_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('station_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['commodity_id'], ['commodity.id'], name='station_prohibited_commodities_commodity_id_fkey'),
    sa.ForeignKeyConstraint(['station_id'], ['station.id'], name='station_prohibited_commodities_station_id_fkey'),
    sa.PrimaryKeyConstraint('id', name='station_prohibited_commodities_pkey')
    )
    op.create_table('station_import_commodities',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('commodity_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('station_id', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('price', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('demand', sa.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['commodity_id'], ['commodity.id'], name='station_import_commodities_commodity_id_fkey'),
    sa.ForeignKeyConstraint(['station_id'], ['station.id'], name='station_import_commodities_station_id_fkey'),
    sa.PrimaryKeyConstraint('id', name='station_import_commodities_pkey')
    )
    # ### end Alembic commands ###