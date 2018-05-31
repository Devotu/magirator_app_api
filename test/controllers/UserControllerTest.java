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

public class UserControllerTest {

    @Test
    public void signupSuccessful(){
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode createJson = objectMapper.readValue("{ \"userName\" : \"Elaine\", \"password\" : \"Elaine123\", \"playerName\" : \"Frida\" }", ObjectNode.class);
    
            String response = Requests.makeRequest(
                TestVariables.BASE_URL + "/users", 
                "POST",
                createJson
            );
    
            assertTrue("".equals(response));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void signupBadInput(){
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode createJson = objectMapper.readValue("{ \"userName\" : \"Elaine\", \"password\" : \"Elaine123\", \"playerName\" : \"\" }", ObjectNode.class);
    
            String response = Requests.makeRequest(
                TestVariables.BASE_URL + "/users", 
                "POST",
                createJson
            );
    
            assertTrue( response.contains("Error") );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}