select market.commodityid as "commodity ID", 
market.sellprice as "Sell price", 
station.name as "Station name", 
starsystem.name as "System"
from market
join station on market.marketid = station.marketid and market.commodityid = 'monazite' and market.demand > 0
join starsystem on station.starsystemid = starsystem.id
order by market.sellprice desc
limit 1