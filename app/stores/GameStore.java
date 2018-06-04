package stores;

import model.*;
import transfers.*;
import drivers.*;

import java.util.*;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.types.Node;

import static org.neo4j.driver.v1.Values.parameters;

import play.Logger;

public class GameStore {

    
    public Parcel create(Player anchor, Game model, Neo4jDriver db){

        String msg = "";

        try {            
            model.id = Neo4jDriver.getUniqueId(db);            
        } catch (Exception e) {
            return new Parcel(Status.ERROR, "Could not generate a unique id", null);
        }

        String query =  
        "MATCH " + 
        " (a:Player)" + 
        " WHERE " +
        "  a.id = $anchor_id" +
        " CREATE" + 
        " (a)" +
        "  -[:Created]->" +
        " (n:Game { " +
        "   id:$id," + 
        "   created:TIMESTAMP()," +
        "   end:$end" +
        " })" + 
        " RETURN n," +
        " [] AS resultIds";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Creating game " + model.id);
        
        params.put("anchor_id", anchor.id);
        params.put("id", model.id);
        params.put("end", model.end.toString());

        StatementResult result = db.runQuery(query, params);
        Record record = result.single();            
        Game game = extractGame( record );
        
        return new Parcel(Status.OK, msg, game);
    }

    public Parcel read(long id, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:Game)" +
        " WHERE n.id = $id" + 
        " OPTIONAL MATCH (n:Game)" +
        " <-[:In]-" +
        " (r:Result)" +
        " WHERE n.id = $id" + 
        " RETURN n," +
        " collect(r.id) AS resultIds";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Reading game " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);
            Record record = result.single();            
            Game game = extractGame( record );

            return new Parcel(Status.OK, "", game);

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    /**
     * OK, ERROR
     * -
     * List<Game>
     */
    public Parcel listByPlayer(long playerId, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (a:Player)" +
        " -[:Possess]->" +
        " (:Deck)" +
        " -[:Got]->" +
        " (:Result)" +
        " -[:In]->" +
        " (n:Game)" +
        " <-[:In]-" +
        " (r:Result)" +
        " WHERE a.id = $id" + 
        " RETURN n," +
        " collect(r.id) AS resultIds";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Reading games belonging to " + playerId);
        
        params.put("id", playerId);

        try {
            StatementResult result = db.runQuery(query, params);
            List<Record> records = result.list();
            List<Game> games = new ArrayList();

            for (Record r : records) {
                games.add( extractGame( r ) );
            }

            return new Parcel(Status.OK, "", games);

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }


    public Parcel delete(long id, Neo4jDriver db){
        
        String msg = "";

        String query =  
        "MATCH (n:Game)" + 
        " WHERE n.id = $id" + 
        " DETACH DELETE n" +
        " RETURN count(n) AS deleted";

        //TODO +remove Results

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Deleting game " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberDeleted = result.single().get("deleted").asInt();

            if (numberDeleted == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberDeleted > 1) {
                msg = "Found and deleted more than one game";
                
            } else if (numberDeleted == 0) {
                msg = "Could not find (and delete) requested game";
                
            }

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, false);
    }

    
    private Game extractGame(Record record){

        Node node = record.get("n").asNode();
        List<Object> resultIds = record.get("resultIds").asList();

        Game game = new Game();
        game.assignAll(
            node.get("id").asLong(),
            new Date(node.get("created").asLong()),
            GameEnd.valueOf(node.get("end").asString()),
            resultIds
        );

        return game;
    }
}