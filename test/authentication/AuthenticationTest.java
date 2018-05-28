package authentication;

import drivers.*;
import test.*;
import transfers.*;


import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

public class AuthenticationTest {
    
    @Test 
    public void requestTokenSuccess() {

        Neo4jDriver db = new Neo4jDriver();

        String username = TestVariables.USER_1_NAME;
        String password = TestVariables.USER_1_PASSWORD;

        Parcel result = Authentication.requestToken( username, password, db );
        Token token = (Token) result.payload;

        assertEquals( Status.OK, result.status );
        assertTrue( null != token );
        assertTrue( token.token.length() == Encryption.TOKEN_LENGTH );
    } 

    @Test 
    public void requestTokenFailureUser() {

        Neo4jDriver db = new Neo4jDriver();

        String username = "Errol";
        String password = TestVariables.USER_1_PASSWORD;

        Parcel result = Authentication.requestToken( username, password, db );
        String token = (String) result.payload;

        assertEquals( Status.UNAUTHORIZED, result.status );
        assertTrue( null == token );
    } 

    @Test 
    public void requestTokenFailurePassword() {

        Neo4jDriver db = new Neo4jDriver();

        String username = TestVariables.USER_1_NAME;
        String password = "Wrong";

        Parcel result = Authentication.requestToken( username, password, db );
        String token = (String) result.payload;

        assertEquals( Status.UNAUTHORIZED, result.status );
        assertTrue( null == token );
    }

    @Test 
    public void validateTokenSuccess() {

        Neo4jDriver db = new Neo4jDriver();

        long playerId = TestVariables.PLAYER_2_ID;
        String token = TestVariables.PLAYER_2_TOKEN;

        Parcel result = Authentication.validateGrant( playerId, token, db );

        assertEquals( Status.OK, result.status );
    } 

    @Test 
    public void validateTokenFailureTarget() {

        Neo4jDriver db = new Neo4jDriver();

        long playerId = TestVariables.INVALID_ID;
        String token = TestVariables.PLAYER_2_TOKEN;

        Parcel result = Authentication.validateGrant( playerId, token, db );

        assertEquals( Status.NOT_FOUND, result.status );
    }

    @Test 
    public void validateTokenFailureToken() {

        Neo4jDriver db = new Neo4jDriver();

        long playerId = TestVariables.PLAYER_2_ID;
        String token = "XXX";

        Parcel result = Authentication.validateGrant( playerId, token, db );

        assertEquals( Status.NOT_FOUND, result.status );
    }
}