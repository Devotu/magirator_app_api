package authentication;

import test.*;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

public class EncryptionTest {
    
    @Test 
    public void getHash() {

        String word = TestVariables.USER_1_PASSWORD;
        String hash = TestVariables.USER_1_PASSWORD_HASH;

        String encryptedWord = Encryption.getHash(word);

        assertTrue( hash.equals(hash) );
    }

    @Test 
    public void generateToken() {

        String token = Encryption.generateToken();

        assertTrue( token.length() == Encryption.TOKEN_LENGTH );
    }
}