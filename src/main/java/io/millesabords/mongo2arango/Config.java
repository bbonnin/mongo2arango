package io.millesabords.mongo2arango;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration of the application.
 *
 * @author Bruno Bonnin
 */
public class Config extends Properties {

    private static final long serialVersionUID = -7269241682972794667L;

    private static Config INSTANCE = new Config();

    public static final String MONGO_HOST = "mongo.host";
    public static final String MONGO_PORT = "mongo.port";
    public static final String MONGO_DB = "mongo.db";
    public static final String MONGO_COLLECTION = "mongo.collection";
    public static final String MONGO_QUERY = "mongo.query";
    public static final String MONGO_PROJECTION = "mongo.projection";
    public static final String MONGO_BATCH_SIZE = "mongo.batch_size";

    public static final String ARANGO_HOST = "arango.host";
    public static final String ARANGO_PORT = "arango.port";
    public static final String ARANGO_DB = "arango.db";
    public static final String ARANGO_COLLECTION = "arango.collection";
    public static final String ARANGO_CREATE_DB = "arango.create_db";
    public static final String ARANGO_BATCH_SIZE = "arango.batch_size";


    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        put(MONGO_HOST, "localhost");
        put(MONGO_PORT, "27017");
        put(MONGO_DB, "test");
        put(MONGO_COLLECTION, "test");
        put(MONGO_QUERY, "{}");
        put(MONGO_BATCH_SIZE, "20");

        put(ARANGO_HOST, "localhost");
        put(ARANGO_PORT, "8529");
        put(ARANGO_CREATE_DB, false);
        put(ARANGO_BATCH_SIZE, "20");
    }

    public String get(final String key) {
        return getProperty(key);
    }

    public int getInt(final String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(final String key) {
        return Boolean.parseBoolean(get(key));
    }

    @Override
    public synchronized void load(final InputStream is) throws IOException {
        super.load(is);

        if (get(ARANGO_DB) == null || get(ARANGO_DB).trim().length() == 0) {
            put(ARANGO_DB, get(MONGO_DB));
        }
        if (get(ARANGO_COLLECTION) == null || get(ARANGO_COLLECTION).trim().length() == 0) {
            put(ARANGO_COLLECTION, get(MONGO_COLLECTION));
        }
    }

    @Override
    public String toString() {
        return "Config: " + super.toString();
    }
}
