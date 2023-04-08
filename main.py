import zlib, json, time, os, sys
import zmq, zmq.asyncio
import asyncio

import tracemalloc
tracemalloc.start()

# EDDN relay to connect to
relayEDDN = "tcp://eddn.edcd.io:9500"
timeoutEDDN = 60*10**3 # 60 seconds

# Only allow commodity messages
allowedSchemas = ["https://eddn.edcd.io/schemas/commodity/3"]

# zqm.asyncio requires WindowsSelectorEventLoopPolicy on Windows
if sys.platform: asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())

# Save messages to a folder
dataFolderPath = "data"
if not os.path.exists(dataFolderPath):
    os.makedirs(dataFolderPath)

# Save message to a file
async def saveMessage(message, filename):
    with open(rf"{dataFolderPath}\{filename}.json", "w") as f:
        json.dump(message, f, indent=4)

# Subscribe to EDDN
def main():
    print("Starting EDDN listener...")
    context = zmq.asyncio.Context.instance()
    async def subs():
        subscriber = context.socket(zmq.SUB)

        subscriber.setsockopt(zmq.RCVTIMEO, timeoutEDDN)
        subscriber.setsockopt(zmq.SUBSCRIBE, b"")

        while True:
            print("Connecting to EDDN relay...", flush=True)
            try:
                subscriber.connect(relayEDDN)
                print("Connected to EDDN relay", flush=True)
                while True:
                    message = await subscriber.recv()
                    if message == False:
                        subscriber.disconnect(relayEDDN)
                        break
                    message = zlib.decompress(message)
                    jsonMessage = json.loads(message)
                    
                    # Detect only commodity messages
                    if jsonMessage["$schemaRef"] in allowedSchemas:
                        saveTime = time.strftime("%Y%m%d%H%M%S")
                        await saveMessage(jsonMessage, saveTime)
                        print(f"Saved message at {saveTime}.json", flush=True)
                    else: pass
            except zmq.ZMQError as e:
                print ("ZMQSocketException: " + str(e), flush=True)
                subscriber.disconnect(relayEDDN)
                time.sleep(5)
                print("Retrying...", flush=True)
    asyncio.run(subs())
            
if __name__ == "__main__":
    main()