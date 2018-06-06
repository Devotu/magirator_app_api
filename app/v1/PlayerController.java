package v1;

import drivers.*;
import model.*;
import responses.*;
import stores.*;
import transfers.*;

import java.util.*;

import play.mvc.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.http.HttpStatus;

public class PlayerController extends Controller{

    public play.mvc.Result get(long id){

        try {
            PlayerStore playerStore = new PlayerStore();
            Neo4jDriver db = Neo4jDriverFactory.getDriver();
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

    public play.mvc.Result list(){

        try {
            PlayerStore playerStore = new PlayerStore();
            Neo4jDriver db = Neo4jDriverFactory.getDriver();
            Parcel dataParcel = playerStore.list( db );

            if( dataParcel.status == Status.OK )
            {
                return ok( JsonResponses.convertListToData( (List)dataParcel.payload, new Player() ) );
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