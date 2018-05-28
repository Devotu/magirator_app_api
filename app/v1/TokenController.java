package v1;

import authentication.*;
import drivers.*;
import responses.*;
import stores.*;
import transfers.*;

import play.mvc.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.http.HttpStatus;

public class TokenController extends Controller{

    public Result requestToken(String username, String password){

        try {
            Neo4jDriver db = new Neo4jDriver();

            Parcel dataParcel = Authentication.requestToken( username, password, db);

            if( dataParcel.status == Status.OK )
            {
                return ok( JsonResponses.convertToData( dataParcel.payload ) );
            } 
            else if ( dataParcel.status == Status.NOT_FOUND )
            {
                return notFound();
            } 
            else if ( dataParcel.status == Status.UNAUTHORIZED )
            {
                return unauthorized();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }    
}