import json

from database import Database
from secret import hostname, port, username, password, databaseName, applicationName

database = Database() # Create database object
assert database.connect(hostname, port, username, password, databaseName, applicationName), "Database connection failed"

print("Loading system data...") # Load system data (from eddb.io)
with open(r"upload\systems_populated.json", "r") as file:
    systems = json.load(file)

print("Loading commodity data...") # Load commodity data
with open(r"upload\commodities.json", "r") as file:
    commodities = json.load(file)

print("Loading complete!\nUploading data to database...")

print("Saving systems...") # Save into database
for system in systems:
    systemName = system["name"].replace("'", "''")
    edsmID = system["edsm_id"] if system["edsm_id"] else 0
    population = system["population"] if system["is_populated"] else 0
    x, y, z = system["x"], system["y"], system["z"]

    # Add system to database
    command = [
        f"INSERT INTO starsystem (edsmid, name, population)",
        f"VALUES ({edsmID}, '{systemName}', {population});",
    ]
    database.execute(" ".join(command))
    database.commit()

    # Add coordinates to database
    systemID = database.fetch(f"SELECT id FROM starsystem WHERE name = '{systemName}';")
    systemID = systemID[0][0]
    command = [
        f"INSERT INTO coordinates (id, x, y, z)",
        f"VALUES ({systemID}, {x}, {y}, {z})",
        f"ON CONFLICT DO NOTHING;"
    ]
    database.execute(" ".join(command))
    database.commit()

print("Saving commodities...") # Add commodities to database
for commodity in commodities.keys():
    command = [
        f"INSERT INTO commodity (id, name)",
        f"VALUES ('{commodity}', '{commodities[commodity]}')",
        f"ON CONFLICT DO NOTHING;"
    ]
    database.execute(" ".join(command))
    database.commit()

print("Closing database connection...")
database.close() # Close database connection