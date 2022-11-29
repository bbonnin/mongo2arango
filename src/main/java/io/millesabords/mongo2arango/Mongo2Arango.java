package io.millesabords.mongo2arango;

import java.io.FileInputStream;
import java.util.Date;

import org.jongo.Jongo;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arangodb.ArangoDB;
import com.arangodb.DbName;
import com.arangodb.mapping.ArangoJack;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Simple tool for exporting data from MongoDB to ArangoDB.
 *
 * @author Bruno Bonnin
 */
public class Mongo2Arango {

    private static Logger LOGGER = LoggerFactory.getLogger(Mongo2Arango.class);

    @Option(name = "-c", usage = "Configuration file", required = true, metaVar = "<file name>")
    private String configFileName;

    private MongoClient mongoClient;

    private Jongo jongo;

    private ArangoDB arangoDb;

    private final Config cfg = Config.get();


    public static void main(final String[] args) {
        new Mongo2Arango().doMain(args);
    }

    private void doMain(final String[] args) {
        final CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);

            cfg.load(new FileInputStream(configFileName));

            LOGGER.info("Config : {}", cfg);

            initMongoClient();
            initArangoClient();

            createArangoDatabase();

            LOGGER.info("Start : {}", new Date());

            new BulkInsert(jongo, arangoDb).run();

        }
        catch (final CmdLineException e) {
            System.err.println("Bad argument");
            parser.printUsage(System.err);
        }
        catch (final Exception e) {
            LOGGER.error("Problem during processing", e);
        }

        LOGGER.info("End : {}", new Date());

    }

    private void initMongoClient() {
        mongoClient = new MongoClient(new MongoClientURI(
            "mongodb://" + cfg.get(Config.MONGO_HOST) + ":" + cfg.get(Config.MONGO_PORT)));
        mongoClient.getServerAddressList(); // To check that the connection is ok
        jongo = new Jongo(mongoClient.getDB(cfg.get(Config.MONGO_DB)));
    }

    private void initArangoClient() {
        arangoDb = new ArangoDB.Builder().serializer(new ArangoJack()).build();
    }

    private void createArangoDatabase() {
    	DbName dbName = DbName.of(cfg.get(Config.ARANGO_DB));
        if (cfg.getBoolean(Config.ARANGO_CREATE_DB) && !arangoDb.db(dbName).exists()) {
			arangoDb.createDatabase(dbName);
        }
    }

}
