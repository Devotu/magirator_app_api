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

public class DeckStore implements Store<Player, Deck> {

    @Override
    public Parcel create(Player anchor, Deck model, Neo4jDriver db){

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
        "  -[:Possess]->" +
        " (n:Deck:Active:PERSISTENT { id:$id })" + 
        "  -[:Currently]->" +
        " (d:Data {" + 
        "   created:TIMESTAMP()," +
        "   name:$name," +
        "   format:$format," +
        "   theme:$theme," +
        "   black:$black," +
        "   white:$white," +
        "   red:$red," +
        "   green:$green," +
        "   blue:$blue," +
        "   colorless:$colorless" +
        " })" + 
        " RETURN n,d";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Creating deck " + model.id + ", " + model.name);
        
        params.put("anchor_id", anchor.id);
        params.put("id", model.id);
        params.put("name", model.name);
        params.put("format", model.format.name());
        params.put("theme", model.theme);
        params.put("black", model.black);
        params.put("white", model.white);
        params.put("red", model.red);
        params.put("green", model.green);
        params.put("blue", model.blue);
        params.put("colorless", model.colorless);

        StatementResult result = db.runQuery(query, params);
        Record record = result.single();            
        Deck deck = extractDeck( record );
        
        return new Parcel(Status.OK, msg, deck);
    }

    public Parcel read(long id, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH (n:Deck)" +
        " -[:Currently]->" +
        " (d:Data)" +
        " WHERE n.id = $id" + 
        " RETURN n,d";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Reading deck " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);
            Record record = result.single();            
            Deck deck = extractDeck( record );

            return new Parcel(Status.OK, "ok", deck);

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    public Parcel update(Deck newDeck, Neo4jDriver db){

        String msg = "";

        String query =  
        "MATCH" + 
        " (n:Deck)" +
        " -[oc:Currently]->" +
        " (od:Data)" +
        " WHERE" +
        "  n.id = $id" + 
        " CREATE (n)" +
        "  -[nc:Currently]->" +
        " (nd:Data {" +
        "   created:TIMESTAMP()," +
        "   name:$name," +
        "   format:$format," +
        "   theme:$theme," +
        "   black:$black," +
        "   white:$white," +
        "   red:$red," +
        "   green:$green," +
        "   blue:$blue," +
        "   colorless:$colorless" +
        " })" + 
        "  <-[:Became]-" +
        " (od)" +
        " DELETE oc" +
        " RETURN count(nd) AS updated";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Updating deck " + newDeck.id + " to name: " + newDeck.name);
        
        params.put("id", newDeck.id);
        params.put("name", newDeck.name);
        params.put("format", newDeck.format.toString());
        params.put("theme", newDeck.theme);
        params.put("black", newDeck.black);
        params.put("white", newDeck.white);
        params.put("red", newDeck.red);
        params.put("green", newDeck.green);
        params.put("blue", newDeck.blue);
        params.put("colorless", newDeck.colorless);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberUpdated = result.single().get("updated").asInt();

            if (numberUpdated == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberUpdated > 1) {
                msg = "Found and updated more than one deck";
                
            } else if (numberUpdated == 0) {
                msg = "Could not find (and update) requested deck";
                
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
        "MATCH (n:Deck)" + 
        " WHERE n.id = $id" + 
        " REMOVE n:Active" +
        " SET n:Deleted" +
        " RETURN count(n) AS deleted";

        HashMap<String, Object> params = new HashMap<>();

        Logger.debug("Deleting deck " + id);
        
        params.put("id", id);

        try {
            StatementResult result = db.runQuery(query, params);

            int numberDeleted = result.single().get("deleted").asInt();

            if (numberDeleted == 1) {
                return new Parcel(Status.OK, "ok", true);

            } else if (numberDeleted > 1) {
                msg = "Found and deleted more than one deck";
                
            } else if (numberDeleted == 0) {
                msg = "Could not find (and delete) requested deck";
                
            }

        } catch (Exception e) {
            Logger.debug(this.getClass().getName() + ": " + e.toString());             
            e.printStackTrace();
        }

        return new Parcel(Status.ERROR, msg, null);
    }

    private Deck extractDeck(Record record){

        Node node = record.get("n").asNode();
        Logger.debug("Got node");
        Node data = record.get("d").asNode();
        Logger.debug("Got data");

        Deck deck = new Deck();
        deck.assignAll(
            node.get("id").asLong(),
            new Date(data.get("created").asLong()),
            data.get("name").asString(),
            DeckFormat.valueOf(data.get("format").asString()),
            data.get("theme").asString(),
            data.get("black").asBoolean(),
            data.get("white").asBoolean(),
            data.get("red").asBoolean(),
            data.get("green").asBoolean(),
            data.get("blue").asBoolean(),
            data.get("colorless").asBoolean()
        );

        return deck;
    }
}