create table starSystem (
	id serial primary key,
	name text
);
create table coordinates (
	id int,
	x double precision,
	y double precision,
	z double precision
);
create table commodity (
	id text primary key,
	name text
);
create table station (
	marketId bigint primary key,
	starSystemId int,
	name text,
	constraint fkSystemId foreign key(starSystemId) references starSystem(id)
);
create table market (
	commodityId text,
	marketId bigint,
	buyPrice int,
	stock int,
	sellPrice int,
	demand int,
	constraint fkCommodityId foreign key(commodityId) references commodity(id),
	constraint fkMarketId foreign key(marketId) references station(marketId),
	constraint pkMarketIdComodityId primary key(marketId, commodityId)
);