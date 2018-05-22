package responses;

import transfers.*;

import play.libs.Json;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class JsonResponses {

    public static ObjectNode convertToData(Object object) {
         
        ObjectNode result = Json.newObject();

        if (object instanceof String) {

            result.put("data", (String) object);
        }
        else {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode data = objectMapper.createObjectNode();
            JsonNode value = objectMapper.convertValue(object, JsonNode.class);

            data.set(object.getClass().getSimpleName(), value);
            result.set("data", data);
        }
 
        Logger.debug(result.toString());
        return result;
    }
}