package v1;

import drivers.*;
import responses.*;
import stores.*;
import transfers.*;

import play.mvc.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.http.HttpStatus;

public class PlayerController extends Controller{

    public Result get(long id){

        try {
            //Read data
            PlayerStore playerStore = new PlayerStore();
            Neo4jDriver db = new Neo4jDriver();
            Parcel dataParcel = playerStore.read( id, db );

            if( dataParcel.status == Status.OK )
            {
                return ok( JsonResponses.convertToData( dataParcel.payload ) );
            } 
            else if ( dataParcel.status == Status.NOT_FOUND )
            {
                return notFound();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }    
}