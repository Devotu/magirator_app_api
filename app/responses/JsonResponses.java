package responses;

import transfers.*;

import java.util.List;

import play.libs.Json;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class JsonResponses {

    public static ObjectNode convertToData(Object object) {
         
        ObjectNode result = Json.newObject();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode data = objectMapper.createObjectNode();
        JsonNode value = objectMapper.convertValue(object, JsonNode.class);

        data.set(object.getClass().getSimpleName(), value);
        result.set("data", data);
 
        Logger.debug(result.toString());
        return result;
    }

    public static ObjectNode convertListToData(List list, Object type) {
         
        ObjectNode result = Json.newObject();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode data = objectMapper.createObjectNode();
        ArrayNode items = objectMapper.createArrayNode();

        for (Object object : list) {
            JsonNode value = objectMapper.convertValue(object, JsonNode.class);
            items.add(value);
        }

        data.set(type.getClass().getSimpleName() + "s", items);
        result.set("data", data);
 
        Logger.debug(result.toString());
        return result;
    }
}