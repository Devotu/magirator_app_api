package v1;

import model.*;
import test.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class PlayerControllerTest {

    @Test
    public void getSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + "/players/" + TestVariables.PLAYER_1_ID, 
                    "GET"
                ), ObjectNode.class
            );
                
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertTrue(response.get("data").has("Player"));
        assertTrue(response.get("data").get("Player").has("id"));
        assertTrue(response.get("data").get("Player").has("name"));
    }


    @Test
    public void listSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        List<Player> players = new ArrayList();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + "/players", 
                    "GET"
                ), ObjectNode.class
            );

            for (JsonNode node : response.get("data").get("Players")) {
                players.add( objectMapper.treeToValue(node, Player.class) );
            }
                
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.debug(response.toString());
        
        assertTrue(response.get("data").has("Players"));
        assertTrue(players != null);
        assertTrue(players.size() > 0);
    }
}