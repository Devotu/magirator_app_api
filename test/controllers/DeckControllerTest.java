package v1;

import test.*;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class DeckControllerTest {

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
}