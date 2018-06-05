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

public class NotGameControllerTest {

    @Test
    public void createSuccessful(){
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode createJson = objectMapper.readValue("" +
                "{ " +
                "\"playerId\" : \"" + TestVariables.PLAYER_2_ID + "\", " +
                "\"token\" : \"" + TestVariables.PLAYER_2_TOKEN + "\", " +
                "\"end\" : \"" + GameEnd.VICTORY + "\"" +
                "}", ObjectNode.class);
            
                Logger.debug(createJson.toString());


            ObjectNode response = objectMapper.createObjectNode();

            try {
                response = objectMapper.readValue(
                    Requests.makeRequest(
                        TestVariables.BASE_URL + "/games", 
                        "POST",
                        createJson
                    ), ObjectNode.class
                );
                    
            } catch (IOException e) {
                e.printStackTrace();
            }

            Logger.debug(response.toString());
    
            assertTrue(response.get("data").has("Game"));
            assertTrue(response.get("data").get("Game").has("id"));
            assertTrue(response.get("data").get("Game").has("end"));

        } catch (Exception e) {
            Logger.debug("Game Error");
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
                    TestVariables.BASE_URL + "/games/" + TestVariables.GAME_1_ID, 
                    "GET"
                ), ObjectNode.class
            );
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertTrue(response.get("data").has("Game"));
        assertTrue(response.get("data").get("Game").has("id"));
        assertTrue(response.get("data").get("Game").has("end"));
    }

    @Test
    public void playerListSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        List<Game> games = new ArrayList<>();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + "/players/" + TestVariables.PLAYER_2_ID + "/games", 
                    "GET"
                ), ObjectNode.class
            );

            for (JsonNode node : response.get("data").get("Games")) {
                games.add( objectMapper.treeToValue(node, Game.class) );
            }
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertTrue(response.get("data").has("Games"));
        assertTrue(games != null);
        assertTrue(games.size() > 0);
    }
}