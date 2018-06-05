package v1;

import authentication.*;
import drivers.*;
import model.*;
import responses.*;
import stores.*;
import transfers.*;

import java.util.List;

import play.mvc.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.http.HttpStatus;

public class GameController extends Controller{

    public play.mvc.Result create(){

        try {
            JsonNode json = request().body().asJson();
            Long anchorId = json.get("playerId").asLong();
            String token = json.get("token").textValue();
            String end = json.get("end").textValue();

            Game game = new Game();
            game.assignNew( GameEnd.valueOf(end) );

            try ( Neo4jDriver db = new Neo4jDriver() ){

                GameStore gameStore = new GameStore();
                PlayerStore playerStore = new PlayerStore();

                Parcel playerParcel = playerStore.read( anchorId, db );
                Player player = (Player) playerParcel.payload;
                if (player == null) {
                    return notFound();
                }

                Parcel grantParcel = Authentication.validateGrant( anchorId, token, db );
                if (grantParcel.status != Status.OK) {
                    return unauthorized();
                }

                Parcel gameParcel = gameStore.create( player, game, db );

                if( gameParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertToData( gameParcel.payload ) );
                }
            }

        } catch (IllegalArgumentException e) {            
            return badRequest();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }

    
    public play.mvc.Result get(long id){

        try {
            
            try ( Neo4jDriver db = new Neo4jDriver() ){

                GameStore gameStore = new GameStore();
                Parcel gameParcel = gameStore.read( id, db );

                if( gameParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertToData( gameParcel.payload ) );
                } 
                else if ( gameParcel.status == Status.NOT_FOUND )
                {
                    return notFound();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }

    
    public play.mvc.Result listByPlayer(long playerId){

        try {
            
            try ( Neo4jDriver db = new Neo4jDriver() ){

                GameStore gameStore = new GameStore();

                Parcel gamesParcel = gameStore.listByPlayer( playerId, db );

                if( gamesParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertListToData( (List)gamesParcel.payload, new Game() ) );
                } 
                else if ( gamesParcel.status == Status.NOT_FOUND )
                {
                    return notFound();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }  
}