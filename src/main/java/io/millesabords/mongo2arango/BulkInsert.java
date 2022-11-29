package io.millesabords.mongo2arango;

import java.util.Map;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.QueryModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.DbName;
import com.mongodb.DBCursor;

/**
 * Bulk insert of data.
 *
 * @author Bruno Bonnin
 */
public class BulkInsert {

    private static Logger LOGGER = LoggerFactory.getLogger(BulkInsert.class);

    private final Jongo jongo;

    private final ArangoDB arango;

    private final Config cfg = Config.get();

    public BulkInsert(final Jongo jongo, final ArangoDB arango) {
        this.jongo = jongo;
        this.arango = arango;
    }

    /**
     * Data transfer from MongoDB to ArangoDB using a bulk insertion.
     */
    @SuppressWarnings({ "rawtypes" })
    public void run() throws Exception {

        final MongoCollection collection = jongo.getCollection(cfg.get(Config.MONGO_COLLECTION));
        final MongoCursor<Map> cursor = collection
                .find(cfg.get(Config.MONGO_QUERY)).projection(cfg.get(Config.MONGO_PROJECTION))
                .with(new QueryModifier() {
                    @Override
                    public void modify(DBCursor cursor) {
                        cursor.batchSize(cfg.getInt(Config.MONGO_BATCH_SIZE));
                    }
                })
                .as(Map.class);
        
        int total = 0;
        int count = 0;
        Map doc;
        final String arangoDatabaseName = cfg.get(Config.ARANGO_DB);
        final String arangoColl = cfg.get(Config.ARANGO_COLLECTION);
        final int batchSize = cfg.getInt(Config.ARANGO_BATCH_SIZE);
        
        ArangoCollection arangoCollection = arango.db(DbName.of(arangoDatabaseName)).collection(arangoColl);
		if (!arangoCollection.exists()) {
			LOGGER.info("ArangoDB | Creating collection : {}", arangoCollection.name());
			arangoCollection.create();
		}
        
        while (cursor.hasNext()) {
            total++;
            count++;
            doc = cursor.next();
            doc.remove("_id").toString();

            arangoCollection.insertDocument(doc);

            if (count % batchSize == 0 || !cursor.hasNext()) {
                LOGGER.info("Current total : {}", total);
                count = 0;
            }
        }

        LOGGER.info("Total of inserted docs : {}", total);
    }

}
