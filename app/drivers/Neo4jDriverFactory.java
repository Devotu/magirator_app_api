package drivers;

import org.neo4j.driver.v1.*;

import play.Logger;

public class Neo4jDriverFactory {
    private static Neo4jDriver instance;
    public static synchronized Neo4jDriver getDriver() {
        if (instance == null) {
            Logger.debug("---------> Initiating new db <----------");
            instance = new Neo4jDriver();
        }
        Logger.debug("<returning db>");
        return instance;
    }
}