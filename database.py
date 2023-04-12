import psycopg2

class Database(object):
    def __init__(self):
        self.connection = None
        self.cursor = None
        
    def connect(self, hostname = "localhost", port = 5432, username = "postgres", password = "postgres", databaseName = "", application_name="") -> bool:
        """Connect to database

        If connection fails, return False"""
        self.hostname = hostname
        self.port = port
        self.username = username
        self.password = password
        self.databaseName = databaseName
        self.applicationName = application_name
        
        try:
            self.connection = psycopg2.connect(
                host=self.hostname,
                port=self.port,
                user=self.username,
                password=self.password,
                database=self.databaseName,
                application_name=self.applicationName
                )
            self.cursor = self.connection.cursor()
            return True
        except Exception as error:
            print(f"Database connection failed: {error}")
            return False
    
    def execute(self, query):
        """Execute query

        If execute fails, return None"""
        if self.connection == None or self.connection.closed:
            if not self.connect(): return None
        try:
            self.cursor.execute(query)
            return True
        except Exception as error: return None
    
    def commit(self):
        """Commit changes to database

        If commit fails, return None"""
        if self.connection == None or self.connection.closed:
            if not self.connect(): return None
        try:
            self.connection.commit()
            return True
        except Exception as error: return None
    
    def fetch(self, query):
        """Execute query and return all results

        If fetch fails, return None"""
        if self.connection == None or self.connection.closed:
            if not self.connect(): return None
        self.cursor.execute(query)
        try: return self.cursor.fetchall()
        except psycopg2.ProgrammingError as error: return None

    def close(self):
        if self.connection != None:
            if not self.connection.closed:
                self.connection.close()