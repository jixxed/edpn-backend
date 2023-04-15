from fastapi import FastAPI, Request
import uvicorn

from database import Database
from secret import hostname, port, username, password, databaseName, applicationName

import tracemalloc
tracemalloc.start()

app = FastAPI()

# Create database object
database = Database()
assert database.connect(hostname, port, username, password, databaseName, applicationName), "Database connection failed"

@app.get("/station/{id}")
async def random_number(id: int, request: Request):
    response = {}
    dataNotation = database.fetch("SELECT column_name from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'station' order by ORDINAL_POSITION;")
    stationData = database.fetch(f"SELECT * FROM station WHERE id = {id};")
    if len(stationData) == 0:
        response["error"] = "No station with that id"
    else:
        response = {dataNotation[i][0]: stationData[0][i] for i in range(len(dataNotation))}
    return response

if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8000)