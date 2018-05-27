package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.nio.charset.Charset;

public class Encryption {

	public static int TOKEN_LENGTH = 16;
    
    private static char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    
    public static String getHash(String wordToHash)
    {
        String generatedWord = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(wordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedWord = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedWord;
    }

    public static String generateToken(){
    	
    	return generateRandomStringOfLength(TOKEN_LENGTH);
    }

    private static String generateRandomStringOfLength(int length){
    	
    	String string = "";
        Random random = new Random();
        
        while (string.length() < length){
        	string += CHARSET_AZ_09[random.nextInt(CHARSET_AZ_09.length)];       	
        }
    	
    	return string;
    }
}
