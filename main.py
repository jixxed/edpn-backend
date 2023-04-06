import zlib, json, time, zmq

# EDDN relay to connect to
relayEDDN = "tcp://eddn.edcd.io:9500"
timeoutEDDN = 60*10**3 # 60 seconds

# Only allow commodity messages
allowedSchemas = ["https://eddn.edcd.io/schemas/commodity/3"]

def main():
    print("Starting EDDN listener...")
    context = zmq.Context()
    subscriber = context.socket(zmq.SUB)

    subscriber.setsockopt(zmq.RCVTIMEO, timeoutEDDN)
    subscriber.setsockopt(zmq.SUBSCRIBE, b"")

    while True:
        try:
            subscriber.connect(relayEDDN)
            while True:
                message = subscriber.recv()
                if message == False:
                    subscriber.disconnect(relayEDDN)
                    break
                message = zlib.decompress(message)
                jsonMessage = json.loads(message)
                
                # Detect only commodity messages
                if jsonMessage["$schemaRef"] in allowedSchemas:
                    # Print incoming message to console
                    print(json.dumps(jsonMessage), flush=True)
                else: pass
        except zmq.ZMQError as e:
            print ("ZMQSocketException: " + str(e), flush=True)
            subscriber.disconnect(relayEDDN)
            time.sleep(5)
            
if __name__ == "__main__":
    main()