## Mongo2Arango

Tool for copying data from MongoDB to ArangoDB. The data are read from MongoDB (using a find query) and inserted in ArangoDB. A query and a projection can be provided, to select the data to write in ArangoDB.


### Build

Requirements:
* Maven 3
* Java 7

```bash
mvn clean package
```

### Run

#### Configuration

You have to provide a properties file to the application (you can reuse the `mongo2arango-example.properties` file).

The supported properties are:


Property                   | Default value              | Description
-------------------------- | -------------------------- | ----------------------------------------------------------------------
mongo.host                 | localhost                  | mongod host
mongo.port                 | 27017                      | mongod port
mongo.db                   | test                       | Database name
mongo.collection           | test                       | Collection name
mongo.query                | {}                         | Query to select data 
mongo.projection           | (none)                     | Projection used to select fields that will be indexed
mongo.batch_size           | 20                         | Batch size for the find query
arango.host                | localhost                  | ArangoDB host or IP address
arango.port                | 8529                       | ArangoDB port
arango.db                  | (same as mongo.db)         | Database name
arango.collection          | (same as mongo.collection) | Collection name
arango.create_db           | false                      | Creation of the database if it does no exist
arango.batch_size          | 20                         | Nb of documents before executing the batch



#### Launch the application

```bash
java -jar mongo2arango-jar-with-dependencies.jar -m bulk -c mongo2arango.properties
```


### Test env

This tool has been tested with:
* ArangoDB 2.7.3
* MongoDB 3.0.7, 3.2.0

