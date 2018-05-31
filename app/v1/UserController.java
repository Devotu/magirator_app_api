package v1;

import drivers.*;
import model.*;
import responses.*;
import stores.*;
import transfers.*;

import java.util.Date;

import play.mvc.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.apache.http.HttpStatus;

public class UserController extends Controller{

    public play.mvc.Result signup(){

        JsonNode json = request().body().asJson();
        String userName = json.get("userName").textValue();
        String password = json.get("password").textValue();
        String playerName = json.get("playerName").textValue();

        if ( !inputIsValid( userName, password, playerName ) ) {
            return badRequest();
        }

        try {

            UserStore userStore = new UserStore();
            PlayerStore playerStore = new PlayerStore();
            Neo4jDriver db = new Neo4jDriver();

            User user = new User();
            user.assignNew( userName, password );
            
            Parcel userData = userStore.create( null, user, db );
            User createdUser = (User) userData.payload;

            Player player = new Player();
            player.assignNew( playerName );

            Parcel playerParcel = playerStore.create( createdUser, player, db );

            if( playerParcel.status == Status.OK )
            {
                return ok();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return internalServerError();
    }

    private boolean inputIsValid(String username, String password, String playername){
        
        if ( "".equals(username) || "".equals(playername) || password.length() < 8 || password.length() > 30 ) {
            return false;
        }

        return true;
    }
}