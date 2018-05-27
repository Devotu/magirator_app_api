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

        String encryptedPassword = Encryption.getHash(password);
        Logger.debug("Encrypted password: " + encryptedPassword);

        Parcel result = Authentication.requestToken( username, encryptedPassword, db );
        String token = (String) result.payload;

        Logger.debug("Token payload: " + result.payload);
        Logger.debug("Token: " + token);
        Logger.debug("Token length: " + token.length());

        assertEquals( Status.OK, result.status );
        assertTrue( null != token );
        assertTrue( token.length() == Encryption.TOKEN_LENGTH );
    } 

    // @Test 
    // public void requestTokenFailureUnauthorized() {

    //     Neo4jDriver db = new Neo4jDriver();

    //     String username = "Adam";
    //     String password = "Hemligt";

    //     String encryptedPassword = Encryption.getHash(password);
    //     Logger.debug(encryptedPassword);

    //     Parcel result = Authentication.requestToken( username, encryptedPassword, db );

    //     assertEquals( Status.OK, result.status );
    //     assertTrue( result.payload.getClass().isInstance(String.class) );
    //     assertTrue( null != result.payload );
    // }
}