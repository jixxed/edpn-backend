from fastapi import FastAPI
import uvicorn
import random

app = FastAPI()

@app.get("/random/{numberFrom}/{numberTo}")
async def random_number(numberFrom: int, numberTo: int):
    return {"random": random.randint(numberFrom, numberTo), "from": numberFrom, "to": numberTo}

if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=8000)