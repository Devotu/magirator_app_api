package stores;

import model.*;
import transfers.*;
import drivers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Date;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.exceptions.*;

import static org.neo4j.driver.v1.Values.parameters;

import play.Logger;

public class PlayerStore implements Store<User, Player> {

    @Override
    public Parcel create(User anchor, Player model, Neo4jDriver db){

        String msg = "";

        try {            
            model.id = Neo4jDriver.getUniqueId(db);
        } catch (Exception e) {
            return new Parcel(Status.ERROR, "Could not generate a unique id", null);
        }

        String query =  
        "MATCH " + 
        " (a:User)" + 
        " WHERE " +
        " a.id = $anchor_id" +
        " CREATE" + 
        " (a)" +
        " -[:Is]->" +
        " (n:Player:Active:PERSISTENT { id:$id })" + 
        " -[:Currently]->" +
        " (d:Data { created:TIMESTAMP(), name:$name })" + 
        " RETURN n.id";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Creating player " + model.id + ", " + model.name);
        
        params.put("anchor_id", anchor.id);
        params.put("id", model.id);
        params.put("name", model.name);

        StatementResult result = db.runQuery(query, params);

        long id = result.single().get("n.id").asLong();
        
        return new Parcel(Status.OK, msg, id);
    }

    public Parcel read(long id, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:Player:Active)" +
        " -[:Currently]->" +
        " (d:Data)" +
        " WHERE n.id = $id" + 
        " OPTIONAL MATCH (n:Player:Active)" +
        " -[:Possess]->" +
        " (deck:Deck)" +
        " WHERE n.id = $id" + 
        " RETURN n,d," + 
        " collect(deck.id) as deckIds";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Reading player " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            Record record = result.single();
            Node node = record.get("n").asNode();
            Node data = record.get("d").asNode();
            List<Object> deckIds = record.get("deckIds").asList();

            Player player = new Player();
            player.assignAll(
                node.get("id").asLong(),
                new Date(data.get("created").asLong()),
                data.get("name").asString(),
                deckIds
            );

            Logger.debug("Player: " + player.id + ", " + player.name);

            return new Parcel(Status.OK, msg, player);

        } catch (NoSuchRecordException e) {            
            return new Parcel(Status.NOT_FOUND, msg, null);

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    public Parcel update(Player newPlayer, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:Player)" +
        " -[oc:Currently]->" +
        " (od:Data)" +
        " WHERE n.id = $id" + 
        " CREATE (n)" +
        " -[nc:Currently]->" +
        " (nd:Data { created:TIMESTAMP(), name:$name })" +
        " <-[:Became]-" +
        " (od)" +
        " DELETE oc" +
        " RETURN count(nd) AS updated";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Updating player " + newPlayer.id + " to name: " + newPlayer.name);
        
        params.put("id", newPlayer.id);
        params.put("name", newPlayer.name);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberUpdated = result.single().get("updated").asInt();

            if (numberUpdated == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberUpdated > 1) {
                msg = "Found and updated more than one player";
                
            } else if (numberUpdated == 0) {
                msg = "Could not find (and update) requested player";
                
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
        "MATCH (n:Player)" + 
        " WHERE n.id = $id" + 
        " REMOVE n:Active" +
        " SET n:Deleted" +
        " RETURN count(n) AS deleted";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Deleting player " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberDeleted = result.single().get("deleted").asInt();

            if (numberDeleted == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberDeleted > 1) {
                msg = "Found and deleted more than one player";
                
            } else if (numberDeleted == 0) {
                msg = "Could not find (and delete) requested player";
                
            }

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }
}