package stores;

import model.*;
import transfers.*;
import drivers.*;

import java.util.HashMap;
import java.util.Date;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.types.Node;

import static org.neo4j.driver.v1.Values.parameters;

import play.Logger;

public class UserStore {

    
    public Parcel create( Model _anchor, User model, Neo4jDriver db ){

        String msg = "";

        try {            
            model.id = Neo4jDriver.getUniqueId(db);
        } catch (Exception e) {
            return new Parcel(Status.ERROR, "Could not generate a unique id", null);
        }

        String query =  
        "CREATE" + 
            " (n:User:PERSISTENT:Active { id:$id })" + 
            " -[:Currently]->" +
            " (d:Data { created:TIMESTAMP(), name:$name, password:$password })" + 
        " RETURN n,d";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Creating user " + model.id + ", " + model.name);
        
        params.put("id", model.id);
        params.put("name", model.name);
        params.put("password", model.password);

        StatementResult result = db.runQuery(query, params);

        User user = extractUser( result.single() );
        
        return new Parcel(Status.OK, msg, user);
    }

    public Parcel read(long id, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:User)" +
        " -[:Currently]->" +
        " (d:Data)" +
        " WHERE n.id = $id" + 
        " RETURN n,d";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Reading user " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            User user = extractUser( result.single() );

            return new Parcel(Status.OK, "ok", user);

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    public Parcel update(User newUser, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:User)" +
        " -[oc:Currently]->" +
        " (od:Data)" +
        " WHERE n.id = $id" + 
        " CREATE (n)" +
        " -[nc:Currently]->" +
        " (nd:Data { created:TIMESTAMP(), name:$name, password:$password })" +
        " <-[:Became]-" +
        " (od)" +
        " DELETE oc" +
        " RETURN count(nd) AS updated";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Updating user " + newUser.id + " to name: " + newUser.name);
        
        params.put("id", newUser.id);
        params.put("name", newUser.name);
        params.put("password", newUser.password);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberUpdated = result.single().get("updated").asInt();

            if (numberUpdated == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberUpdated > 1) {
                msg = "Found and updated more than one user";
                
            } else if (numberUpdated == 0) {
                msg = "Could not find (and update) requested user";
                
            }

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    public Parcel delete(long id, Neo4jDriver db){
        
        String msg = "";

        String query =  
        "MATCH (n:User)" + 
        " WHERE n.id = $id" + 
        " REMOVE n:Active" +
        " SET n:Deleted" +
        " RETURN count(n) AS deleted";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Deleting user " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberDeleted = result.single().get("deleted").asInt();

            if (numberDeleted == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberDeleted > 1) {
                msg = "Found and deleted more than one user";
                
            } else if (numberDeleted == 0) {
                msg = "Could not find (and delete) requested user";
                
            }

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    private User extractUser(Record record){

        Node node = record.get("n").asNode();
        Logger.debug("Got node");
        Node data = record.get("d").asNode();
        Logger.debug("Got data");

        User user = new User();
        user.assignAll(
            node.get("id").asLong(),
            new Date(data.get("created").asLong()),
            data.get("name").asString(),
            data.get("password").asString()
        );

        return user;
    }
}