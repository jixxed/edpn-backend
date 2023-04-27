"""Remove multiple tables for string values

Revision ID: 9a7d455aaae1
Revises: 43219ed47437
Create Date: 2023-04-27 02:18:41.821004

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '9a7d455aaae1'
down_revision = '43219ed47437'
branch_labels = None
depends_on = None


def upgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.alter_column('body', 'distance_to_arrival',
               existing_type=sa.BIGINT(),
               type_=sa.Double(),
               existing_nullable=True)
    op.add_column('station', sa.Column('allegiance', sa.String(), nullable=True))
    op.add_column('station', sa.Column('government', sa.String(), nullable=True))
    op.add_column('station', sa.Column('station_type', sa.String(), nullable=True))
    op.alter_column('station', 'distance_to_star',
               existing_type=sa.BIGINT(),
               type_=sa.Double(),
               existing_nullable=True)
    op.drop_constraint('station_allegiance_id_fkey', 'station', type_='foreignkey')
    op.drop_constraint('station_government_id_fkey', 'station', type_='foreignkey')
    op.drop_constraint('station_station_type_id_fkey', 'station', type_='foreignkey')
    op.drop_column('station', 'allegiance_id')
    op.drop_column('station', 'government_id')
    op.drop_column('station', 'station_type_id')
    op.add_column('system', sa.Column('allegiance', sa.String(), nullable=True))
    op.add_column('system', sa.Column('government', sa.String(), nullable=True))
    op.drop_constraint('system_government_id_fkey', 'system', type_='foreignkey')
    op.drop_constraint('system_allegiance_id_fkey', 'system', type_='foreignkey')
    op.drop_column('system', 'allegiance_id')
    op.drop_column('system', 'government_id')
    op.drop_table('allegiance')
    op.drop_table('government')
    op.drop_table('station_type')
    # ### end Alembic commands ###


def downgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('system', sa.Column('government_id', sa.INTEGER(), autoincrement=False, nullable=True))
    op.add_column('system', sa.Column('allegiance_id', sa.INTEGER(), autoincrement=False, nullable=True))
    op.create_foreign_key('system_allegiance_id_fkey', 'system', 'allegiance', ['allegiance_id'], ['id'])
    op.create_foreign_key('system_government_id_fkey', 'system', 'government', ['government_id'], ['id'])
    op.drop_column('system', 'government')
    op.drop_column('system', 'allegiance')
    op.add_column('station', sa.Column('station_type_id', sa.INTEGER(), autoincrement=False, nullable=True))
    op.add_column('station', sa.Column('government_id', sa.INTEGER(), autoincrement=False, nullable=True))
    op.add_column('station', sa.Column('allegiance_id', sa.INTEGER(), autoincrement=False, nullable=True))
    op.create_foreign_key('station_station_type_id_fkey', 'station', 'station_type', ['station_type_id'], ['id'])
    op.create_foreign_key('station_government_id_fkey', 'station', 'government', ['government_id'], ['id'])
    op.create_foreign_key('station_allegiance_id_fkey', 'station', 'allegiance', ['allegiance_id'], ['id'])
    op.alter_column('station', 'distance_to_star',
               existing_type=sa.Double(),
               type_=sa.BIGINT(),
               existing_nullable=True)
    op.drop_column('station', 'station_type')
    op.drop_column('station', 'government')
    op.drop_column('station', 'allegiance')
    op.alter_column('body', 'distance_to_arrival',
               existing_type=sa.Double(),
               type_=sa.BIGINT(),
               existing_nullable=True)
    op.create_table('government',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('name', sa.VARCHAR(), autoincrement=False, nullable=True),
    sa.PrimaryKeyConstraint('id', name='government_pkey')
    )
    op.create_table('allegiance',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('name', sa.VARCHAR(), autoincrement=False, nullable=True),
    sa.PrimaryKeyConstraint('id', name='allegiance_pkey')
    )
    op.create_table('station_type',
    sa.Column('id', sa.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('name', sa.VARCHAR(), autoincrement=False, nullable=True),
    sa.PrimaryKeyConstraint('id', name='station_type_pkey')
    )
    # ### end Alembic commands ###
