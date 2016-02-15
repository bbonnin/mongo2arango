## Mongo2Arango

**mongo2arango** is tool to copy data from a MongoDB collection to an ArangoDB collection.
This is very useful when you to migrate your data from a database to another one, or you just want to populate data in ArangoDB to take advantages of such a database.

![mongo2arango](images/mongo2arango.png)

The application will process the data in the following way :
* the data are read from MongoDB : in addition to the collection, a query and a projection can be provided to select the data and the fields to write in ArangoDB.
* then inserted in ArangoDB: the tool uses the batch mode, i.e. new document creations are stacked until a limit size, then all these stacked requests are sent to ArangoDB
Processing will loop through these steps until there are no more data to read.

As it it a Java application, it will run wherever you want. For launching, use the following command : java -jar mongo2arango-jar-with-dependencies.jar -c mongo2arango.properties.

The only argument is a properties file that you need to provide to the application.
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


The tool is available on github: https://github.com/bbonnin/mongo2arango
