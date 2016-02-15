## Mongo2Arango

**mongo2arango** is tool to copy data from a MongoDB collection to an ArangoDB collection.
This is very useful when you to migrate your data from a database to another one, or you just want to populate data in ArangoDB to take advantages of such a database.
The data are read from MongoDB (using a find query) and then inserted in ArangoDB. A query and a projection can be provided, to select the data and the fields to write in ArangoDB.

The configuration must be provided in a properties file. 
* first, you configure the source:
  * MongoDB database and collection 
  * Optionally, you provide a query to select the data and a projection to define the field you want to insert in ArangoDB
* then, you configure the target:
  * ArangoDB database and collection
  * Optionally, you can set a size for the bulk insert (i.e. the number of documents to create before executing the batch)



The tool is available on github: https://github.com/bbonnin/mongo2arango

You have to provide a properties file to the application (you can reuse the `mongo2arango-example.properties` file).

The supported properties are:


Property                   | Default value              | Description
-------------------------- | -------------------------- | ----------------------------------------------------------------------
mongo.host                 | localhost                  | mongod host
mongo.port                 | 27017                      | mongod port
mongo.db                   | test                       | Database name
mongo.collection           | test                       | Collection name
mongo.query                | {}                         | Query to select data 
mongo.projection           | (none)                     | Projection used to select fields that will be inserted
mongo.batch_size           | 20                         | Batch size for the find query
arango.host                | localhost                  | ArangoDB host or IP address
arango.port                | 8529                       | ArangoDB port
arango.db                  | (same as mongo.db)         | Database name
arango.collection          | (same as mongo.collection) | Collection name
arango.create_db           | false                      | Creation of the database if it does no exist
arango.batch_size          | 20                         | Nb of documents before executing the batch



#### Launch the application

```bash
java -jar mongo2arango-jar-with-dependencies.jar -c mongo2arango.properties
```


### Test env

This tool has been tested with:
* ArangoDB 2.7.3
* MongoDB 3.0.7, 3.2.0

