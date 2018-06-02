package v1;

import test.*;
import model.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class DeckControllerTest {

    @Test
    public void createSuccessful(){
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode createJson = objectMapper.readValue("" +
                "{ " +
                "\"playerId\" : \"" + TestVariables.PLAYER_2_ID + "\", " +
                "\"token\" : \"" + TestVariables.PLAYER_2_TOKEN + "\", " +
                "\"name\" : \"GeneratedDeck\", " +
                "\"format\" : \"COMMANDER\", " +
                "\"theme\" : \"Testing\", " +
                "\"black\": false, " +
                "\"white\": false, " +
                "\"red\": true, " +
                "\"green\": false, " +
                "\"blue\": false, " +
                "\"colorless\": true" +
                "}", ObjectNode.class);
            
            ObjectNode response = objectMapper.createObjectNode();

            try {
                response = objectMapper.readValue(
                    Requests.makeRequest(
                        TestVariables.BASE_URL + "/decks", 
                        "POST",
                        createJson
                    ), ObjectNode.class
                );
                    
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            assertTrue(response.get("data").has("Deck"));
            assertTrue(response.get("data").get("Deck").has("id"));
            assertTrue(response.get("data").get("Deck").has("name"));
            assertTrue(response.get("data").get("Deck").has("name"));
            assertTrue(!response.get("data").get("Deck").get("black").asBoolean());
            assertTrue(response.get("data").get("Deck").get("red").asBoolean());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + "/decks/" + TestVariables.DECK_1_ID, 
                    "GET"
                ), ObjectNode.class
            );
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertTrue(response.get("data").has("Deck"));
        assertTrue(response.get("data").get("Deck").has("id"));
        assertTrue(response.get("data").get("Deck").has("name"));
        assertTrue(response.get("data").get("Deck").has("name"));
        assertTrue(response.get("data").get("Deck").get("black").asBoolean());
        assertTrue(!response.get("data").get("Deck").get("red").asBoolean());
    }

    @Test
    public void playerListSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        List<Deck> decks = new ArrayList();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + "/players/" + TestVariables.PLAYER_1_ID + "/decks", 
                    "GET"
                ), ObjectNode.class
            );

            for (JsonNode node : response.get("data").get("Decks")) {
                decks.add( objectMapper.treeToValue(node, Deck.class) );
            }
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertTrue(response.get("data").has("Decks"));
        assertTrue(decks != null);
        assertTrue(decks.size() > 0);
    }
}