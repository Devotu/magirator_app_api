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
import java.util.ArrayList;

public class JsonResponsesTest {

    @Test
    public void parcelToJson() {
        
        Player player = new Player();

        player.assignAll(1, new Date(), "Jason", new ArrayList());
        
        Parcel parcel = new Parcel(Status.OK, "Testing", player);

        ObjectNode response = JsonResponses.convertToData(parcel.payload);

        Logger.debug(response.toString());

        assertTrue(response.get("data").has("Player"));
        assertEquals(player.name, response.get("data").get("Player").get("name").asText());
    }
}