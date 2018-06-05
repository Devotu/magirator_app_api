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

public class DeckController extends Controller{

    public play.mvc.Result create(){

        try {
            JsonNode json = request().body().asJson();
            Long anchorId = json.get("playerId").asLong();
            String token = json.get("token").textValue();
            String name = json.get("name").textValue();
            String format = json.get("format").textValue();
            String theme = json.get("theme").textValue();
            boolean black = json.get("black").asBoolean();
            boolean white = json.get("white").asBoolean();
            boolean red = json.get("red").asBoolean();
            boolean green = json.get("green").asBoolean();
            boolean blue = json.get("blue").asBoolean();
            boolean colorless = json.get("colorless").asBoolean();

            Deck deck = new Deck();
            deck.assignNew(name, DeckFormat.valueOf(format), theme, black, white, red, green, blue, colorless);


            try ( Neo4jDriver db = new Neo4jDriver() ){

                DeckStore deckStore = new DeckStore();
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

                Parcel deckParcel = deckStore.create( player, deck, db );

                if( deckParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertToData( deckParcel.payload ) );
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

                DeckStore deckStore = new DeckStore();
                Parcel deckParcel = deckStore.read( id, db );

                if( deckParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertToData( deckParcel.payload ) );
                } 
                else if ( deckParcel.status == Status.NOT_FOUND )
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

                DeckStore deckStore = new DeckStore();

                Parcel decksParcel = deckStore.listByPlayer( playerId, db );

                if( decksParcel.status == Status.OK )
                {
                    return ok( JsonResponses.convertListToData( (List)decksParcel.payload, new Deck() ) );
                } 
                else if ( decksParcel.status == Status.NOT_FOUND )
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