package responses;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import play.libs.Json;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import model.*;
import transfers.*;
import drivers.*;
import test.*;

import java.util.Date;

public class JsonResponsesTest {

    @Test
    public void parcelToJson() {
        
        Player player = new Player();

        player.assignAll(1, new Date(), "Jason");
        
        Parcel parcel = new Parcel(Status.OK, "Testing", player);

        ObjectNode response = JsonResponses.createResponse(parcel);

        Logger.debug(response.toString());

        assertTrue(response.get("isSuccessful").asBoolean());
        assertTrue(response.get("data").has("Player"));
        assertEquals(player.name, response.get("data").get("Player").get("name").asText());
    }
}