package authentication;

import model.*;
import transfers.*;
import drivers.*;

import java.util.HashMap;
import java.util.Date;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.exceptions.*;

import static org.neo4j.driver.v1.Values.parameters;

public class Authentication {

    public static Parcel requestToken( String username, String password, Neo4jDriver db ) {
        
        String encryptedPassword = Encryption.getHash(password);
        String tokenChars = Encryption.generateToken();

        String msg = "";

        String query =  
        "MATCH " + 
        " (p:Player)<-[:Is]-(u:User)-[:Currently]->(d:Data)" + 
        " WHERE " +
        " d.name = $username" +
        " AND d.password = $password" +
        " MERGE" + 
        " (t:Token)" +
        " -[:Grants]->" +
        " (p)" + 
        " ON CREATE " + 
        " SET t.created = TIMESTAMP(), t.token = $token" +
        " ON MATCH " +
        " SET t.created = TIMESTAMP()" + 
        " RETURN t";

        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", encryptedPassword);
        params.put("token", tokenChars);

        StatementResult result = db.runQuery(query, params);

        try {
            result.single().get("t");
        } catch (NoSuchRecordException e) {
            return new Parcel(Status.UNAUTHORIZED, "No user with such credentials.", null);
        }

        Token token = new Token();
        token.created = new Date();
        token.token = tokenChars;
        
        return new Parcel(Status.OK, msg, token);
    }

    public static Parcel validateGrant( long id, String token, Neo4jDriver db ) {

        String msg = "";

        String query =  
        "MATCH " + 
        " (t:Token)-[:Grants|:Possess|:Got*]->(n)" + 
        " WHERE " +
        " t.token = $token" +
        " AND n.id = $id" +
        " SET t.refreshed = TIMESTAMP()" + 
        " RETURN n";

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("token", token);

        StatementResult result = db.runQuery(query, params);

        try {
            result.single().get("n");
        } catch (NoSuchRecordException e) {
            return new Parcel(Status.NOT_FOUND, "Node not found", null);
        }
        
        return new Parcel(Status.OK, msg, token);
    }
}