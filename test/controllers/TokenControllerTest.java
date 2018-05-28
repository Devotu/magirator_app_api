package v1;

import authentication.*;
import test.*;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class TokenControllerTest {

    @Test
    public void getSuccessful(){

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            response = objectMapper.readValue(
                Requests.makeRequest(
                    TestVariables.BASE_URL + 
                        "/token?username=" + 
                        TestVariables.USER_1_NAME + 
                        "&password=" +
                        TestVariables.USER_1_PASSWORD, 
                    "GET"
                ), ObjectNode.class
            );
                
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.debug(response.toString());
        
        assertTrue(response.get("data").has("Token"));
        assertTrue(response.get("data").get("Token").has("token"));
        assertTrue(response.get("data").get("Token").get("token").asText().length() == Encryption.TOKEN_LENGTH);
    }
}