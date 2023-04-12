import zlib, json, time, sys
import zmq, zmq.asyncio
import asyncio
import msvcrt

from database import Database
import logFunctions as log

import tracemalloc
tracemalloc.start()

# EDDN relay to connect to
relayEDDN = "tcp://eddn.edcd.io:9500"
timeoutEDDN = 60*10**3 # 60 seconds

# Only allow commodity messages
allowedSchemas = ["https://eddn.edcd.io/schemas/commodity/3"]

# zqm.asyncio requires WindowsSelectorEventLoopPolicy on Windows
if sys.platform: asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())

# Connection details
hostname, port = "localhost", "5432"
username, password = "postgres", "root"
databaseName = "eddb2.0"
applicationName = "EDDB2.0"

# Create database object
database = Database()
assert database.connect(hostname, port, username, password, databaseName, applicationName), "Database connection failed"

# Save message to a database
async def saveMessage2db(message, database: Database):
    marketId = message["message"]["marketId"]
    stationId = marketId
    systemName = message["message"]["systemName"].replace("'", "''")
    stationName = message["message"]["stationName"].replace("'", "''")
    commodities = message["message"]["commodities"]

    # Get system id
    database.execute(f"INSERT INTO starsystem (name) VALUES ('{systemName}');")
    database.commit()
    systemId = database.fetch(f"SELECT id FROM starsystem WHERE name = '{systemName}';")
    systemId = systemId[0][0]

    # Add station to database
    database.execute(f"INSERT INTO station (marketId, starsystemid, name) VALUES ({stationId}, {systemId}, '{stationName}') ON CONFLICT DO NOTHING;")
    database.commit()

    # Add commodities to database
    for commodity in commodities:
        id = commodity["name"]
        buyPrice = commodity["buyPrice"]
        sellPrice = commodity["sellPrice"]
        demand = commodity["demand"]
        stock = commodity["stock"]

        # Add commodity to database
        command = [
            "INSERT INTO market (commodityid, marketid, buyPrice, stock, sellprice, demand) ",
            f"VALUES ('{id}', {marketId}, {buyPrice}, {stock}, {sellPrice}, {demand}) ",
            f"ON CONFLICT (commodityid, marketid) DO UPDATE SET ",
            f"buyPrice = {buyPrice}, stock = {stock}, sellprice = {sellPrice}, demand = {demand};"
        ]
        database.execute("".join(command))
        database.commit()



# Subscribe to EDDN
def main():
    print("Starting EDDN listener...")
    context = zmq.asyncio.Context.instance()
    async def subs():
        subscriber = context.socket(zmq.SUB)

        subscriber.setsockopt(zmq.RCVTIMEO, timeoutEDDN)
        subscriber.setsockopt(zmq.SUBSCRIBE, b"")

        while True:
            log.logInfo("Connecting to EDDN relay...", "ZMQ")
            try:
                subscriber.connect(relayEDDN)
                log.logInfo("Connected to EDDN relay", "ZMQ")
                while True:
                    message = await subscriber.recv()
                    if message == False:
                        subscriber.disconnect(relayEDDN)
                        break
                    message = zlib.decompress(message)
                    jsonMessage = json.loads(message)
                    
                    # Detect only commodity messages
                    if jsonMessage["$schemaRef"] in allowedSchemas:
                        log.logInfo(f"Received message from schema 'commodityV3'", "EDDN")
                        await saveMessage2db(jsonMessage, database)
                        log.logInfo(f"Saved message to database", "EDDN")
                    else: pass
            except zmq.ZMQError as e:
                log.logError("ZMQSocketException: " + str(e), "ZMQ")
                subscriber.disconnect(relayEDDN)
                time.sleep(5)
                log.logInfo("Retrying...", "ZMQ")
    asyncio.run(subs())
            
if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("Keyboard interrupt detected, closing...")
        database.close()
        sys.exit(0)