package responses;

import transfers.*;

import play.libs.Json;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;

public class JsonResponses {
    
    public static ObjectNode createResponse(Parcel parcel) {
         
        ObjectNode result = Json.newObject();

        boolean isSuccessful = (parcel.status == Status.OK);
        result.put("isSuccessful", isSuccessful);

        result.put("response", parcel.message);

        if (parcel.payload instanceof String) {

            result.put("data", (String) parcel.payload);
        }
        else {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode data = objectMapper.createObjectNode();
            JsonNode value = objectMapper.convertValue(parcel.payload, JsonNode.class);

            data.set(parcel.payload.getClass().getSimpleName(), value);
            result.set("data", data);
        }
 
        Logger.debug(result.toString());
        return result;
    }

    public static ObjectNode createResponse(boolean ok, String response, Object body) {
         
        ObjectNode result = Json.newObject();
        result.put("isSuccessfull", ok);
        result.put("response", response);

        if (body instanceof String) {
            result.put("data", (String) body);
        }
        else {
            result.put("data", (JsonNode) body);
        }
 
        Logger.debug(result.toString());
        return result;
    }

    public static ObjectNode createResponse(boolean ok, String response) {
        
       ObjectNode result = Json.newObject();
       result.put("isSuccessfull", ok);
       result.put("response", response);

       Logger.debug(result.toString());
       return result;
   }
}